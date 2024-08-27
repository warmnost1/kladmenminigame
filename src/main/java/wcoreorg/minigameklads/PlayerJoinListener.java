package wcoreorg.minigameklads;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerJoinListener implements Listener {
    private final TreasureHuntGame treasureHuntGame;

    public PlayerJoinListener(TreasureHuntGame treasureHuntGame) {
        this.treasureHuntGame = treasureHuntGame;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        treasureHuntGame.checkAndStartGame();
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        treasureHuntGame.checkAndStartGame();
    }
}
