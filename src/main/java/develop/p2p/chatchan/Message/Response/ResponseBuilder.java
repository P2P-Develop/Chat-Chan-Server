package develop.p2p.chatchan.Message.Response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ResponseBuilder
{
    public static String getResponse(int code) throws JsonProcessingException
    {
        Response response = new Response(code);
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(response);
    }
}
