package develop.p2p.chatchan.ClientExecution.Execution;

import com.fasterxml.jackson.databind.*;
import develop.p2p.chatchan.Init.BlackList;
import develop.p2p.chatchan.Interface.ClientExecutionBase;
import develop.p2p.chatchan.Main;
import develop.p2p.chatchan.Message.EncryptManager;
import develop.p2p.chatchan.Player.Player;
import develop.p2p.chatchan.Server.Thread.*;
import org.slf4j.Logger;

import java.io.*;
import java.net.Socket;

public class Join implements ClientExecutionBase
{
    @Override
    public String getName()
    {
        return "join";
    }

    @Override
    public void executeCall(Player sender, String commandName, JsonNode node, Logger logger, Socket sockets, CallThread thread) throws Exception
    {
        PrintWriter send = new PrintWriter(sockets.getOutputStream(), true);
        if (BlackList.isBlackListed(sockets.getRemoteSocketAddress().toString()))
        {
            send.println("{\"code\": 400}");
            sockets.close();
            return;
        }
        if (node.get("name").isNull())
        {
            String response = Main.playerList.join(sender, "");
            send.println(response);
            sockets.close();
            return;
        }
        sender.name = node.get("name").asText();
        String encryptKey;
        String decryptKey;
        Main.logger.info("[ECGM] Generating Encrypt key...");
        encryptKey = EncryptManager.generateEncryptKey();
        System.out.println("OK");
        Main.logger.info("[ECGM] Generating Decrypt key...");
        decryptKey = EncryptManager.generateDecryptKey();
        System.out.println("OK");
        sender.encryptKey = encryptKey;
        sender.decryptKey = decryptKey;
        String message = Main.playerList.join(sender);
        ObjectMapper mappers = new ObjectMapper();
        JsonNode nodes = mappers.readTree(message);
        int code = nodes.get("code").asInt();
        if (code == 200)
            send.println(message);
        else
            sockets.close();
    }

    @Override
    public void executeChat(Player sender, String commandName, JsonNode node, Logger logger, Socket sockets, ChatThread thread) throws Exception
    {
        joinChatOrCommand(true, sender, commandName, node, logger, sockets, null, thread);
    }

    @Override
    public void executeCommand(Player sender, String commandName, JsonNode node, Logger logger, Socket sockets, CommandThread thread) throws Exception
    {
        joinChatOrCommand(false, sender, commandName, node, logger, sockets, thread, null);
    }

    private void joinChatOrCommand(boolean isChat, Player sender, String commandName, JsonNode node, Logger logger, Socket sockets, CommandThread commandThread, ChatThread chatThread) throws IOException
    {
        PrintWriter send = new PrintWriter(sockets.getOutputStream(), true);
        ObjectMapper mapper = new ObjectMapper();
        if (node.get("token").isNull() || node.get("name").isNull())
        {
            String response = Main.playerList.join(sender, "");
            send.println(response);
            sockets.close();
            return;
        }
        String token = node.get("token").asText();
        String name = node.get("name").asText();
        Player player = Main.playerList.getPlayerFromName(name);
        if (player == null)
            return;
        String response = Main.playerList.join(player, token);
        JsonNode nodes = mapper.readTree(response);
        if (nodes.get("code").asInt() != 200)
        {
            send.println(response);
            return;
        }
        send.println(response);
        player.chatSocket = sockets;
        if (isChat)
        {
            player.isChatAuthorized = true;
            chatThread.setPlayer(player);
        }
        else
        {
            player.isCommandAuthorized = true;
            commandThread.setPlayer(player);
        }

        Main.playerList.put(name, player);
    }
}
