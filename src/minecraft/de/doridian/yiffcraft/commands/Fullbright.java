package de.doridian.yiffcraft.commands;

import de.doridian.yiffcraft.Yiffcraft;

public class Fullbright extends BaseCommand {
	public void run(String[] args) throws Exception {
		Yiffcraft.enableFullbright = !Yiffcraft.enableFullbright;
		Yiffcraft.rerender();
	}
	public String getHelp() throws Exception {
		return "Toggles fullbright mode.";
	}
	public String getUsage() throws Exception {
		return "";
	}
}
