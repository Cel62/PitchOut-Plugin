package fr.cel.pitchout.listeners;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;

import fr.cel.pitchout.PitchOut;
import fr.cel.pitchout.manager.GameManager;

public class ListenersManager {

    private PitchOut main;
    private PluginManager pm;
    private GameManager gameManager;

    public ListenersManager(PitchOut main, GameManager gameManager) {
        this.main = main;
        this.pm = Bukkit.getPluginManager();
        this.gameManager = gameManager;
    }

    public void registerListeners() {
        pm.registerEvents(new PlayerListener(main, gameManager), this.main);
        pm.registerEvents(new EntityDamageListener(this.main, gameManager), this.main);
    }
    
}
