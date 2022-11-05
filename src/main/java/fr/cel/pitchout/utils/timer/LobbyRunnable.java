package fr.cel.pitchout.utils.timer;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import fr.cel.pitchout.manager.GameManager;
import fr.cel.pitchout.manager.GameState;
import fr.cel.pitchout.PitchOut;

public class LobbyRunnable extends BukkitRunnable {

    private int timer = 11;

    private PitchOut main;
    private GameManager gameManager;

    public LobbyRunnable(PitchOut main, GameManager gameManager) {
        this.main = main;
        this.gameManager = gameManager;
    }

    @Override
    public void run() {
        
        if(!(gameManager.isGameState(GameState.WAITING))) {
            timer = 11;
            cancel();
            return;
        }

        if(gameManager.getAlive().size() <= 1) {
            Bukkit.broadcastMessage(main.getPrefix() + "§cIl n'y a pas assez de joueurs pour lancer la partie");
            timer = 11;
            cancel();
            return;
        }

        timer--;
        setLevel();

        if(timer <= 0) {
            cancel();
            gameManager.setGameState(GameState.STARTING);
            return;
        }

        if((timer == 10) || (timer <= 5 && timer != 0)) {
            Bukkit.broadcastMessage(main.getPrefix() + "§6Démarrage de la partie dans §e" + timer + getSecond() + "§6.");
            for(Player players : Bukkit.getOnlinePlayers()) {
                players.playSound(players.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 10f, 1f);
            }
        }

    }

    private String getSecond() {
        return timer == 1 ? " seconde" : " secondes";
    }

    private void setLevel() {
        for(Player players : Bukkit.getOnlinePlayers()) {
            players.setLevel(timer);
        }
    }
    
}
