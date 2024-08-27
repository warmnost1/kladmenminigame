package wcoreorg.minigameklads;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GameCommand implements CommandExecutor {
    private final TreasureHuntGame treasureHuntGame;

    public GameCommand(TreasureHuntGame treasureHuntGame) {
        this.treasureHuntGame = treasureHuntGame;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (!player.isOp()) {
                player.sendMessage("У вас нет прав на выполнение этой команды.");
                return true;
            }
            if (label.equalsIgnoreCase("startgame")) {
                if (treasureHuntGame.isRunning()) {
                    player.sendMessage("Игра уже идёт!");
                } else {
                    treasureHuntGame.startGame();
                    player.sendMessage("Игра началась!");
                }
            }
        } else {
            sender.sendMessage("Только игроки могут выполнять эту команду.");
        }
        return true;
    }
}