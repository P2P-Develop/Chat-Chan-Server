package develop.p2p.chatchan.Command.Commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import develop.p2p.chatchan.Enum.EnumCommandOutput;
import develop.p2p.chatchan.Interface.CommandBase;
import develop.p2p.chatchan.Main;
import develop.p2p.chatchan.Player.Player;
import develop.p2p.chatchan.Response.Kicked;
import org.slf4j.Logger;

import java.net.Socket;
import java.util.ArrayList;

public class CommandKick implements CommandBase
{
    @Override
    public String getName()
    {
        return "kick";
    }

    @Override
    public EnumCommandOutput execute(Player sender, String commandName, ArrayList<String> args, Logger logger) throws Exception
    {
        String kickedMessage = "Kicked from LOCAL_ADMIN.";
        if (args.size() == 3)
            kickedMessage = args.get(2);
        else if (args.size() != 2)
        {
            logger.info("[SYSTEM] Unknown Args. See help command for showing help.\n");
            return EnumCommandOutput.FAILED;
        }

        Player player = Main.playerList.getPlayerFromName(args.get(1));
        if (player == null)
        {
            logger.error("[SYSTEM] Player not found.\n");
            return EnumCommandOutput.FAILED;
        }

        Socket chatSocket = player.chatSocket;
        Socket commandSocket = player.commandSocket;
        Socket callSocket = player.callSocket;
        boolean chatFlag = false;
        boolean commandFlag = false;
        boolean callFlag = false;

        if (chatSocket != null)
        {
            Kicked kicked = new Kicked(500, kickedMessage);
            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValueAsString(kicked);
            chatSocket.close();
            chatFlag = true;
        }

        if (commandSocket != null)
        {
            commandSocket.close();
            commandFlag = true;
        }

        if (callSocket != null)
        {
            callSocket.close();
            callFlag = true;
        }

        logger.info(String.format("[SYSTEM] Player(%s) kicked from %s%s%s\n", args.get(1), chatFlag ? "ChatServer ": "", commandFlag ? "CommandServer ": "", callFlag ? "CallServer ": ""));
        return EnumCommandOutput.OK;
    }

    @Override
    public String getUsage()
    {
        return "kick <PlayerName>";
    }

    @Override
    public String getHelp()
    {
        return "kick player from all servers.";
    }

    @Override
    public ArrayList<String> getAlias()
    {
        return null;
    }
}
