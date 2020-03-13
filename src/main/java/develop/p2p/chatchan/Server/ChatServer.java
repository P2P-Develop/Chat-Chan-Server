package develop.p2p.chatchan.Server;

import develop.p2p.chatchan.Main;
import develop.p2p.chatchan.Server.Thread.ChatThread;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class ChatServer
{
    public void chat(int port)
    {
        try (ServerSocket listener = new ServerSocket())
        {
            listener.setReuseAddress(true);
            InetSocketAddress address = new InetSocketAddress(port);
            listener.bind(address);
            while (true)
            {
                Socket socket = listener.accept();
                ChatThread thread  = new ChatThread(socket);
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
