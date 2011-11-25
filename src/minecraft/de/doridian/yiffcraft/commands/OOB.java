package de.doridian.yiffcraft.commands;

import de.doridian.yiffcraft.Yiffcraft;

public class OOB extends BaseCommand {
	public void run(String[] args) throws Exception {
		Yiffcraft.enableOutOfBody = !Yiffcraft.enableOutOfBody;
		Yiffcraft.RefreshOutOfBody();
	}
	public String getHelp() throws Exception {
		return "Toggles out of body mode.";
	}
	public String getUsage() throws Exception {
		return "";
	}
}
