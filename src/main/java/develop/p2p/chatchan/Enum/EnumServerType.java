package develop.p2p.chatchan.Enum;

import develop.p2p.chatchan.Server.Thread.*;

public enum EnumServerType
{
    CHAT(),
    CALL,
    COMMAND;

    private CallThread callThread;
    private ChatThread chatThread;
    private CommandThread commandThread;
    public void setEnumServerType(CallThread thread)
    {
        this.callThread = thread;
    }

    public void setEnumServerType(ChatThread thread)
    {
        this.chatThread = thread;
    }

    public void setEnumServerType(CommandThread thread)
    {
        this.commandThread = thread;
    }

    public CallThread getCallThread()
    {
        return callThread;
    }

    public ChatThread getChatThread()
    {
        return chatThread;
    }

    public CommandThread getCommandThread()
    {
        return commandThread;
    }

    EnumServerType()
    {

    }
}
