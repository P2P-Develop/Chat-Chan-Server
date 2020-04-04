package develop.p2p.chatchan.Server;

import develop.p2p.chatchan.Interface.ServerThreadBase;
import develop.p2p.chatchan.Main;
import develop.p2p.chatchan.Server.Thread.ChatThread;

import java.io.IOException;
import java.net.*;

public class CommandServer implements ServerThreadBase
{
    @Override
    public void start(int port)
    {
        try (ServerSocket listener = new ServerSocket())
        {
            listener.setReuseAddress(true);
            listener.bind(new InetSocketAddress(port));
            while (true)
            {
                ChatThread thread  = new ChatThread(listener.accept());
                thread.start();
            }
        }
        catch (IOException e)
        {
            Main.logger.error("[COMMAND] Error: ");
            e.printStackTrace();
        }
    }
}
