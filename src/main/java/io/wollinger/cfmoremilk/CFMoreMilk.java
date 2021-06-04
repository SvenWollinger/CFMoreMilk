package io.wollinger.cfmoremilk;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public class CFMoreMilk extends JavaPlugin implements Listener {

    private CFMoreMilk instance;
    public static String KEY = "CFMilkOrigin";

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        getServer().getPluginManager().registerEvents(this, this);
        LogManager.log("CFMoreMilk started! Have fun!", Level.INFO);
    }

    @Override
    public void onDisable() {

    }

    public boolean isEntityEnabled(String entity) {
        return getConfig().getBoolean("enabled_entities." + entity);
    }

    public void giveDefaultMilk(Player player, Entity entity, EquipmentSlot equipmentSlot) {
        String entityName = Utils.capitalize(entity.getName());
        if(entity instanceof Player)
            entityName = ((Player) entity).getDisplayName();

        if(!(entity instanceof Cow) && !(entity instanceof MushroomCow)) {
            player.playSound(player.getLocation(), Sound.ENTITY_COW_MILK, 1f, 1f);
        }

        ItemStack milk = new ItemStack(Material.MILK_BUCKET);
        ItemMeta milkMeta = milk.getItemMeta();
        milkMeta.setDisplayName("\u00A7f" + getConfig().getString("item_name").replaceAll("%name%", entityName));
        NamespacedKey key = new NamespacedKey(instance, CFMoreMilk.KEY);
        milkMeta.getPersistentDataContainer().set(key, PersistentDataType.STRING, entity.getType().toString().toLowerCase());
        milk.setItemMeta(milkMeta);

        player.getInventory().setItem(equipmentSlot, milk);
    }

    @EventHandler
    public void onPlayerInteractEntityEvent(PlayerInteractEntityEvent event) {
        if(event.getHand().equals(EquipmentSlot.HAND)) {
            if(event.getPlayer().getInventory().getItem(event.getHand()).getType() != Material.BUCKET)
                return;

            event.setCancelled(true);

            if(getConfig().getBoolean("enable_all")) {
                giveDefaultMilk(event.getPlayer(), event.getRightClicked(), event.getHand());
            } else {
                String name = event.getRightClicked().getType().toString().toLowerCase();
                if(isEntityEnabled(name))
                    giveDefaultMilk(event.getPlayer(), event.getRightClicked(), event.getHand());
            }
        }
    }

    @EventHandler
    public void onMilkDrink(PlayerItemConsumeEvent event) {
        if(event.getItem().getType() == Material.MILK_BUCKET) {
            PersistentDataContainer dataContainer = event.getItem().getItemMeta().getPersistentDataContainer();
            NamespacedKey key = new NamespacedKey(instance, CFMoreMilk.KEY);
            if(dataContainer.has(key, PersistentDataType.STRING)) {
                String mob = dataContainer.get(key, PersistentDataType.STRING);
                dataContainer.remove(key);

                
            }
        }
    }


}
