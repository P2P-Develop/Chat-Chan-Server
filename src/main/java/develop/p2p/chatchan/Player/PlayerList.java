package develop.p2p.chatchan.Player;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import develop.p2p.chatchan.Main;
import develop.p2p.chatchan.Player.Join.JoinParser;
import develop.p2p.chatchan.Player.Join.NotCallJoinParser;
import develop.p2p.chatchan.Player.Leave.LeaveParser;

import java.awt.*;
import java.util.ArrayList;
import java.util.UUID;

public class PlayerList
{
    private ObjectMapper mapper = new ObjectMapper();
    private ArrayList<Player> player = new ArrayList<>();
    public String join(Player player) throws JsonProcessingException
    {
        int[] port = new int[2];
        if (this.player.size() <= 2)
        {
            port[0] = Main.chatPort;
            port[1] = Main.commandPort;
            for (Player players : this.player)
            {
                if (players.name.equals(player.name))
                {
                    port[0] = 0;
                    port[1] = 0;
                    return mapper.writeValueAsString(new JoinParser(402, port, this.player.size(), "", "", ""));
                }
            }
            UUID id = UUID.randomUUID();
            player.token = id.toString();
            this.player.add(player);
            return mapper.writeValueAsString(new JoinParser(200, port, this.player.size(), id.toString(), player.encryptKey, player.decryptKey));
        }
        else
        {
            return mapper.writeValueAsString(new JoinParser(401, port, this.player.size(), "", "", ""));
        }
    }

    public String join(Player player, String token) throws JsonProcessingException
    {
        int code = 403;
        if (player.token.equals(token))
            code = 200;
        return mapper.writeValueAsString(new NotCallJoinParser(code, this.player.size()));
    }
    public String leave(Player player) throws JsonProcessingException
    {
        this.player.remove(player);
        return mapper.writeValueAsString(new LeaveParser(200));
    }

    public int size()
    {
        return player.size();
    }

    public ArrayList<Player> getPlayers()
    {
        return player;
    }

    public Player getPlayerFromName(String name)
    {
        for (Player player: this.player)
        {
            if (player.name != null)
            {
                if (player.name.equals(name))
                    return player;
            }
        }
        return null;
    }

    public void put(Player oldPlayer, Player newPlayer)
    {
        ArrayList<Player> temPlayer = new ArrayList<>();
        for (Player player: this.player)
        {
            if (oldPlayer.equals(player))
            {
                temPlayer.add(newPlayer);
            }
            else
            {
                temPlayer.add(player);
            }
        }
        this.player = temPlayer;
    }

    public void put(String oldPlayerName, Player newPlayer)
    {
        ArrayList<Player> temPlayer = new ArrayList<>();
        for (Player player: this.player)
        {
            if (this.getPlayerFromName(oldPlayerName).equals(player))
            {
                temPlayer.add(newPlayer);
            }
            else
            {
                temPlayer.add(player);
            }
        }
        this.player = temPlayer;
    }
}
