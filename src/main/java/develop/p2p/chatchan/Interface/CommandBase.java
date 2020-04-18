package develop.p2p.chatchan.Interface;

import develop.p2p.chatchan.Enum.*;
import develop.p2p.chatchan.Player.*;
import org.slf4j.*;

import java.util.*;

public interface CommandBase
{
    String getName();
    EnumCommandOutput execute(Player sender, String commandName, ArrayList<String> args, Logger logger) throws Exception;
    String getUsage();
    String getHelp();
    ArrayList<String> getAlias();
}
