package fr.cel.pitchout.listeners;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import fr.cel.pitchout.PitchOut;
import fr.cel.pitchout.manager.GameManager;
import fr.cel.pitchout.manager.GameState;

public class PlayerListener implements Listener {

    private PitchOut main;
    private GameManager gameManager;

    public PlayerListener(PitchOut main, GameManager gameManager) {
        this.main = main;
        this.gameManager = gameManager;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        final UUID uuidPlayer = player.getUniqueId();

        if(gameManager.isGameState(GameState.STARTING) || gameManager.isGameState(GameState.GAME)) {
            player.setGameMode(GameMode.SPECTATOR);
            player.sendMessage(main.getPrefix() + "Le jeu a déjà démarré !");
            event.setJoinMessage(null);
        } else {
            gameManager.getAlive().add(uuidPlayer);
            event.setJoinMessage(
                main.getPrefix() + "§e" + player.getName() + " §7a rejoint le serveur §a(" +
                Bukkit.getOnlinePlayers().size() + "§a/" + Bukkit.getMaxPlayers() + ")");
        }

    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        final Player player = event.getPlayer();
        
        if(gameManager.isGameState(GameState.STARTING) || gameManager.isGameState(GameState.GAME)) {
            event.setQuitMessage(main.getPrefix() + "§c" + player.getName() + "§6 a quitté la partie !");
            gameManager.checkWin();
        } else {
            event.setQuitMessage(main.getPrefix() + "§c" + player.getName() + "§6 a quitté le serveur !");
        }

        final UUID uuidPlayer = player.getUniqueId();
        if(gameManager.getAlive().contains(uuidPlayer)) gameManager.getAlive().remove(uuidPlayer);

    }

    @EventHandler
    public void onFoodChange(FoodLevelChangeEvent event) { event.setFoodLevel(20); }

}
