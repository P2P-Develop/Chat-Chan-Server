package develop.p2p.chatchan;

import develop.p2p.chatchan.Command.Comands.CommandHelp;
import develop.p2p.chatchan.Command.Comands.CommandKick;
import develop.p2p.chatchan.Command.Comands.CommandPlayerList;
import develop.p2p.chatchan.Command.Comands.CommandStop;
import develop.p2p.chatchan.Command.CommandCoreBUS;
import develop.p2p.chatchan.Command.EnumCommandOutput;
import develop.p2p.chatchan.Init.Config;
import develop.p2p.chatchan.Init.BlackList;
import develop.p2p.chatchan.Player.Player;
import develop.p2p.chatchan.Player.PlayerList;
import develop.p2p.chatchan.Server.CallServer;
import develop.p2p.chatchan.Server.ChatServer;
import develop.p2p.chatchan.util.ConsolePlayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class Main
{
    public static Logger logger;
    public static int chatPort = 37564;
    public static int commandPort = 46573;
    public static int callPort = 41410;
    public static int keyLength = 24;
    public static Config config;
    public static ArrayList<String> blackLst;
    public static PlayerList playerList;
    public static Thread callServerThread;
    public static Thread chatServerThread;
    public static void main(String[] arg)
    {
        try
        {
            System.out.println("Chat-Chan Personal Server");
            System.out.println("Create by P2PDevelop");
            System.out.println("This application are managed by MIT License.\n\n");
            System.out.print("[SYSTEM] Loading library...");
            playerList = new PlayerList();
            final CallServer callServer = new CallServer();
            final ChatServer chatServer = new ChatServer();
            blackLst = new ArrayList<>();
            config = new Config();
            logger = LoggerFactory.getLogger("Main");
            System.out.println("OK");
            logger.info("[SYSTEM] Loading ConfigFiles...");
            config.loadConfig();
            config.saveDefaultConfig();
            System.out.println("OK");
            logger.info("[SYSTEM] Parsing ConfigFiles...");
            config.parseConfig();
            System.out.println("OK");
            logger.info("[SYSTEM] Loading BlackListFiles...");
            blackLst = BlackList.getBlackList();
            System.out.println("OK");
            logger.info("[SYSTEM] Loading Command Lists...");
            CommandCoreBUS core_BUS = new CommandCoreBUS();
            core_BUS.listen(new CommandStop());
            core_BUS.listen(new CommandHelp());
            core_BUS.listen(new CommandPlayerList());
            core_BUS.listen(new CommandKick());
            logger.info("[SYSTEM] Definition call server...");
            callServerThread = new Thread()
            {
                @Override
                public void run()
                {
                    callServer.call(callPort);
                }
            };
            System.out.println("OK");
            logger.info("[SYSTEM] Definition chat server...");
            chatServerThread = new Thread()
            {
                @Override
                public void run()
                {
                    chatServer.chat(chatPort);
                }
            };
            System.out.println("OK");
            logger.info("[SYSTEM] Starting call server...");
            callServerThread.start();
            System.out.println("OK");
            logger.info("[SYSTEM] Starting chat server...");
            chatServerThread.start();
            System.out.println("OK");
            logger.info("[SYSTEM] See \"help\" command for showing help.\n");
            logger.info("[SYSTEM] Ready\n");
            Scanner scanner = new Scanner(System.in);
            while (true)
            {
                String[] cmdArgs = scanner.nextLine().split(" ");
                String name = cmdArgs[0];
                ArrayList<String> args = new ArrayList<>(Arrays.asList(cmdArgs));
                args.remove(0);
                Player player = ConsolePlayer.getPlayer();
                EnumCommandOutput output = core_BUS.run(player, name, args, logger);
                switch (output)
                {
                    case FAILED:
                    case NOTFOUND:
                        logger.info("[SYSTEM] Unknown command(args). See \"help\" command for showing help.\n");
                        break;
                    case OK:
                        logger.info("[SYNTAX] OK.\n");
                        break;
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            logger.info("[ALL] Stopping server...");
            logger.info("[FATAL] Stopping system...\n");

            System.exit(1);
        }
    }

}
