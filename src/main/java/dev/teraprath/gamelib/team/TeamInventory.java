package dev.teraprath.gamelib.team;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

public class TeamInventory {

    private final Map<Integer, ItemStack> inventory;

    public TeamInventory() {
        this.inventory = new HashMap<>();
    }

    public void addItem(@Nonnull ItemStack itemStack) {
        inventory.put(inventory.size(), itemStack);
    }

    public void addItem(@Nonnegative int slot, @Nonnull ItemStack itemStack) {
        inventory.put(slot, itemStack);
    }

    public void removeItem(@Nonnegative int slot) {
        inventory.remove(slot);
    }

    public void removeItem(@NotNull ItemStack itemStack) {
        this.inventory.forEach((integer, item) -> {
            if (item.equals(itemStack)) {
                this.inventory.remove(integer);
            }
        });
    }

    public Map<Integer, ItemStack> getContents() {
        return this.inventory;
    }

}
