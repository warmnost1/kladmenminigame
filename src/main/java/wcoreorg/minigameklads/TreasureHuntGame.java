package wcoreorg.minigameklads;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

public class TreasureHuntGame implements Listener {
    private final JavaPlugin plugin;
    private final Map<Player, Integer> playerScores;
    private final List<Location> chestLocations;
    private final Map<Player, Integer> playerWins;
    private BukkitTask gameTask;
    private BukkitTask titleTask;
    private boolean running;
    private int countdownSeconds;

    public TreasureHuntGame(JavaPlugin plugin) {
        this.plugin = plugin;
        this.playerScores = new HashMap<>();
        this.chestLocations = new ArrayList<>();
        this.playerWins = new HashMap<>();
        this.running = false;
        this.countdownSeconds = 30;
    }

    // Автозапуск игры если на сервере больше 2 игроков
    public void checkAndStartGame() {
        if (Bukkit.getOnlinePlayers().size() >= 2) {
            startCountdown();
        } else {
            sendTitleMessage("Не хватка игроков!", "", 10, 70, 20);
        }
    }

    // Таймер перед стартом игры
    private void startCountdown() {
        countdownSeconds = 30;
        titleTask = new BukkitRunnable() {
            @Override
            public void run() {
                if (countdownSeconds <= 0) {
                    startGame();
                    cancel();
                } else {
                    sendTitleMessage("Игра начинается через " + countdownSeconds + " секунд.", "", 0, 20, 0);
                    countdownSeconds--;
                }
            }
        }.runTaskTimer(plugin, 0, 20);
    }

    private void sendTitleMessage(String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendTitle(title, subtitle, fadeIn, stay, fadeOut);
        }
    }

    public boolean isRunning() {
        return running;
    }

    public void startGame() {
        this.running = true;
        if (titleTask != null) {
            titleTask.cancel();
        }
        Bukkit.getPluginManager().registerEvents(this, plugin);
        disperseChests();
        gameTask = new BukkitRunnable() {
            @Override
            public void run() {
                endGame();
            }
        }.runTaskLater(plugin, 1200L); // 1 минута игры
    }

    public void endGame() {
        this.running = false;
        if (gameTask != null) {
            gameTask.cancel();
        }
        HandlerList.unregisterAll(this);

        Player winner = playerScores.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);

        if (winner != null) {
            Bukkit.broadcastMessage("Победитель: " + winner.getName() + " с " + playerScores.get(winner) + " очков!");
            playerWins.put(winner, playerWins.getOrDefault(winner, 0) + 1);
        }
        chestLocations.forEach(loc -> loc.getBlock().setType(Material.AIR));
        playerScores.clear();
    }

    private void disperseChests() {
        Random random = new Random();
        for (int i = 0; i < 5; i++) {
            int x = random.nextInt(100);
            int y = 64; // уровень поверхности
            int z = random.nextInt(100);
            Location loc = new Location(Bukkit.getWorld("world"), x, y, z);
            loc.getBlock().setType(Material.CHEST);
            Chest chest = (Chest) loc.getBlock().getState();
            chest.getInventory().addItem(new ItemStack(Material.GOLD_INGOT, random.nextInt(10) + 1));
            chestLocations.add(loc);
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getClickedBlock() != null && event.getClickedBlock().getType() == Material.CHEST) {
            Player player = event.getPlayer();
            Chest chest = (Chest) event.getClickedBlock().getState();
            int score = chest.getInventory().getSize();
            playerScores.put(player, playerScores.getOrDefault(player, 0) + score);
            chest.getBlock().setType(Material.AIR);
            event.setCancelled(true);
        }
    }

    public Map<Player, Integer> getPlayerWins() {
        return playerWins;
    }
}