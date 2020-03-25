package develop.p2p.chatchan.util;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class JsonObj
{
    public static boolean isJson(String json)
    {
        ObjectMapper mapper = new ObjectMapper();
        try
        {
            mapper.readTree(json);
            return true;
        }
        catch (IOException e)
        {
            return false;
        }
    }
}
