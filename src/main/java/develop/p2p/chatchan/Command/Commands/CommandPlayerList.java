package develop.p2p.chatchan.Command.Commands;

import develop.p2p.chatchan.Enum.EnumCommandOutput;
import develop.p2p.chatchan.Interface.CommandBase;
import develop.p2p.chatchan.Main;
import develop.p2p.chatchan.Player.Player;
import org.slf4j.Logger;

import java.util.ArrayList;

public class CommandPlayerList implements CommandBase
{
    @Override
    public String getName()
    {
        return "list";
    }

    @Override
    public EnumCommandOutput execute(Player sender, String commandName, ArrayList<String> args, Logger logger)
    {
        StringBuilder response = new StringBuilder();
        response.append("[SYSTEM]");
        int len = 0;
        for (Player player : Main.playerList.getPlayers())
        {
            String name = player.name;
            String ip = player.ip;
            response.append(String.format(" %s(%s)", name, ip));
            len += 1;
        }
        if (response.toString().equals("[SYSTEM]"))
            response = new StringBuilder().append("[SYSTEM] N/A\n");
        else
            response.append("\n");
        logger.info("[SYSTEM] ---PlayerList---\n");
        logger.info(response.toString());
        logger.info("[SYSTEM] " + len + "/2\n");
        return EnumCommandOutput.OK;
    }

    @Override
    public String getUsage()
    {
        return "list";
    }

    @Override
    public String getHelp()
    {
        return "show all list of player.";
    }
}
