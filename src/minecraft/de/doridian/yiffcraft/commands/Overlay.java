package de.doridian.yiffcraft.commands;

import de.doridian.yiffcraft.Yiffcraft;

public class Overlay extends BaseCommand {
	public void run(String[] args) throws Exception {
		Yiffcraft.guiMode++;
		if(Yiffcraft.guiMode >= 4)
			Yiffcraft.guiMode = 1;
		Yiffcraft.saveConfig();
	}
	public String getHelp() throws Exception {
		return "Toggles YiffCraft interfaces.";
	}
	public String getUsage() throws Exception {
		return "";
	}
}
