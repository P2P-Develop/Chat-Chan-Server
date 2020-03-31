package develop.p2p.chatchan.ClientExecution.Execution;

import com.fasterxml.jackson.databind.JsonNode;
import develop.p2p.chatchan.Interface.ClientExecutionBase;
import develop.p2p.chatchan.Main;
import develop.p2p.chatchan.Player.Player;
import develop.p2p.chatchan.Server.Thread.CallThread;
import develop.p2p.chatchan.Server.Thread.ChatThread;
import develop.p2p.chatchan.Server.Thread.CommandThread;
import org.slf4j.Logger;

import java.io.PrintWriter;
import java.net.Socket;

public class Leave implements ClientExecutionBase
{
    @Override
    public String getName()
    {
        return "leave";
    }

    @Override
    public void executeCall(Player sender, String commandName, JsonNode node, Logger logger, Socket sockets, CallThread thread) throws Exception
    {
        if (sender != null)
        {
            String response =  Main.playerList.leave(sender);
            new PrintWriter(sockets.getOutputStream(), true).println(response);
            sockets.close();
        }
    }

    @Override
    public void executeChat(Player sender, String commandName, JsonNode node, Logger logger, Socket sockets, ChatThread thread) throws Exception
    {
        new PrintWriter(sockets.getOutputStream(), true).println("{\"code\": 200}");
        sockets.close();
    }

    @Override
    public void executeCommand(Player sender, String commandName, JsonNode node, Logger logger, Socket sockets, CommandThread thread) throws Exception
    {
        new PrintWriter(sockets.getOutputStream(), true).println("{\"code\": 200}");
        sockets.close();
    }
}
