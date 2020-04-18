package develop.p2p.chatchan.Interface;

import com.fasterxml.jackson.databind.*;
import develop.p2p.chatchan.Player.*;
import develop.p2p.chatchan.Server.Thread.*;
import org.slf4j.*;

import java.net.*;

public interface ClientExecutionBase
{
    String getName();
    void executeCall(Player sender, String commandName, JsonNode node, Logger logger, Socket sockets, CallThread thread) throws Exception;
    void executeChat(Player sender, String commandName, JsonNode node, Logger logger, Socket sockets, ChatThread thread) throws Exception;
    void executeCommand(Player sender, String commandName, JsonNode node, Logger logger, Socket sockets, CommandThread thread) throws Exception;
}
