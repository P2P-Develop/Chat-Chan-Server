package develop.p2p.chatchan.Command;


import com.sun.xml.internal.ws.resources.SenderMessages;
import develop.p2p.chatchan.Player.Player;
import org.slf4j.Logger;

import java.util.ArrayList;

public class CommandCoreBUS
{
    private int length = 0;
    private ArrayList<InterfaceCommand> list = new ArrayList<>();
    private InterfaceCommand defaultCommand;
    public <T extends InterfaceCommand> void listen(T cmdCls)
    {
        list.add(cmdCls);
        length += 1;
    }

    public <T extends InterfaceCommand> void remove (T cmdCls)
    {
        list.remove(cmdCls);
        length -= 1;
    }

    public int getSize()
    {
        return this.length;
    }

    public EnumCommandOutput run(Player sender, String commandName, ArrayList<String> args, Logger logger) throws Exception
    {
        EnumCommandOutput output = null;
        for (InterfaceCommand command: this.list)
        {
            if (command.getName().equals(commandName))
                output = command.execute(sender, commandName, args, logger);
        }

        if (output == null && this.defaultCommand != null)
            output = defaultCommand.execute(sender, commandName, args, logger);
        else
            output = EnumCommandOutput.NOTFOUND;
        return output;
    }

    public <T extends InterfaceCommand> void setDefault(T cmdCls)
    {
        defaultCommand = cmdCls;
    }
}
