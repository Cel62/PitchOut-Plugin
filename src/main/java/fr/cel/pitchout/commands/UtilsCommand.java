package fr.cel.pitchout.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import fr.cel.pitchout.manager.GameManager;

public class UtilsCommand implements CommandExecutor {

    private GameManager gameManager;

    public UtilsCommand(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        sender.sendMessage("Le GameState est " + gameManager.getGameState());
        return false;
    }
    
}
