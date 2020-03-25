package develop.p2p.chatchan.Response;

public class Kicked
{
    public int code;
    public String reason;
    public Kicked(int code, String reason)
    {
        this.code = code;
        this.reason = reason;
    }
}
