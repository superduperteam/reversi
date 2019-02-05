package game.webLogic;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class OnlinePlayersManager {

    private Set<OnlinePlayer> onlinePlayers = new HashSet<>();

    public void addPlayer(String playerName) {
        onlinePlayers.add(new OnlinePlayer(playerName));
    }

    public void removePlayer(String playerName) {
        for(OnlinePlayer onlinePlayer : onlinePlayers) {
            if(onlinePlayer.getName().equals(playerName)) {
                onlinePlayers.remove(onlinePlayer);
                break;
            }
        }
    }

    public boolean isPlayerExists(String playerName) {
        for(OnlinePlayer onlinePlayer : onlinePlayers) {
            if(onlinePlayer.getName().equals(playerName)) {
                return true;
            }
        }

        return false;
    }

    public Set<OnlinePlayer> getOnlinePlayers() {
        return Collections.unmodifiableSet(onlinePlayers);
    }
}
