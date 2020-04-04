package develop.p2p.chatchan.Server.Thread;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import develop.p2p.chatchan.ClientExecution.Execution.*;
import develop.p2p.chatchan.ClientExecution.ExecutionCoreBUS;
import develop.p2p.chatchan.Enum.EnumServerType;
import develop.p2p.chatchan.Main;
import develop.p2p.chatchan.Player.Player;
import develop.p2p.chatchan.util.JsonObj;
import org.slf4j.Logger;

import java.io.*;
import java.net.Socket;

public class ChatThread extends Thread
{
    private Player player;
    private Socket socket;
    private Logger logger;
    public ChatThread(Socket socket)
    {
        this.socket = socket;
        this.logger = Main.logger;
    }

    public void setPlayer(Player player)
    {
        this.player = player;
    }

    public void setSocket(Socket socket)
    {
        this.socket = socket;
    }

    public Player getPlayer()
    {
        return player;
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
            ExecutionCoreBUS coreBUS = new ExecutionCoreBUS();
            coreBUS.listen(new Join());
            coreBUS.listen(new Leave());
            coreBUS.listen(new Send());
            EnumServerType type = EnumServerType.CHAT;
            type.setEnumServerType(this);
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

                    coreBUS.execute(this.player, node.get("exec").asText(), node, Main.logger, socket, type);
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
                if (socket != null)
                    socket.close();
            }
            catch (IOException e)
            {
                logger.error("[CHAT] Error:");
                e.printStackTrace();
            }
        }
    }
}
