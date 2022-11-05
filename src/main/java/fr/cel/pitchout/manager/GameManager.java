package fr.cel.pitchout.manager;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import fr.cel.pitchout.PitchOut;
import fr.cel.pitchout.utils.timer.LobbyRunnable;

public class GameManager {

    private PitchOut main;
    private GameState gameState = GameState.WAITING;

    private final PlayerManager playerManager;

    private LobbyRunnable lobbyRunnable;

    private List<UUID> alive = new ArrayList<>();
    
    private List<Location> spawns = new ArrayList<>();

    public GameManager(PitchOut main) {
        this.main = main;
        this.playerManager = new PlayerManager(this.main, this);
    }

    public void setGameState(GameState gameState) {

        this.gameState = gameState;

        switch (gameState) {

            case PRELOBBY:
            Bukkit.broadcastMessage(main.getPrefix() + "Pré-attente !");
            break;

            case WAITING:
            for(Player pls : Bukkit.getOnlinePlayers()) {
                getAlive().add(pls.getUniqueId());
            }
            this.lobbyRunnable = new LobbyRunnable(main, this);
            this.lobbyRunnable.runTaskTimer(main, 0, 20);

            Bukkit.broadcastMessage(main.getPrefix() + "En Attente !");
            
            getPlayerManager().gmAdventurePlayers();
            getPlayerManager().resetLevelPlayers();
            getPlayerManager().clearPlayers();
            getPlayerManager().healthPlayers();

            World world = Bukkit.getWorld("world");
            final String game = "pitchout.game.";

            spawns.add(new Location(world, main.getConfig().getDouble(game + "spawn1.x"), main.getConfig().getDouble(game + "spawn1.y"), main.getConfig().getDouble(game + "spawn1.z"), (float) main.getConfig().getDouble(game + "spawn1.yaw"), (float) main.getConfig().getDouble(game + "spawn1.pitch")));
            spawns.add(new Location(world, main.getConfig().getDouble(game + "spawn2.x"), main.getConfig().getDouble(game + "spawn2.y"), main.getConfig().getDouble(game + "spawn2.z"), (float) main.getConfig().getDouble(game + "spawn2.yaw"), (float) main.getConfig().getDouble(game + "spawn2.pitch")));
            spawns.add(new Location(world, main.getConfig().getDouble(game + "spawn3.x"), main.getConfig().getDouble(game + "spawn3.y"), main.getConfig().getDouble(game + "spawn3.z"), (float) main.getConfig().getDouble(game + "spawn3.yaw"), (float) main.getConfig().getDouble(game + "spawn3.pitch")));
            spawns.add(new Location(world, main.getConfig().getDouble(game + "spawn4.x"), main.getConfig().getDouble(game + "spawn4.y"), main.getConfig().getDouble(game + "spawn4.z"), (float) main.getConfig().getDouble(game + "spawn4.yaw"), (float) main.getConfig().getDouble(game + "spawn4.pitch")));
            break;

            case STARTING:
            if (this.lobbyRunnable != null) this.lobbyRunnable.cancel();

            Bukkit.broadcastMessage(main.getPrefix() + "Démarrage de la partie !");

            getPlayerManager().teleportPlayers();
            getPlayerManager().giveItemsPlayers();
            getPlayerManager().setLivesLevelPlayers();
            this.setGameState(GameState.GAME);
            break;

            case GAME:
            Bukkit.broadcastMessage(main.getPrefix() + "Et c'est parti !");
            break;
        
        }

    }

    public boolean isGameState(GameState gameState) {
        return this.gameState == gameState;
    }

    public GameState getGameState() {
        return this.gameState;
    }

    public PlayerManager getPlayerManager() {
        return playerManager;
    }

    public List<UUID> getAlive() {
        return alive;
    }

    public List<Location> getSpawns() {
        return spawns;
    }

    public void checkWin() {
        if(this.isGameState(GameState.GAME) && getAlive().size() == 1) {
            UUID uuidWinner = getAlive().get(0);
            Player winner = Bukkit.getPlayer(uuidWinner);

            Bukkit.broadcastMessage(main.getPrefix() + winner.getName() + " a gagné la partie !");
            winner.sendMessage(main.getPrefix() + "Vous avez gagné !");
            winner.sendTitle("Vous avez gagné !", "Bien joué !", 10, 70, 20);
            winner.getInventory().clear();

            for (Player players : Bukkit.getOnlinePlayers()) {
                final String spawnLoc = "pitchout.spawn.";

                Location spawn = new Location(Bukkit.getWorld("world"),
                        main.getConfig().getDouble(spawnLoc + "x"),
                        main.getConfig().getDouble(spawnLoc + "y"),
                        main.getConfig().getDouble(spawnLoc + "z"),
                        (float) main.getConfig().getDouble(spawnLoc + "yaw"),
                        (float) main.getConfig().getDouble(spawnLoc + "pitch"));
                players.teleport(spawn);

                getPlayerManager().resetHealthPlayers();
                getPlayerManager().resetLevelPlayers();
                getAlive().add(players.getUniqueId());
                players.setGameMode(GameMode.ADVENTURE);

            }

            setGameState(GameState.PRELOBBY);
        }
    }
    
}