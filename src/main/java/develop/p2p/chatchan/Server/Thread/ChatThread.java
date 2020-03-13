package develop.p2p.chatchan.Server.Thread;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import develop.p2p.chatchan.JsonObj;
import develop.p2p.chatchan.Main;
import develop.p2p.chatchan.Player.Player;

import java.io.*;
import java.net.Socket;

public class ChatThread extends  Thread
{
    public Player player;
    public Socket socket;
    public ChatThread(Socket socket)
    {
        this.socket = socket;
    }

    @Override
    public void run()
    {
        try
        {

            InputStream stream = socket.getInputStream();
            BufferedReader read = new BufferedReader(new InputStreamReader(stream));
            PrintWriter send = new PrintWriter(socket.getOutputStream(), true);
            ObjectMapper mapper = new ObjectMapper();
            while(true)
            {
                if (socket.isClosed())
                    break;
                byte[] data = new byte[1];
                int rs = stream.read(data, 0, 1);
                if (rs == -1)
                    break;
                String line = new String(data);
                line += read.readLine();
                Main.logger.info("[CHAT] Text from client: " + line + "\n");
                if (JsonObj.isJson(line))
                {
                    JsonNode node = mapper.readTree(line);
                    switch (node.get("exec").asText())
                    {
                        case "join":
                            if (node.get("token").isNull() || node.get("name").isNull())
                            {
                                String response = Main.playerList.join(player, "");
                                send.println(response);
                                socket.close();
                                break;
                            }
                            String token = node.get("token").asText();
                            String name = node.get("name").asText();
                            Player player = Main.playerList.getPlayerFromName(name);
                            if (player != null)
                            {
                                String response = Main.playerList.join(player, token);
                                JsonNode nodes = mapper.readTree(response);
                                if (nodes.get("code").asInt() == 200)
                                {
                                    send.println(response);
                                    this.player = player;
                                    this.player.chatSocket = socket;
                                    this.player.isChatAuthorized = true;
                                    Main.playerList.getPlayerFromName(name).isChatAuthorized = true;
                                }
                                else
                                {
                                    send.println(response);
                                    break;
                                }
                            }
                            else
                            {
                                System.out.println("ｇｙ");
                            }
                            break;
                        case "leave":
                            send.println("{\"code\": 200}");
                            socket.close();
                            break;
                    }
                    continue;
                }
                send.println("{\"code\": 404}");

            }
        }
        catch (Exception e)
        {
            //Main.logger.info("[CHAT] Client Disconnected: " + player.name + "(" + player.ip.replace("/", "") + ")\n");
            e.printStackTrace();
        }
        finally
        {
            if (player == null || player.name == null)
            {
                Main.logger.info("[CHAT] Client Disconnected: Unknown(" + socket.getRemoteSocketAddress().toString() + ")\n");
                if (player != null)
                {
                    try
                    {
                        Main.playerList.leave(player);
                    }
                    catch (JsonProcessingException e)
                    {
                        Main.logger.error("[CHAT] Error:");
                        e.printStackTrace();
                    }
                }
                try
                {
                    if (socket != null)
                        socket.close();
                }
                catch (Exception ignored)
                {
                }
                return;
            }
            Main.logger.info("[CHAT] Client Disconnected: " + player.name + "(" + player.ip.replace("/", "") + ")\n");
            try
            {
                Main.playerList.leave(player);
            }
            catch (JsonProcessingException e)
            {
                Main.logger.error("[CHAT] Error:");
                e.printStackTrace();
            }
            try
            {
                if (socket != null)
                    socket.close();
            }
            catch (Exception ignored)
            {
            }
        }
    }
}
