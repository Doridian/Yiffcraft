package de.doridian.yiffcraft.overrides;

import de.doridian.yiffcraft.Chat;
import de.doridian.yiffcraft.Yiffcraft;
import de.doridian.yiffcraft.commands.BaseCommand;
import net.minecraft.src.GuiChat;

import java.util.HashMap;
import java.util.Map;

public class YCGuiChat extends GuiChat {
    public YCGuiChat() {
        super();
    }
    
    protected String cmdHintSet;
    protected String cmdHintDraw;

    @Override
    public void drawScreen(int i, int i1, float f) {
        if(cmdHintDraw != null) {
            drawRect(2, this.height - 28, this.width - 14, this.height - 14, 0x80000000);
            drawString(this.fontRenderer, cmdHintDraw, 4, this.height - 24, 0xe0e0e0);
        }
        super.drawScreen(i, i1, f);
    }

    @Override
    protected void keyTyped(char var1, int var2) {
        super.keyTyped(var1, var2);

        if (!this.message.isEmpty()) {
            char fChar = this.message.charAt(0);

            if(fChar == '/') {
                if (Yiffcraft.wecui.getLocalPlugin().isEnabled()) {
                    Map<String, String> commands = Yiffcraft.wecui.getLocalPlugin().getPlugin().getCommands();
                    String command = getCommand(this.message);

                    if (commands.containsKey(command)) {
                        setCommandHint("/" + command, commands.get(command));
                    } else {
                        unsetCommandHint();
                    }
                }
            } else if(fChar == '-') {
                String command = getCommand(this.message);

                if(Chat.commands.containsKey(command)) {
                    BaseCommand cmd = Chat.commands.get(command);
                    setCommandHint("-" + command, cmd.getUsage() + " - " + cmd.getHelp());
                } else {
                    unsetCommandHint();
                }
            } else {
                unsetCommandHint();
            }
        }

        refreshCommandHint();
    }

    protected void setCommandHint(String cmd, String cmdHint) {
        String newCmdHintSet = (cmd + " " + cmdHint).trim();
        if(newCmdHintSet.equals(cmdHintSet)) return;
        cmdHintSet = newCmdHintSet;

        int cmdUsageSplit = cmdHintSet.indexOf(" - ");
        if(cmdUsageSplit >= 0) {
            cmdHintAppend = cmdHintSet.substring(cmdUsageSplit);
            cmdHintSet = cmdHintSet.substring(0, cmdUsageSplit);
        } else {
            cmdHintAppend = "";
        }

        cmdHintArgs = cmdHintSet.split(" ");
        cmdHintOptional = new Boolean[cmdHintArgs.length];

        for(int i = 0; i < cmdHintArgs.length; i++) {
            cmdHintOptional[i] = (cmdHintArgs[i].charAt(0) == '[');
        }

        refreshCommandHint();
    }

    protected String cmdHintAppend;

    protected String[] cmdHintArgs;
    protected Boolean[] cmdHintOptional;
    
    protected void refreshCommandHint() {
        if(cmdHintSet == null) return;

        int argc = message.trim().split(" ").length;
        if(message.charAt(message.length() - 1) == ' ') argc++;

        int argToHighlight = argc - 1;

        if(argToHighlight >= cmdHintArgs.length) {
            cmdHintDraw = cmdHintSet;
            return;
        }

        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < cmdHintArgs.length; i++) {
            boolean wasHighlighted = false;
            if(i == argToHighlight) {
                if(cmdHintOptional[i]) {
                    for(int j = i; j < cmdHintOptional.length; j++) {
                        if(!cmdHintOptional[j]) {
                            argToHighlight = j;
                            break;
                        }
                    }
                    sb.append("\u00a7b");
                } else {
                    sb.append("\u00a7a");
                }
                wasHighlighted = true;
            }
            sb.append(cmdHintArgs[i]);
            if(wasHighlighted) {
                sb.append("\u00a7f");
            }
            sb.append(' ');
        }
        
        cmdHintDraw = sb.deleteCharAt(sb.length() - 1).append(cmdHintAppend).toString();
    }

    protected void unsetCommandHint() {
        cmdHintSet = null;
        cmdHintDraw = null;
    }

    protected String getCommand(String text) {
        String[] args = text.split(" ");

        if (args.length == 0) {
            return "";
        }

        return args[0].toLowerCase().substring(1);
    }
}
