package wecui.gui;

import java.util.Map;

import net.minecraft.src.GuiChat;
import wecui.WorldEditCUI;

/**
 * Main GUI class for WorldEdit commands
 * 
 * @author yetanotherx
 * 
 * @obfuscated
 */
public class WorldEditScreen extends GuiChat {

    protected WorldEditCUI controller;

    public WorldEditScreen(WorldEditCUI controller) {
        super();
        this.controller = controller;
    }

    /**
     * Draws the screen
     */
    @Override
    public void drawScreen(int i, int i1, float f) {
        if (!this.getMessage().isEmpty() && this.getMessage().substring(0, 1).equals("/")) {
            if (controller.getLocalPlugin().isEnabled()) {
                Map<String, String> commands = controller.getLocalPlugin().getPlugin().getCommands();
                String command = getCommand(this.getMessage());

                if (commands.containsKey(command.substring(1))) {
                    drawRect(2, this.getScreenHeight() - 28, this.getScreenWidth() - 14, this.getScreenHeight() - 14, 0x80000000);
                    drawString("  " + command + " " + commands.get(command.substring(1)), 4, this.height - 24, 0xe0e0e0);
                }

            }
        }
        super.drawScreen(i, i1, f);
    }

    protected void setMessage(String message) {
        this.message = message;
    }

    protected String getMessage() {
        return this.message;
    }

    protected int getScreenHeight() {
        return this.height;
    }

    protected int getScreenWidth() {
        return this.width;
    }

    protected void drawString(String string, int x, int y, int color) {
        this.drawString(this.fontRenderer, string, x, y, color);
    }

    protected String getCommand(String text) {
        String[] args = text.split(" ");

        if (args.length == 0) {
            return "";
        }

        return args[0].toLowerCase();
    }
}
