package de.doridian.yiffcraft.commands;

import de.doridian.yiffcraft.Yiffcraft;

public class Clear extends BaseCommand {
	public void run(String[] args) throws Exception {
		Yiffcraft.minecraft.ingameGUI.clearChatMessages();
	}
	public String getHelp() throws Exception {
		return "Clears all chat messages from the display.";
	}
	public String getUsage() throws Exception {
		return "";
	}
}
