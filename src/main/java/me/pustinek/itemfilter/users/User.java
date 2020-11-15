package me.pustinek.itemfilter.users;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
public class User {

    private final UUID uuid;
    private boolean enabled;
    private Set<Material> materials;
    @Setter
    private boolean dirty = false;

    public User(final UUID uuid, boolean enabled, Set<Material> materials) {
        this.uuid = uuid;
        this.enabled = enabled;
        this.materials = materials;
    }


    public void toggleMaterial(Material material) {
        if (materials.contains(material)) {
            removeMaterial(material);
        } else {
            addMaterial(material);
        }
    }

    public void addMaterial(Material material) {
        materials.add(material);
        setDirty(true);
    }

    public void removeMaterial(Material material) {
        materials.remove(material);
        setDirty(true);
    }

    public void resetMaterials() {
        materials = new HashSet<>();
        setDirty(true);
    }

    public void setEnabled(boolean value) {
        this.enabled = value;
        setDirty(true);
    }


    public String materialsToString() {
        StringBuilder builder = new StringBuilder();
        for (Material material : materials) {
            builder.append(material.name()).append(",");
        }
        return builder.toString();
    }

}
