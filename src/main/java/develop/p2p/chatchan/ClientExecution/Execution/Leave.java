package develop.p2p.chatchan.ClientExecution.Execution;

import com.fasterxml.jackson.databind.*;
import develop.p2p.chatchan.Interface.*;
import develop.p2p.chatchan.*;
import develop.p2p.chatchan.Player.*;
import develop.p2p.chatchan.Server.Thread.*;
import org.slf4j.*;

import java.io.*;
import java.net.*;

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
