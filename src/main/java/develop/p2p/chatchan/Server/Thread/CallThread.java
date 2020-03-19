package develop.p2p.chatchan.Server.Thread;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import develop.p2p.chatchan.IntToString;
import develop.p2p.chatchan.Main;
import develop.p2p.chatchan.Message.Response.ResponseBuilder;
import develop.p2p.chatchan.Player.Player;
import develop.p2p.chatchan.Player.PlayerList;

import java.io.*;
import java.net.Socket;
import java.util.Calendar;
import java.util.Random;

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
            player.ip = socket.getRemoteSocketAddress().toString();
            BufferedReader read = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter send = new PrintWriter(socket.getOutputStream(), true);
            ObjectMapper mapper = new ObjectMapper();
            while(true)
            {
                if (socket.isClosed())
                    break;
                byte[] data = new byte[1];
                if (socket.getInputStream().read(data, 0, 1) == -1)
                    break;
                try
                {
                    String line = read.readLine();
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
                                    send.println(Main.playerList.join(player, ""));
                                    socket.close();
                                    break;
                                }
                                player.name = root.get("name").asText();
                                String encryptKey = "";
                                String decryptKey = "";
                                Main.logger.info("[ECGM] Generating Encrypt key...");
                                for (int ii = 0; ii < Main.keyLength; ii++)
                                {
                                    Random random = new Random();
                                    long seed = 314L;
                                    do
                                    {
                                        seed = seed + ((seed ^ 0x5DEECE66DL + 0xBL) & (0xFFFFFFFFFFFFL)) * new Random().nextLong();
                                    }
                                    while((new Random(random.nextInt(24)).nextInt(1024) > 999));
                                    Calendar cTime = Calendar.getInstance();
                                    seed = seed +
                                            (cTime.get(Calendar.SECOND) * cTime.get(Calendar.HOUR) * 0x7c8a3b4L) +
                                            (cTime.get(Calendar.SECOND) * 0x3a4bdeL) +
                                            (cTime.get(Calendar.MINUTE) * cTime.get(Calendar.YEAR)) * 0x4307a7L +
                                            (cTime.get(Calendar.MILLISECOND) * 0xf7defL) ^ 0x3ad8025f;
                                    seed = (seed * 0x5DEECE66DL + 0xBL) & 0xFFFFFFFFFFFFL;
                                    encryptKey = encryptKey + (IntToString.getStringFromInt(((int) (seed >> 17)) % 62));
                                }
                                System.out.println("OK");
                                Main.logger.info("[ECGM] Generating Decrypt key...");
                                for (int ii = 0; ii < Main.keyLength; ii++)
                                {
                                    Random random = new Random();
                                    long seed = 314L;
                                    do
                                    {
                                        seed = seed + ((seed ^ 0x5DEECE66DL + 0xBL) & (0xFFFFFFFFFFFFL)) * new Random().nextLong();
                                    }
                                    while((new Random(random.nextInt(24)).nextInt(1024) > 999));
                                    Calendar cTime = Calendar.getInstance();
                                    seed = seed +
                                            (cTime.get(Calendar.HOUR) * cTime.get(Calendar.MINUTE) * 0x4c1906) +
                                            (cTime.get(Calendar.MILLISECOND) * 0x5ac0db) +
                                            (cTime.get(Calendar.YEAR) * cTime.get(Calendar.SECOND)) * 0x4307a7L +
                                            (cTime.get(Calendar.MONTH) * 0x5f24f) ^ 0x3ad8025f;
                                    seed = (seed * 0x5DEECE66DL + 0xBL) & 0xFFFFFFFFFFFFL;
                                    decryptKey = decryptKey + (IntToString.getStringFromInt(((int) (seed >> 17)) % 62));
                                }
                                System.out.println("OK");
                                player.encryptKey = encryptKey;
                                player.decryptKey = decryptKey;
                                String message = Main.playerList.join(player);
                                ObjectMapper mappers = new ObjectMapper();
                                if (mappers.readTree(message).get("code").asInt() == 200)
                                {
                                    send.println(message);
                                }
                                else
                                {
                                    socket.close();
                                    break;
                                }
                                break;
                            case "leave":
                                if (player != null)
                                {
                                    send.println(Main.playerList.leave(player));
                                    socket.close();
                                    break;
                                }
                                break;
                            case "send":
                                for (Player player : Main.playerList.getPlayers())
                                {
                                    try
                                    {
                                        // 注意: 未使用の変数が発見されました。
                                        PrintWriter cSend = new PrintWriter(socket.getOutputStream(), true);
                                        String encryptedMessage = root.get("message").asText();

                                    }
                                    catch (Exception e)
                                    {
                                        send.println(ResponseBuilder.getResponse(405));
                                    }
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
            else if (player != null)
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
