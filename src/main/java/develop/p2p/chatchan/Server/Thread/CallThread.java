package develop.p2p.chatchan.Server.Thread;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import develop.p2p.chatchan.Main;
import develop.p2p.chatchan.Message.EncryptManager;
import develop.p2p.chatchan.Player.Player;

import java.io.*;
import java.net.Socket;

public class CallThread extends  Thread
{
    public Player player;
    public Socket socket;
    public CallThread(Socket socket)
    {
        Main.logger.info("[CALL] Player Joining: " + socket.getRemoteSocketAddress() + "\n");
        this.socket = socket;
        this.player = new Player();
        this.player.callSocket = socket;
    }

    @Override
    public void run()
    {
        player.name = "Unknown";
        try
        {

            InputStream stream = socket.getInputStream();

            player.ip = socket.getRemoteSocketAddress().toString();
            BufferedReader read = new BufferedReader(new InputStreamReader(stream));
            PrintWriter send = new PrintWriter(socket.getOutputStream(), true);
            ObjectMapper mapper = new ObjectMapper();
            while(true)
            {
                if (socket.isClosed())
                    break;
                byte[] data = new byte[1];
                if (stream.read(data, 0, 1) == -1)
                    break;
                try
                {
                    String line;
                    line = read.readLine();
                    line.length(); //isNull
                    line = "{" + line;
                    line = line.replace("\n", "").replace("\r", "");
                    Main.logger.info("[CALL] Text from client: " + line + "\n");
                    try
                    {
                        JsonNode root = mapper.readTree(line);
                        switch (root.get("exec").asText())
                        {
                            case "join":
                                if (root.get("name").isNull())
                                {
                                    String response = Main.playerList.join(player, "");
                                    send.println(response);
                                    socket.close();
                                    break;
                                }
                                player.name = root.get("name").asText();
                                String encryptKey;
                                String decryptKey;
                                Main.logger.info("[ECGM] Generating Encrypt key...");
                                encryptKey = EncryptManager.generateEncryptKey();
                                System.out.println("OK");
                                Main.logger.info("[ECGM] Generating Decrypt key...");
                                decryptKey = EncryptManager.generateDecryptKey();
                                System.out.println("OK");
                                player.encryptKey = encryptKey;
                                player.decryptKey = decryptKey;
                                String message = Main.playerList.join(player);
                                ObjectMapper mappers = new ObjectMapper();
                                JsonNode node = mappers.readTree(message);
                                int code = node.get("code").asInt();
                                if (code == 200)
                                    send.println(message);
                                else
                                {
                                    socket.close();
                                    break;
                                }
                                break;
                            case "leave":
                                if (player != null)
                                {
                                    String response =  Main.playerList.leave(player);
                                    send.println(response);
                                    socket.close();
                                    break;
                                }
                                break;
                            default:
                                send.println("{\"code\": 404}");
                        }
                    }
                    catch (IOException e)
                    {
                        send.println("{\"code\": 404}");
                    }

                }
                catch (Exception ignored)
                {
                    send.println("{\"code\": 404}");
                }
            }
        }
        catch (Exception ignored)
        {
            //Main.logger.info("\n[CALL] Client Disconnected: " + player.name + "(" + player.ip.replace("/", "") + ")\n");

        }
        finally
        {
            if (player == null || player.name == null)
            {
                Main.logger.info("[CALL] Client Disconnected: Unknown(" + socket.getRemoteSocketAddress().toString() + ")\n");
                if (player != null)
                {
                    try
                    {
                        Main.playerList.leave(player);
                    }
                    catch (JsonProcessingException e)
                    {

                        Main.logger.error("[CALL] Error:");
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
            Main.logger.info("[CALL] Client Disconnected: " + player.name + "(" + player.ip.replace("/", "") + ")\n");
            try
            {
                Main.playerList.leave(player);
            }
            catch (JsonProcessingException e)
            {

                Main.logger.error("[CALL] Error:");
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
