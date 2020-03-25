package develop.p2p.chatchan.Server.Thread;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import develop.p2p.chatchan.util.JsonObj;
import develop.p2p.chatchan.Main;
import develop.p2p.chatchan.Message.EncryptManager;
import develop.p2p.chatchan.Message.MessageSender;
import develop.p2p.chatchan.Message.Response.ResponseBuilder;
import develop.p2p.chatchan.Player.Player;
import develop.p2p.chatchan.Player.PlayerList;
import org.slf4j.Logger;

import java.io.*;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ChatThread extends  Thread
{
    private Player player;
    private Socket socket;
    private Logger logger;
    public ChatThread(Socket socket)
    {
        this.socket = socket;
        this.logger = Main.logger;
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
                logger.info("[CHAT] Text from client: " + line + "\n");
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
                        case "send":
                            Player sender = Main.playerList.getPlayerFromName(node.get("name").asText());
                            boolean stopFlag = false;
                            if (!sender.isChatAuthorized)
                                stopFlag = true;
                            else if(!sender.token.equals(node.get("token").asText()))
                                stopFlag = true;
                            if (stopFlag)
                            {
                                send.println("{\"code\": 400}");
                                break;
                            }
                            PlayerList players = Main.playerList;
                            for (Player sendPlayer : players.getPlayers())
                            {
                                try
                                {
                                    if (!sendPlayer.isChatAuthorized)
                                        continue;
                                    PrintWriter cSend = new PrintWriter(sendPlayer.chatSocket.getOutputStream(), true);
                                    String encryptedMessage = node.get("message").asText();
                                    String decryptedMessage = EncryptManager.decrypt(encryptedMessage, this.player.encryptKey);
                                    logger.info("Received message by " + sender.name + "(" + sender.ip + "): " + decryptedMessage + "\n");
                                    String newEncryptedMessage = EncryptManager.encrypt(decryptedMessage, sendPlayer.decryptKey);
                                    MessageSender msgSender = new MessageSender();
                                    msgSender.name = sender.name;
                                    msgSender.content = newEncryptedMessage;
                                    Calendar calendar = Calendar.getInstance();
                                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                    msgSender.date = format.format(calendar);
                                    cSend.println(mapper.writeValueAsString(msgSender));
                                }
                                catch (Exception e)
                                {
                                    send.println(ResponseBuilder.getResponse(405));
                                }
                            }
                            break;
                    }
                    continue;
                }
                send.println("{\"code\": 404}");

            }
        }
        catch (Exception e)
        {
            //logger.info("[CHAT] Client Disconnected: " + player.name + "(" + player.ip.replace("/", "") + ")\n");
            e.printStackTrace();
        }
        finally
        {
            if (player == null || player.name == null)
            {
                logger.info("[CHAT] Client Disconnected: Unknown(" + socket.getRemoteSocketAddress().toString() + ")\n");
                if (player != null)
                {
                    try
                    {
                        Main.playerList.leave(player);
                    }
                    catch (JsonProcessingException e)
                    {
                        logger.error("[CHAT] Error:");
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
            logger.info("[CHAT] Client Disconnected: " + player.name + "(" + player.ip.replace("/", "") + ")\n");
            try
            {
                Main.playerList.leave(player);
            }
            catch (JsonProcessingException e)
            {
                logger.error("[CHAT] Error:");
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
