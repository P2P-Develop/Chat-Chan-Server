package develop.p2p.chatchan.Server;

import develop.p2p.chatchan.Interface.*;
import develop.p2p.chatchan.*;
import develop.p2p.chatchan.Server.Thread.*;

import java.io.*;
import java.net.*;

public class ChatServer implements ServerThreadBase
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
            Main.logger.error("[CHAT] Error: ");
            e.printStackTrace();
        }
    }

}
