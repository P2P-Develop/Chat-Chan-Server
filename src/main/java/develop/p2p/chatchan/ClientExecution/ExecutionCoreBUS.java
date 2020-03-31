package develop.p2p.chatchan.ClientExecution;

import com.fasterxml.jackson.databind.JsonNode;
import develop.p2p.chatchan.Enum.EnumServerType;
import develop.p2p.chatchan.Interface.ClientExecutionBase;
import develop.p2p.chatchan.Player.Player;
import org.slf4j.Logger;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
public class ExecutionCoreBUS
{
    private int length = 0;
    private ArrayList<ClientExecutionBase> list = new ArrayList<>();
    public <T extends ClientExecutionBase> void listen(T cmdCls)
    {
        list.add(cmdCls);
        length += 1;
    }

    public <T extends ClientExecutionBase> void remove (T cmdCls)
    {
        list.remove(cmdCls);
        length -= 1;
    }

    public int getSize()
    {
        return length;
    }

    public <T> void execute(Player sender, String commandName, JsonNode node, Logger logger, Socket socket, EnumServerType type) throws Exception
    {
        for (ClientExecutionBase command: list)
        {
            if (command.getName().equals(commandName))
            {
                switch(type)
                {
                    case CALL:
                        command.executeCall(sender, commandName, node, logger, socket, type.getCallThread());
                        return;
                    case CHAT:
                        command.executeChat(sender, commandName, node, logger, socket, type.getChatThread());
                        return;
                    case COMMAND:
                        command.executeCommand(sender, commandName, node, logger, socket, type.getCommandThread());
                        return;
                }
            }
        }
        new PrintWriter(socket.getOutputStream(), true).println("{\"code\": 404}");
    }
}
