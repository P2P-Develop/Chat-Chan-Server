package develop.p2p.chatchan.Command.Commands;

import com.sun.deploy.trace.LoggerTraceListener;
import develop.p2p.chatchan.Enum.EnumCommandOutput;
import develop.p2p.chatchan.Interface.CommandBase;
import develop.p2p.chatchan.Main;
import develop.p2p.chatchan.Player.Player;
import org.slf4j.Logger;

import java.util.ArrayList;

public class CommandBAN implements CommandBase
{
    @Override
    public String getName()
    {
        return "ban";
    }

    @Override
    public EnumCommandOutput execute(Player sender, String commandName, ArrayList<String> args, Logger logger) throws Exception
    {
        String ip;
        if (args.size() != 1)
        {
            logger.error("[BAN] Invalid arguments.\n");
            return EnumCommandOutput.NOTFOUND;
        }
        else if (Main.blackList.isBlackListed(args.get(0)))
        {
            logger.error("[BAN] Player " + args.get(0) + " is been banned.\n");
            return EnumCommandOutput.OK;
        }
        ip = args.get(0);
        if (Main.blackList.add(ip))
            logger.info("[BAN] Player" + ip + " has been banned!\n");
        else
            logger.error("[BAN] Unknown error.\n");
        return EnumCommandOutput.OK;
    }

    @Override
    public String getUsage()
    {
        return "ban <ip>";
    }

    @Override
    public String getHelp()
    {
        return "add ban players.";
    }

    @Override
    public ArrayList<String> getAlias()
    {
        return null;
    }
}
