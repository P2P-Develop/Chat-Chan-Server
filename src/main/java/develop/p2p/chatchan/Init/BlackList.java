package develop.p2p.chatchan.Init;

import develop.p2p.chatchan.Main;
import develop.p2p.chatchan.util.ErrorStop;
import develop.p2p.chatchan.util.ListTextUtil;

import java.io.IOException;
import java.util.ArrayList;

public class BlackList
{
    public static ArrayList<String> getBlackList() throws IOException
    {
        ListTextUtil util = new ListTextUtil("blacklist.lst");
        if (!util.saveDefaultList())
        {
            System.out.println("FAILED");
            ErrorStop.stop("Failed save Config files.");
        }
        return util.getList();
    }

    public static boolean isBlackListed(String ip)
    {
        ArrayList<String> list = Main.blackLst;
        for (String ips: list)
        {
            if (ips.equals(ip))
                return true;
        }
        return false;
    }

}
