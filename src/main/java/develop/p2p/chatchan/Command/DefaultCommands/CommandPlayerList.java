package develop.p2p.chatchan.Command.DefaultCommands;

import develop.p2p.chatchan.Enum.*;
import develop.p2p.chatchan.Interface.*;
import develop.p2p.chatchan.*;
import develop.p2p.chatchan.Player.*;
import org.slf4j.*;

import java.util.*;

public class CommandPlayerList implements CommandBase
{
    ArrayList<String> alias = new ArrayList<>();
    public CommandPlayerList()
    {
        this.alias.add("ls");
        this.alias.add("list");
        this.alias.add("lst");
    }

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

    @Override
    public ArrayList<String> getAlias()
    {
        return this.alias;
    }
}
