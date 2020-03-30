package develop.p2p.chatchan.Command;


public enum EnumCommandOutput
{
    OK,
    NOTFOUND,
    FAILED;
    private String message = "";
    public void setMessage(String message)
    {
        this.message = message;
    }

    public String getMessage()
    {
        return this.message;
    }
}
