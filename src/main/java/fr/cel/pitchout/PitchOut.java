package fr.cel.pitchout;

import org.bukkit.plugin.java.JavaPlugin;

import fr.cel.pitchout.commands.StartCommand;
import fr.cel.pitchout.commands.UtilsCommand;
import fr.cel.pitchout.listeners.ListenersManager;
import fr.cel.pitchout.manager.GameManager;
import fr.cel.pitchout.manager.GameState;

public class PitchOut extends JavaPlugin {

    private GameManager gameManager;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        getLogger().info("[PitchOut] Plugin active");

        this.gameManager = new GameManager(this);
        gameManager.setGameState(GameState.PRELOBBY);
        
        getCommand("start").setExecutor(new StartCommand(gameManager));
        getCommand("getstate").setExecutor(new UtilsCommand(gameManager));

        new ListenersManager(this, gameManager).registerListeners();
    }

    @Override
    public void onDisable() {
        getLogger().info("[PitchOut] Plugin desactive");
    }

    public String getPrefix() {
        return "§2[PitchOut] §a-§f ";
    }

}