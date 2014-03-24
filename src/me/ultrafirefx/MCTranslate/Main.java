package me.ultrafirefx.MCTranslate;

import java.util.logging.Logger;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.api.GoogleAPI;
import com.google.api.translate.Language;
import com.google.api.translate.Translate;

public class Main extends JavaPlugin {
	// **Please try to add comments where possible!**
	// Access to Minecraft Chat.
	Logger log = getLogger();

	// TODO
	@Override
	public void onEnable() {
		log.info("MC Translate enabled!");
	}

	// TODO
	@Override
	public void onDisable() {

	}

	// TODO config.yml, plugin,yml
	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args[]) {

		return false;
	}

	public static String[] main(String[] args, String langFrom, String langTo)
			throws Exception {
		String[] translatedText = args;
		GoogleAPI
				.setHttpReferrer("http://dev.bukkit.org/bukkit-plugins/mctranslate/");
		GoogleAPI.setKey("");
		int r = 0;
		for (String s : args) {
			String x = Translate.DEFAULT.execute(s,
					Language.fromString(langFrom), Language.fromString(langTo));
			translatedText[r] = x;
			r++;
		}
		return translatedText;
	}

}
