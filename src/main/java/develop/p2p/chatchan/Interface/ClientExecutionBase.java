package develop.p2p.chatchan.Interface;

import com.fasterxml.jackson.databind.JsonNode;
import develop.p2p.chatchan.Player.Player;
import develop.p2p.chatchan.Server.Thread.CallThread;
import develop.p2p.chatchan.Server.Thread.ChatThread;
import develop.p2p.chatchan.Server.Thread.CommandThread;
import org.slf4j.Logger;

import java.net.Socket;

public interface ClientExecutionBase
{
    String getName();
    void executeCall(Player sender, String commandName, JsonNode node, Logger logger, Socket sockets, CallThread thread) throws Exception;
    void executeChat(Player sender, String commandName, JsonNode node, Logger logger, Socket sockets, ChatThread thread) throws Exception;
    void executeCommand(Player sender, String commandName, JsonNode node, Logger logger, Socket sockets, CommandThread thread) throws Exception;
}
