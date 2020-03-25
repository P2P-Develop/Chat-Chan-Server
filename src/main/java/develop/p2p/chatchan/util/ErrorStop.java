package develop.p2p.chatchan.util;

import develop.p2p.chatchan.Main;

public class ErrorStop
{
    public static void stop(String error)
    {
        System.out.println();
        Main.logger.error(error + "\n");
        Main.logger.error("[FATAL] Stopping server...\n");
        System.exit(1);
    }
}
