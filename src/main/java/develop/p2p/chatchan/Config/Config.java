package develop.p2p.chatchan.Config;

import java.io.*;
import java.util.Properties;

public class Config
{
    File file = new File("./settings.xml");
    Properties settings = new Properties();
    public boolean saveDefaultConfig()
    {
        if (file.exists())
            return true;
        settings.setProperty("ip", "0.0.0.0");
        settings.setProperty("callPort", "41410");
        settings.setProperty("commandPort", "46573");
        settings.setProperty("chatPort", "37564");
        settings.setProperty("keyLength", "24");
        FileOutputStream out = null;
        try
        {
            out = new FileOutputStream("settings.xml");
            settings.storeToXML(out, "edit option");
        }
        catch (Exception e)
        {
            return false;
        }
        finally
        {
            if (out != null)
            {
                try
                {
                    out.close();
                }
                catch (Exception ignored) {}
                return true;
            }
        }
        return false;

    }

    public String getString(String key, String defaults) throws IOException
    {
        settings.loadFromXML(new FileInputStream(file));
        return settings.getProperty(key, defaults);
    }
}
