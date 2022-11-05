package fr.cel.pitchout.manager;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import fr.cel.pitchout.PitchOut;
import fr.cel.pitchout.utils.ItemBuilder;

public class PlayerManager {

    private PitchOut main;
    private GameManager gameManager;

    public PlayerManager(PitchOut main, GameManager gameManager) {
        this.main = main;
        this.gameManager = gameManager;
    }

    public void gmAdventurePlayers() {
        for(Player players : Bukkit.getOnlinePlayers()) {
            players.setGameMode(GameMode.ADVENTURE);
        }
    }

    public void clearPlayers() {
        Bukkit.getOnlinePlayers().stream().filter(player -> player.getGameMode() == GameMode.ADVENTURE).forEach(this::clear);
    }

    private void clear(Player player) {
        player.getInventory().clear();
    }

    public void teleportPlayers() {
        for(int i = 0; i < gameManager.getAlive().size(); i++) {

            Location spawn = gameManager.getSpawns().get(i);

            UUID uuidPlayer = gameManager.getAlive().get(i);
            Player player = Bukkit.getPlayer(uuidPlayer);
            
            player.teleport(spawn);
            
        }
    }

    public void healthPlayers() {
        Bukkit.getOnlinePlayers().stream().filter(player -> player.getGameMode() == GameMode.ADVENTURE).forEach(this::health);
    }

    private void health(Player player) {
        player.setMaxHealth(6.0);
        player.setHealth(6.0);
    }

    public void resetHealthPlayers() {
        Bukkit.getOnlinePlayers().stream().filter(player -> player.getGameMode() == GameMode.ADVENTURE || player.getGameMode() == GameMode.SPECTATOR).forEach(this::resetHealth);
    }

    private void resetHealth(Player player) {
        player.setMaxHealth(20.0);
        player.setHealth(20.0);
    }



    public void giveItemsPlayers() {
        Bukkit.getOnlinePlayers().stream().filter(player -> player.getGameMode() == GameMode.ADVENTURE).forEach(this::giveItems);
    }

    private void giveItems(Player player) {
        ItemStack stick = new ItemBuilder(Material.STICK).addEnchant(Enchantment.KNOCKBACK, 4)
                .setDisplayName("PitchNette 2.0").toItemStack();

        ItemStack bow = new ItemBuilder(Material.BOW).setUnbreakable().addEnchant(Enchantment.ARROW_KNOCKBACK, 4).addEnchant(Enchantment.ARROW_INFINITE, 1)
                .setDisplayName("Ejector 2.0").toItemStack();
        
        player.getInventory().setItemInOffHand(bow);
        player.getInventory().setItem(0, stick);
        player.getInventory().setItem(17, new ItemStack(Material.ARROW));
        player.updateInventory();
    }

    public void resetLevelPlayers() {
        Bukkit.getOnlinePlayers().stream().filter(player -> player.getGameMode() == GameMode.ADVENTURE).forEach(this::setLevel);
    }

    private void setLevel(Player player) {
        player.setLevel(0);
    }

    public void setLivesLevelPlayers() {
        Bukkit.getOnlinePlayers().stream().filter(player -> player.getGameMode() == GameMode.ADVENTURE).forEach(this::setLevelLives);
    }

    private void setLevelLives(Player player) {
        player.setLevel(3);
    }

    public void eliminate(Player player) {
        gameManager.getAlive().remove(player.getUniqueId());

        player.setGameMode(GameMode.SPECTATOR);
        player.getInventory().clear();
        player.sendMessage(main.getPrefix() + "§cVous avez perdu !");
        player.sendTitle("§cVous avez perdu !", "§aVous êtes maintenant spectateur.", 10, 70, 20);

        gameManager.checkWin();
    }
     
}
