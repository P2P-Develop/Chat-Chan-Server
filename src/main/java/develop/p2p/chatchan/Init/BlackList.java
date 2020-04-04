package develop.p2p.chatchan.Init;

import develop.p2p.chatchan.Main;
import develop.p2p.chatchan.util.*;

import java.io.IOException;
import java.util.ArrayList;

public class BlackList
{
    private ListTextUtil util;
    public ArrayList<String> getBlackList() throws IOException
    {
        util = new ListTextUtil("blacklist.lst");
        if (!util.saveDefaultList())
        {
            System.out.println("FAILED");
            ErrorStop.stop("Failed save Config files.");
        }
        return util.getList();
    }

    public boolean isBlackListed(String ip)
    {
        for (String ips: util.getList())
        {
            if (ip.contains(ips))
                return true;
        }
        return false;
    }

    public boolean add (String ip) throws IOException
    {
        util.add(ip);
        return util.save();
    }

}
