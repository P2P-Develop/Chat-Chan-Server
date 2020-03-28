package develop.p2p.chatchan.Command.Commands;

import develop.p2p.chatchan.Command.EnumCommandOutput;
import develop.p2p.chatchan.Command.InterfaceCommand;
import develop.p2p.chatchan.Player.Player;
import org.slf4j.Logger;

import java.util.ArrayList;

public class CommandHelp implements InterfaceCommand
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
        logger.info("[SYSTEM] # help -- showing help.\n");
        logger.info("[SYSTEM] # stop -- stopping all server.\n");
        logger.info("[SYSTEM] # kick <PlayerName> -- player kick from all server.\n");
        return EnumCommandOutput.OK;
    }
}
