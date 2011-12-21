package de.doridian.yiffcraft.overrides;

import de.doridian.yiffcraft.Chat;
import de.doridian.yiffcraft.Yiffcraft;
import de.doridian.yiffcraft.commands.BaseCommand;
import net.minecraft.src.GuiChat;

import java.util.Map;

public class YCGuiChat extends GuiChat {
    public YCGuiChat() {
        super();
    }

    @Override
    public void drawScreen(int i, int i1, float f) {
        if (!this.message.isEmpty()) {
            char fChar = this.message.charAt(0);

            if(fChar == '/') {
                if (Yiffcraft.wecui.getLocalPlugin().isEnabled()) {
                    Map<String, String> commands = Yiffcraft.wecui.getLocalPlugin().getPlugin().getCommands();
                    String command = getCommand(this.message);

                    if (commands.containsKey(command)) {
                        drawCommandHint("  /" + command + " " + commands.get(command));
                    }
                }
            } else if(fChar == '-') {
                String command = getCommand(this.message);

                if(Chat.commands.containsKey(command)) {
                    BaseCommand cmd = Chat.commands.get(command);
                    drawCommandHint("  -" + command + " " + cmd.getUsage() + " - " + cmd.getHelp());
                }
            }
        }
        super.drawScreen(i, i1, f);
    }
    
    protected void drawCommandHint(String cmdHint) {
        drawRect(2, this.height - 28, this.width - 14, this.height - 14, 0x80000000);
        drawString(this.fontRenderer, cmdHint, 4, this.height - 24, 0xe0e0e0);
    }

    protected String getCommand(String text) {
        String[] args = text.split(" ");

        if (args.length == 0) {
            return "";
        }

        return args[0].toLowerCase().substring(1);
    }
}
