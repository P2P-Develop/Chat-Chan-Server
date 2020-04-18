package develop.p2p.chatchan.Message.Response;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;

public class ResponseBuilder
{
    public static String getResponse(int code) throws JsonProcessingException
    {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(new Response(code));
    }
}
