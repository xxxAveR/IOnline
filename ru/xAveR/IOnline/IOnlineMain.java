package ru.xAveR.IOnline;


import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;


public class IOnlineMain extends JavaPlugin implements Listener {
	
    private ScoreboardManager sbM; 
    private Scoreboard s; 
    private Team online;
    private Team notBother;
    private Team offline ;
    private Objective IOnline;
    
	@SuppressWarnings("deprecation")
	@Override
	public void onEnable(){
		getServer().getPluginManager().registerEvents(this, this);
		sbM = Bukkit.getScoreboardManager();
		s =  sbM.getNewScoreboard();
		online = s.registerNewTeam("online");
		notBother = s.registerNewTeam("notBother");
		offline = s.registerNewTeam("offline");
		IOnline = s.registerNewObjective("IOnline", "dummy");
		
		online.setSuffix(ChatColor.GREEN + " [Онлайн]");
		notBother.setSuffix(ChatColor.GOLD + " [Занят]");
		offline.setSuffix(ChatColor.RED + " [Оффлайн]");
		IOnline.setDisplayName("Администрация");
		IOnline.setDisplaySlot(DisplaySlot.SIDEBAR);
				
		for(Player p : Bukkit.getOnlinePlayers()){
			if(p.hasPermission("ionline.use")){
				Score s = IOnline.getScore(p);
				s.setScore(1);
				online.addPlayer(p);
			}
		}
	}
	@Override
	public void onDisable(){
		
	}
	@SuppressWarnings("deprecation")
	@EventHandler
	public void PlayerJoin(PlayerJoinEvent e){
		Player player = e.getPlayer();
		if(player.hasPermission("ionline.use")){
			online.addPlayer(player);
			Score s = IOnline.getScore(player);
			s.setScore(1);
			scoreUpdate(); 
		}
		scoreUpdate(player);
	}
	@SuppressWarnings("deprecation")
	@EventHandler
	public void PlayerQuit(PlayerQuitEvent e){
		Player player = e.getPlayer();
		if(player.hasPermission("ionline.use")){
			online.removePlayer(player);
			offline.addPlayer(player);
			Score s = IOnline.getScore(player);
			s.setScore(1);
			scoreUpdate();
		}
		scoreUpdate(player);
	}
	@SuppressWarnings("deprecation")
	@EventHandler
	public void PlayerKick(PlayerKickEvent e){
		Player player = e.getPlayer();
		if(player.hasPermission("ionline.use")){
			online.removePlayer(player);
			offline.addPlayer(player);
			Score s = IOnline.getScore(player);
			s.setScore(1);
			scoreUpdate();
		}
		scoreUpdate(player);
	}
    public void scoreUpdate() {
        for (Player p : Bukkit.getOnlinePlayers()) { 
            p.setScoreboard(sbM.getNewScoreboard()); 
            p.setScoreboard(s); 
        }
    }
    public void scoreUpdate(Player p) {
        p.setScoreboard(sbM.getNewScoreboard()); 
        p.setScoreboard(s); 
    }
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (sender instanceof Player) {
            if (commandLabel.equalsIgnoreCase("ibusy")) {
                if (sender.hasPermission("ionline.ibusy")) {
                    Player p = (Player) sender; 
                    if (online.getPlayers().contains(p)) { 
                        online.removePlayer(p); 
                        notBother.addPlayer(p); 
                        scoreUpdate();
                        } else {
                        online.removePlayer(p); 
                        online.addPlayer(p); 
                        scoreUpdate();
                        }
                }
            }
            if (commandLabel.equalsIgnoreCase("iinvisible")){
            	if (sender.hasPermission("ionline.iinvisible")){
            		Player p = (Player) sender;
            		if (online.getPlayers().contains(p)){
            			online.removePlayer(p);
            			offline.addPlayer(p);
            			scoreUpdate();
            			p.setPlayerListName("");
            		} else {
            			offline.removePlayer(p);
            			online.addPlayer(p);
            			scoreUpdate();
            			p.setPlayerListName(p.getName());
            		}
            	}
            }
        }
		return false;
    }
}

