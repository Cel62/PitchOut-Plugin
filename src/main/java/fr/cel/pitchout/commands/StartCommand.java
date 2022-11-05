package fr.cel.pitchout.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import fr.cel.pitchout.manager.GameManager;
import fr.cel.pitchout.manager.GameState;

public class StartCommand implements CommandExecutor {

    private GameManager gameManager;

    public StartCommand(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        gameManager.setGameState(GameState.WAITING);
        return false;
    }
    
}
