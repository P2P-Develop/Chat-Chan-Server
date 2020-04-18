package develop.p2p.chatchan.Server.Thread;

import com.fasterxml.jackson.databind.*;
import develop.p2p.chatchan.ClientExecution.Execution.*;
import develop.p2p.chatchan.ClientExecution.*;
import develop.p2p.chatchan.Enum.*;
import develop.p2p.chatchan.*;
import develop.p2p.chatchan.Player.*;
import develop.p2p.chatchan.util.*;

import java.io.*;
import java.net.*;

public class CallThread extends Thread
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
            ExecutionCoreBUS coreBUS = new ExecutionCoreBUS();
            coreBUS.listen(new Leave());
            coreBUS.listen(new Join());

            EnumServerType type = EnumServerType.CALL;
            type.setEnumServerType(this);
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
                    if (JsonObj.isJson(line))
                    {
                        JsonNode node = mapper.readTree(line);
                        coreBUS.execute(this.player, node.get("exec").asText(), node, Main.logger, socket, type);
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
                    catch (Exception e)
                    {

                        Main.logger.error("[CALL] Error:");
                        e.printStackTrace();
                    }
                }
                return;
            }
            Main.logger.info("[CALL] Client Disconnected: " + player.name + "(" + player.ip.replace("/", "") + ")\n");
            try
            {
                Main.playerList.leave(player);
                if (socket != null)
                    socket.close();
            }
            catch (Exception e)
            {

                Main.logger.error("[CALL] Error:");
                e.printStackTrace();
            }
        }
    }
}
