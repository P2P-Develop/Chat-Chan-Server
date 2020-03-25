package develop.p2p.chatchan.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Buffer
{
    public static OutputStream copy(InputStream inputStream,  OutputStream outputStream) throws IOException
    {
        byte[] buffer = new byte[1024];
        int size = -1;
        while ((size = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, size);
        }
        return outputStream;
    }
}
