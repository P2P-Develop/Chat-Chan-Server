package develop.p2p.chatchan;

import develop.p2p.chatchan.Command.*;
import develop.p2p.chatchan.Command.DefaultCommands.*;
import develop.p2p.chatchan.Enum.*;
import develop.p2p.chatchan.Init.*;
import develop.p2p.chatchan.Player.*;
import develop.p2p.chatchan.Server.*;
import develop.p2p.chatchan.util.*;
import org.slf4j.*;

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
    public static Thread commandServerThread;
    public static CommandCoreBUS commandCoreBUS;
    public static BlackList blackList;
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
            final CommandServer commandServer = new CommandServer();

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
            blackList = new BlackList();
            blackLst = blackList.getBlackList();
            System.out.println("OK");

            logger.info("[SYSTEM] Loading Command Lists...");
            commandCoreBUS = new CommandCoreBUS();
            commandCoreBUS.listen(new CommandStop());
            commandCoreBUS.listen(new CommandHelp());
            commandCoreBUS.listen(new CommandPlayerList());
            commandCoreBUS.listen(new CommandKick());
            commandCoreBUS.listen(new CommandBAN());
            commandCoreBUS.setDefault(new CommandHelp());
            System.out.println("OK");

            logger.info("[SYSTEM] Definition call server...");
            callServerThread = new Thread()
            {
                @Override
                public void run()
                {
                    callServer.start(callPort);
                }
            };
            System.out.println("OK");

            logger.info("[SYSTEM] Definition chat server...");
            chatServerThread = new Thread()
            {
                @Override
                public void run()
                {
                    chatServer.start(chatPort);
                }
            };
            System.out.println("OK");

            logger.info("[SYSTEM] Definition command server...");
            commandServerThread = new Thread()
            {
                @Override
                public void run()
                {
                    commandServer.start(commandPort);
                }
            };
            System.out.println("OK");

            logger.info("[SYSTEM] Starting call server...");
            callServerThread.start();
            System.out.println("OK");

            logger.info("[SYSTEM] Starting chat server...");
            chatServerThread.start();
            System.out.println("OK");

            logger.info("[SYSTEM] Starting command server...");
            commandServerThread.start();
            System.out.println("OK");

            logger.info("[SYSTEM] See \"help\" command for showing help.\n");
            logger.info("[SYSTEM] Ready\n");

            Scanner scanner = new Scanner(System.in);

            while (true)
            {
                String[] cmdArgs = scanner.nextLine().split(" ");
                ArrayList<String> args = new ArrayList<>(Arrays.asList(cmdArgs));
                args.remove(0);
                EnumCommandOutput output = commandCoreBUS.run(ConsolePlayer.getPlayer(), cmdArgs[0], args, logger);

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
