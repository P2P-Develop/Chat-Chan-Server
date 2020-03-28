package develop.p2p.chatchan.Init;

import develop.p2p.chatchan.Interface.ConfigInterface;
import develop.p2p.chatchan.Main;
import develop.p2p.chatchan.util.ErrorStop;
import org.omg.CORBA.CODESET_INCOMPATIBLE;

import java.io.IOException;

public class Config
{
    ConfigInterface configInterface;
    public Config()
    {
        configInterface = new ConfigInterface("config.xml");
    }
    public void parseConfig() throws IOException
    {
        if (!this.configInterface.isExists())
            this.configInterface.saveDefaultConfig();
        if (!configInterface.isExists())
        {
            configInterface.setProperty("callPort", "41410");
            configInterface.setProperty("commandPort", "46573");
            configInterface.setProperty("chatPort", "37564");
            configInterface.setProperty("keyLength", "24");
        }
        if (!configInterface.saveDefaultConfig())
            ErrorStop.stop("Failed to Loading Config files.");

        Main.callPort = Integer.parseInt(configInterface.getString("callPort", "41410"));
        Main.commandPort = Integer.parseInt(configInterface.getString("commandPort", "45673"));
        Main.chatPort = Integer.parseInt(configInterface.getString("chatPort", "37564"));
        Main.keyLength = Integer.parseInt(configInterface.getString("keyLength", "256"));
    }

    public void loadConfig()
    {
        configInterface = new ConfigInterface("config.xml");
    }

    public boolean saveDefaultConfig() throws IOException
    {
        return this.configInterface.saveDefaultConfig();
    }

}
