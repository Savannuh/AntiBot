package me.savannuh.antibot;

import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin; 

public class Main extends JavaPlugin implements Listener {

	private char[] chars = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'm', 'n', 'p', 'q', 'r', 's', 't',
			'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'J', 'K', 'M', 'N', 'P', 'Q', 'R',
			'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '1', '2', '3', '4', '5', '6', '7', '8', '9' };
	private Random random = new Random();
	private HashMap<UUID, Captcha> verifyMap = new HashMap<>();

	public void onEnable() {
		Bukkit.getPluginManager().registerEvents(this, this);
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		if (!player.hasPlayedBefore() || verifyMap.containsKey(player.getUniqueId())) {
			String word = "";
			for (int i = 0; i < 8; i++) {
				word += chars[random.nextInt(chars.length)];
			}
			verifyMap.put(player.getUniqueId(), new Captcha(word));
			player.sendMessage(
					"§8§m-------------------- §6§lCarrotnetwork Verificatie §8§m--------------------\n§eVerifieer dat je een echte speler bent!\n§eTyp het volgende over: &8"
							+ word + "\n§8§m-------------------- §6§lCarrotnetwork Verificatie §8§m--------------------");
			Bukkit.getScheduler().runTaskLater(this, () -> { if (verifyMap.containsKey(player.getUniqueId())) { player.kickPlayer("§4§lVerificatie niet voltooid"); } }, 2500);
		}
	}

	@EventHandler
	public void onAsyncPlayerChat(AsyncPlayerChatEvent event) {
		Player player = event.getPlayer();
		if (verifyMap.containsKey(player.getUniqueId())) {
			Captcha captcha = verifyMap.get(player.getUniqueId());
			if (event.getMessage().equals(captcha.getWord())) {
				verifyMap.remove(player.getUniqueId());
				player.sendMessage("§aVerficatie voltooid!");
			} else {
				if (captcha.getAttempts() == 1) {
					Bukkit.getScheduler().runTask(this, () -> player.kickPlayer("§4§lVerificatie niet voltooid"));
				} else {
					captcha.minusAttempt();
					player.sendMessage("§4Probeer opnieuw: §1" + captcha.getWord());
				}
			}
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
		if (verifyMap.containsKey(event.getPlayer().getUniqueId())) event.setCancelled(true);
	}
	
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
		if (verifyMap.containsKey(event.getPlayer().getUniqueId())) event.setCancelled(true);
	}

}
