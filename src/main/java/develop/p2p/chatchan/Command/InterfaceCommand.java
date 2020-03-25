package develop.p2p.chatchan.Command;

import develop.p2p.chatchan.Player.Player;

import org.slf4j.Logger;

import java.util.ArrayList;

public interface  InterfaceCommand
{
    String getName();
    EnumCommandOutput execute(Player sender, String commandName, ArrayList<String> args, Logger logger) throws Exception;
}
