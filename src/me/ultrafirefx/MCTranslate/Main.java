package me.ultrafirefx.MCTranslate;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.api.GoogleAPI;
import com.google.api.translate.Language;
import com.google.api.translate.Translate;

public class Main extends JavaPlugin implements Listener {
	// **Please try to add comments where possible!**
	// Access to Minecraft Chat.
	Logger log = getLogger();
	// SQL connection
	private static Connection connection;
	static String ip, port, username, password = null;

	// Register some events
	@Override
	public void onEnable() {
		Bukkit.getPluginManager().registerEvents(this, this);
		getServer().getPluginManager().registerEvents(this, this);
		log.info("MC Translate enabled!");
	}

	// Make sure the connection is closed
	@Override
	public void onDisable() {
		try {
			if (connection != null && !connection.isClosed()) {
				connection.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// Open our SQL connection
	public synchronized static void openConnection() {
		try {// TODO get SQL database
			connection = DriverManager.getConnection("jdbc:mysql://" + ip + ":"
					+ port + "3066/ultrafirefx", username, password);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Close our SQL connection
	public synchronized static void closeConnection() {
		try {// TODO get SQL database
			connection.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Checks the SQL database if it contains a player
	public synchronized static boolean playerDataContainsPlayer(Player player) {
		try {
			PreparedStatement sql = connection
					.prepareStatement("SELECT * FROM `player_data` WHERE player=?;");
			sql.setString(1, player.getName());
			ResultSet resultSet = sql.executeQuery();
			boolean containsPlayer = resultSet.next();
			sql.close();
			resultSet.close();
			return containsPlayer;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	// TODO Command sequence
	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args[]) {

		return false;
	}

	// When someone talks, it tries to translate it
	@EventHandler
	public void onChatEvent(AsyncPlayerChatEvent event) {
		String arg = event.getMessage();
		Player player = event.getPlayer();
		String langFrom = "AUTO";
		String lang = getLang(player);
		String translatedText = null;
		try {
			translatedText = translateText(arg, langFrom, lang);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (translatedText != null) {
				event.setMessage(translatedText);
			}
		}

	}

	// TODO More methods of finding the default language
	// Get the lang of the player
	public String getLang(Player player) {
		String lang = "English";
		openConnection();
		try {
			if (playerDataContainsPlayer(player.getPlayer())) {
				PreparedStatement sql = connection
						.prepareStatement("SELECT lang FROM `player_data` WHERE player=?;");
				sql.setString(1, player.getName());
				ResultSet result = sql.executeQuery();
				result.next();
				lang = result.getString("lang");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeConnection();
		}
		return lang;
	}

	// Translate an array of Strings
	public static String[] translateTextArray(String[] args, String langFrom,
			String lang) throws Exception {
		String[] translatedText = args;
		GoogleAPI
				.setHttpReferrer("http://dev.bukkit.org/bukkit-plugins/mctranslate/");
		GoogleAPI.setKey("");
		int r = 0;
		for (String s : args) {
			String x = Translate.DEFAULT.execute(s,
					Language.fromString(langFrom), Language.fromString(lang));
			translatedText[r] = x;
			r++;
		}
		return translatedText;
	}

	// Translate one String
	public static String translateText(String arg, String langFrom, String lang)
			throws Exception {
		String translatedText = arg;
		GoogleAPI
				.setHttpReferrer("http://dev.bukkit.org/bukkit-plugins/mctranslate/");
		GoogleAPI.setKey("");
		translatedText = Translate.DEFAULT.execute(arg,
				Language.fromString(langFrom), Language.fromString(lang));

		return translatedText;
	}
}
