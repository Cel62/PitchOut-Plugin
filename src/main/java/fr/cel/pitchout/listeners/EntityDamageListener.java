package fr.cel.pitchout.listeners;

import java.util.Random;
import java.util.UUID;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import fr.cel.pitchout.PitchOut;
import fr.cel.pitchout.manager.GameManager;
import fr.cel.pitchout.manager.GameState;

public class EntityDamageListener implements Listener {

    private PitchOut main;
    private GameManager gameManager;

    public EntityDamageListener(PitchOut main, GameManager gameManager) {
        this.main = main;
        this.gameManager = gameManager;
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {

        final Entity victim = event.getEntity();

        if (victim instanceof Player) {

            final Player player = (Player) victim;
            final UUID playerUUID = player.getUniqueId();

            if (gameManager.isGameState(GameState.PRELOBBY) || gameManager.isGameState(GameState.WAITING) || gameManager.isGameState(GameState.STARTING)) {
                event.setCancelled(true);
                return;
            }

            if ((event.getCause() == DamageCause.FIRE || event.getCause() == DamageCause.FIRE_TICK || event.getCause() == DamageCause.FALL || event.getCause() == DamageCause.LAVA) && gameManager.isGameState(GameState.GAME)) {
                player.setFireTicks(0);
                event.setCancelled(true);
            }

            if (gameManager.getAlive().contains(playerUUID) && (player.getGameMode() == GameMode.ADVENTURE) && gameManager.isGameState(GameState.GAME)) {

                if ((player.getLocation().getBlock().getType() == Material.LAVA)) {
                    if (player.getMaxHealth() == 2) {
                        gameManager.getPlayerManager().eliminate(player);
                    } else {
                        player.setFireTicks(0);

                        int randomSpawn = new Random().nextInt(gameManager.getSpawns().size());
                        Location respawn = gameManager.getSpawns().get(randomSpawn);

                        player.teleport(respawn);
                        player.sendMessage(main.getPrefix() + "Vous avez perdu une vie ! Il vous reste " + (((int) player.getMaxHealth() / 2) - 1) + " vies.");
                        player.setLevel((int) (player.getMaxHealth() / 2) - 1);
                        player.setMaxHealth(player.getMaxHealth() - 2);
                    }
                }

            }

        }

    }

    @EventHandler
    public void arrowDamage(EntityDamageByEntityEvent event) {
        final Entity victim = event.getEntity();
        if(victim instanceof Player) {
            final Player player = (Player) victim;
            if(gameManager.getAlive().contains(player.getUniqueId()) && gameManager.isGameState(GameState.GAME)) {
                event.setDamage(0.0);
            }
        }
    }
    
}
