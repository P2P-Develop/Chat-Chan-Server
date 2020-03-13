package develop.p2p.chatchan.Message.Response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ResponseBuilder
{
    public static String getResponse(int code) throws JsonProcessingException
    {
        return new ObjectMapper().writeValueAsString(new Response(code));
    }
}
