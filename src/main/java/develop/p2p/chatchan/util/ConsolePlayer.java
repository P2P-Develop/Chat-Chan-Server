package develop.p2p.chatchan.util;

import develop.p2p.chatchan.Player.*;

public class ConsolePlayer extends Player
{

    public static Player getPlayer()
    {
        Player player = new Player();
        player.ip = "0.0.0.0";
        player.isChatAuthorized = true;
        player.name = "[CONSOLE]";
        player.chatSocket = null;
        player.commandSocket = null;
        player.decryptKey = "";
        player.encryptKey = "";
        player.token = "";
        player.isConsole = true;
        return player;
    }
}
