package develop.p2p.chatchan.Command.DefaultCommands;

import develop.p2p.chatchan.Enum.*;
import develop.p2p.chatchan.Interface.*;
import develop.p2p.chatchan.*;
import develop.p2p.chatchan.Player.*;
import org.slf4j.*;

import java.util.*;

public class CommandHelp implements CommandBase
{
    ArrayList<String> alias = new ArrayList<>();
    public CommandHelp()
    {
        this.alias.add("?");
    }
    @Override
    public String getName()
    {
        return "help";
    }

    @Override
    public EnumCommandOutput execute(Player sender, String commandName, ArrayList<String> args, Logger logger)
    {
        logger.info("[SYSTEM] ---HELP---\n");
        for (CommandBase command: Main.commandCoreBUS.getCommandList())
            logger.info(String.format("[SYSTEM] # %s -- %s\n", command.getUsage(), command.getHelp()));
        return EnumCommandOutput.OK;
    }

    @Override
    public String getUsage()
    {
        return "help";
    }

    @Override
    public String getHelp()
    {
        return "showing commands help.";
    }

    @Override
    public ArrayList<String> getAlias()
    {
        return this.alias;
    }
}
