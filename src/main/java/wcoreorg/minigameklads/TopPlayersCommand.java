package wcoreorg.minigameklads;


import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class TopPlayersCommand implements CommandExecutor {

    private final JavaPlugin plugin;
    private final TreasureHuntGame treasureHuntGame;

    public TopPlayersCommand(JavaPlugin plugin, TreasureHuntGame treasureHuntGame) {
        this.plugin = plugin;
        this.treasureHuntGame = treasureHuntGame;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            Inventory gui = Bukkit.createInventory(null, 9, "Top Players");

// Получаем список игроков отсортированный по количеству побед
            Map<Player, Integer> sortedWins = treasureHuntGame.getPlayerWins().entrySet()
                    .stream()
                    .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            Map.Entry::getValue,
                            (e1, e2) -> e1,
                            LinkedHashMap::new
                    ));

// Заполняем GUI предметами, представляющими лучших игроков
            int i = 0;
            for (Map.Entry<Player, Integer> entry : sortedWins.entrySet()) {
                if (i >= 9) break;
                gui.setItem(i, GuiMenu.createGuiItem(entry.getKey().getName(), entry.getValue()));
                i++;
            }

            player.openInventory(gui);
        } else {
            sender.sendMessage("Только игроки могут выполнять эту команду.");
        }
        return true;
    }
}