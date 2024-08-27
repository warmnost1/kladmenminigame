package wcoreorg.minigameklads;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class GuiMenu {

    public static ItemStack createGuiItem(String name, int wins) {
        ItemStack item = new ItemStack(Material.PAPER, 1);
        ItemMeta meta = item.getItemMeta();

        assert meta != null;
        meta.setDisplayName(name);
        meta.setLore(Arrays.asList("Победы: " + wins));
        item.setItemMeta(meta);

        return item;
    }
}

