package develop.p2p.chatchan.Interface;

import develop.p2p.chatchan.Enum.EnumCommandOutput;
import develop.p2p.chatchan.Player.Player;

import org.slf4j.Logger;

import java.util.ArrayList;

public interface CommandBase
{
    String getName();
    EnumCommandOutput execute(Player sender, String commandName, ArrayList<String> args, Logger logger) throws Exception;
    String getUsage();
    String getHelp();
}
