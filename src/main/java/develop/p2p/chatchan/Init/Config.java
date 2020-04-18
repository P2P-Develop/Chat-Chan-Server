package develop.p2p.chatchan.Init;

import develop.p2p.chatchan.*;
import develop.p2p.chatchan.util.*;

import java.io.*;

public class Config
{
    ConfigUtil configUtil;
    public Config()
    {
        configUtil = new ConfigUtil("config.xml");
    }
    public void parseConfig() throws IOException
    {
        if (!this.configUtil.isExists())
            this.configUtil.saveDefaultConfig();
        if (!configUtil.isExists())
        {
            configUtil.setProperty("callPort", "41410");
            configUtil.setProperty("commandPort", "46573");
            configUtil.setProperty("chatPort", "37564");
            configUtil.setProperty("keyLength", "24");
        }
        if (!configUtil.saveDefaultConfig())
            ErrorStop.stop("Failed to Loading Config files.");

        Main.callPort = Integer.parseInt(configUtil.getString("callPort", "41410"));
        Main.commandPort = Integer.parseInt(configUtil.getString("commandPort", "45673"));
        Main.chatPort = Integer.parseInt(configUtil.getString("chatPort", "37564"));
        Main.keyLength = Integer.parseInt(configUtil.getString("keyLength", "256"));
    }

    public void loadConfig()
    {
        configUtil = new ConfigUtil("config.xml");
    }

    public boolean saveDefaultConfig() throws IOException
    {
        return this.configUtil.saveDefaultConfig();
    }

}
