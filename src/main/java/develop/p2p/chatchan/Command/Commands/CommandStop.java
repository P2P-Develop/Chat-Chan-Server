package develop.p2p.chatchan.Command.Commands;

import develop.p2p.chatchan.Enum.EnumCommandOutput;
import develop.p2p.chatchan.Interface.CommandBase;
import develop.p2p.chatchan.Main;
import develop.p2p.chatchan.Player.Player;
import org.slf4j.Logger;

import java.util.ArrayList;

public class CommandStop implements CommandBase
{
    public String getName()
    {
        return "stop";
    }

    public EnumCommandOutput execute(Player sender, String commandName, ArrayList<String> args, Logger logger)
    {
        try
        {
            logger.info("[CALL] Stopping server...");
            Main.callServerThread.stop();
            System.out.println("OK");
            logger.info("[CHAT] Stopping server...");
            Main.chatServerThread.stop();
            System.out.println("OK");
        }
        catch (Exception e)
        {
            logger.info("[ERROR] Stacktrace:");
            e.printStackTrace();
            logger.error("[FATAL] Stopping system...\n");
            System.exit(0);
        }
        logger.info("[FATAL] Stopping system...\n");
        System.exit(0);
        return null;
    }

    @Override
    public String getUsage()
    {
        return "stop";
    }

    @Override
    public String getHelp()
    {
        return "Stopping all servers.";
    }
}
