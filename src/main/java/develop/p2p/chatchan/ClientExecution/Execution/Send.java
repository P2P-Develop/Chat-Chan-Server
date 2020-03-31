package develop.p2p.chatchan.ClientExecution.Execution;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import develop.p2p.chatchan.Init.BlackList;
import develop.p2p.chatchan.Interface.ClientExecutionBase;
import develop.p2p.chatchan.Main;
import develop.p2p.chatchan.Message.EncryptManager;
import develop.p2p.chatchan.Message.MessageSender;
import develop.p2p.chatchan.Message.Response.ResponseBuilder;
import develop.p2p.chatchan.Player.Player;
import develop.p2p.chatchan.Player.PlayerList;
import develop.p2p.chatchan.Server.Thread.CallThread;
import develop.p2p.chatchan.Server.Thread.ChatThread;
import develop.p2p.chatchan.Server.Thread.CommandThread;
import org.slf4j.Logger;

import java.io.PrintWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Send implements ClientExecutionBase
{
    @Override
    public String getName()
    {
        return "send";
    }

    @Override
    public void executeCall(Player sender, String commandName, JsonNode node, Logger logger, Socket sockets, CallThread thread) throws Exception
    {

    }

    @Override
    public void executeChat(Player sender, String commandName, JsonNode node, Logger logger, Socket sockets, ChatThread thread) throws Exception
    {
        PrintWriter send = new PrintWriter(sockets.getOutputStream(), true);
        ObjectMapper mapper = new ObjectMapper();
        if (!sender.isChatAuthorized)
        {
            send.println("{\"code\": 400}");
            return;
        }
        else if(!sender.token.equals(node.get("token").asText()))
        {
            send.println("{\"code\": 400}");
            return;
        }
        PlayerList players = Main.playerList;
        for (Player sendPlayer : players.getPlayers())
        {
            try
            {
                if (!sendPlayer.isChatAuthorized)
                    continue;
                PrintWriter cSend = new PrintWriter(sendPlayer.chatSocket.getOutputStream(), true);
                String encryptedMessage = node.get("message").asText();
                String decryptedMessage = EncryptManager.decrypt(encryptedMessage, sender.encryptKey);
                logger.info("Received message by " + sender.name + "(" + sender.ip + "): " + decryptedMessage + "\n");
                String newEncryptedMessage = EncryptManager.encrypt(decryptedMessage, sendPlayer.decryptKey);
                MessageSender msgSender = new MessageSender();
                msgSender.name = sender.name;
                msgSender.content = newEncryptedMessage;
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                msgSender.date = format.format(calendar);
                cSend.println(mapper.writeValueAsString(msgSender));
            }
            catch (Exception e)
            {
                send.println(ResponseBuilder.getResponse(405));
            }
        }
    }

    @Override
    public void executeCommand(Player sender, String commandName, JsonNode node, Logger logger, Socket sockets, CommandThread thread) throws Exception
    {
        ObjectMapper mapper = new ObjectMapper();
        PrintWriter send = new PrintWriter(sockets.getOutputStream(), true);
        if (!sender.isChatAuthorized)
        {
            send.println("{\"code\": 400}");
            return;
        }
        else if(!sender.token.equals(node.get("token").asText()))
        {
            send.println("{\"code\": 400}");
            return;
        }
        PlayerList players = Main.playerList;
        for (Player sendPlayer : players.getPlayers())
        {
            try
            {
                if (!sendPlayer.isChatAuthorized)
                    continue;
                PrintWriter cSend = new PrintWriter(sendPlayer.chatSocket.getOutputStream(), true);
                String encryptedMessage = node.get("command").asText();
                String decryptedMessage = EncryptManager.decrypt(encryptedMessage, sender.encryptKey);
                logger.info("Received command by " + sender.name + "(" + sender.ip + "): " + decryptedMessage + "\n");
                String newEncryptedMessage = EncryptManager.encrypt(decryptedMessage, sendPlayer.decryptKey);
                MessageSender msgSender = new MessageSender();
                msgSender.name = sender.name;
                msgSender.content = newEncryptedMessage;
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                msgSender.date = format.format(calendar);
                cSend.println(mapper.writeValueAsString(msgSender));
            }
            catch (Exception e)
            {
                send.println(ResponseBuilder.getResponse(405));
            }
        }
    }
}
