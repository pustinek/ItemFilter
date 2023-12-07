package me.pustinek.itemfilter.users;

import lombok.Getter;
import lombok.Setter;
import me.pustinek.itemfilter.ItemFilterPlugin;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.UUID;

@Getter
public class User {

    private final UUID uuid;
    private boolean enabled;
    private final ArrayList<ItemStack> items;
    @Setter
    private boolean dirty = false;

    public User(final UUID uuid, boolean enabled, ArrayList<ItemStack> items) {
        this.uuid = uuid;
        this.enabled = enabled;
        this.items = items;
    }


    public boolean hasItem(ItemStack item) {
        for (ItemStack i : items) {
            // Check if type is in configs

            if (i.isSimilar(item)) {
                return true;
            }

            if(i.getType() == item.getType()){
                return ItemFilterPlugin.getInstance()
                        .getConfig()
                        .getStringList("filter_only_by_material")
                        .stream()
                        .anyMatch(s -> s.equalsIgnoreCase(i.getType().toString()));
            }

        }
        return false;
    }



    public void addItem(ItemStack item) {
        if(hasItem(item)) return;
        ItemStack clone = item.clone();
        clone.setAmount(1);
        items.add(clone);
        setDirty(true);
    }




    public void removeItem(ItemStack itemStack) {
        items.removeIf(item -> item.isSimilar(itemStack));
        // Remove item from arraylist
        setDirty(true);
    }

    public void resetItems() {
        items.clear();
        setDirty(true);
    }

    public void setEnabled(boolean value) {
        this.enabled = value;
        setDirty(true);
    }


    public String itemsToString() {
        //TODO: parse items to string
        return "";
    }
}
