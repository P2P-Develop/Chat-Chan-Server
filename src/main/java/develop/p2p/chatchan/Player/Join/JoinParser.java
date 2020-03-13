package develop.p2p.chatchan.Player.Join;


public class JoinParser
{
    public int code;
    public int[] port;
    public int people;
    public String token;
    public String encryptKey;
    public String decryptKey;
    public JoinParser(int code,int[] port, int people, String token, String encryptKey, String decryptKey)
    {
        this.code = code;
        this.port = port;
        this.people = people;
        this.token = token;
        this.encryptKey = encryptKey;
        this.decryptKey = decryptKey;

    }
}
