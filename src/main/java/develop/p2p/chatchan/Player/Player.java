package develop.p2p.chatchan.Player;

import java.net.Socket;

public class Player
{
    public String name;
    public String ip;
    public Socket callSocket;
    public Socket chatSocket;
    public Socket commandSocket;
    public String token = "";
    public String encryptKey = "";
    public String decryptKey = "";
    public boolean isChatAuthorized = false;

}
