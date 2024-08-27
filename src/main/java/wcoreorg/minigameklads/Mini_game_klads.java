package wcoreorg.minigameklads;

import org.bukkit.plugin.java.JavaPlugin;

public class Mini_game_klads extends JavaPlugin {
    private TreasureHuntGame treasureHuntGame;

    @Override
    public void onEnable() {
        this.treasureHuntGame = new TreasureHuntGame(this);
        getCommand("startgame").setExecutor(new GameCommand(treasureHuntGame));
        getCommand("tops").setExecutor(new TopPlayersCommand(this, treasureHuntGame));
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(treasureHuntGame), this);
        getServer().getPluginManager().registerEvents(new InventoryListener(), this);
    }


    @Override
    public void onDisable() {
        if (treasureHuntGame.isRunning()) {
            treasureHuntGame.endGame();
        }
    }
}
