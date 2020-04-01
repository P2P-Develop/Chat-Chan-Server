package develop.p2p.chatchan.Command.Commands;

import develop.p2p.chatchan.Enum.EnumCommandOutput;
import develop.p2p.chatchan.Interface.CommandBase;
import develop.p2p.chatchan.Main;
import develop.p2p.chatchan.Player.Player;
import org.slf4j.Logger;

import java.util.ArrayList;

public class CommandHelp implements CommandBase
{
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
}
