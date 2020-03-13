package develop.p2p.chatchan.Config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class Whitelist
{
    File file = new File("./whitelist.xml");
    Properties settings = new Properties();
    public boolean saveDefaultConfig()
    {
        if (file.exists())
            return true;
        settings.setProperty("wh1", "106.73.0.32");
        settings.setProperty("wh2", "114.514.19.810");
        FileOutputStream out = null;
        try
        {
            out = new FileOutputStream("whitelist.xml");
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

    public String getString(String key, String defaults)
    {
        return settings.getProperty(key, defaults);
    }
}
