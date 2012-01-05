package de.doridian.yiffcraft;

import de.doridian.yiffcraft.overrides.YCGuiChat;
import de.doridian.yiffcraft.overrides.YCRenderPlayer;
import net.minecraft.src.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.HashSet;

public class ClientCommands {
    static class ClientCommandException extends Exception {
        public ClientCommandException(String message) {
            super(message);
        }
    }

    public static void incoming(char cmd, final String args)
    {
        try {
            switch(cmd) {
                case 'c':
                    commandSheet(args);
                    break;
                case 't':
                    transmute(args);
                    break;
                case 'd':
                    datawatcherPut(args);
                    break;
                default:
                    Chat.addChat("Unknown YCC command: " + cmd + "|" + args);
                    break;
            }
        } catch(Exception e) {
            Chat.addChat("Error in YCC command " + cmd + ": " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static void datawatcherPut(String args) throws Exception {
        String[] argArr = args.split("\\|");
        if(argArr.length != 3) {
            throw new ClientCommandException("Invalid argc");
        }
        Integer index = Integer.valueOf(argArr[0]);

        if(index < 0) {
            //TODO: Reserved for future use
            return;
        }

        Class cls = Class.forName(argArr[1]);
        Object value = cls.getConstructor(String.class).newInstance(argArr[2]);


        Entity renderAs = YCRenderPlayer.instance.getRenderAs();
        if(renderAs != null) {
            renderAs.getDataWatcher().updateObject(index, value);
        } //We do NOT want to update the actual player's data watcher!
    }
    
    private static HashMap<String, Class> simpleTransmutes = new HashMap<String, Class>();

    static {
        simpleTransmutes.put("FishingHook", EntityFishHook.class);
        simpleTransmutes.put("Potion", EntityPotion.class);
        simpleTransmutes.put("Egg", EntityEgg.class);
    }
    
    private static void transmute(String args) {
        if(args.isEmpty()) {
            YCRenderPlayer.instance.setRenderAs(null);
            return;
        }

        double yOffset = 0; float yawOffset = 0;
        String[] argArr = args.split("\\|");
        if(argArr.length >= 3) {
            args = argArr[0];
            yawOffset = Float.parseFloat(argArr[1]);
            yOffset = Double.parseDouble(argArr[2]);
        }

        Entity ent = null;

        if(simpleTransmutes.containsKey(args)) {
            try {
                Class cls = simpleTransmutes.get(args);
                Constructor cnst = cls.getConstructor(World.class);
                ent = (Entity)cnst.newInstance(Yiffcraft.minecraft.theWorld);
            }
            catch(Exception e) { e.printStackTrace(); }
        } else {
            ent = EntityList.createEntityInWorld(args, Yiffcraft.minecraft.theWorld);
        }

        if(ent == null) return;

        YCRenderPlayer.instance.yawOffset = yawOffset;
        YCRenderPlayer.instance.yOffset = yOffset;
        YCRenderPlayer.instance.setRenderAs(ent);
    }
    
    private static void commandSheet(final String args) {
        new Thread() {
            private HashSet<URL> loadedURLs;

            public void run() {
                loadedURLs = new HashSet<URL>();
                HashMap<String, String> additionalCommands = new HashMap<String, String>();
                addToHashmap(additionalCommands, null, args);
                YCGuiChat.reloadCommands(additionalCommands);
            }

            private void addToHashmap(HashMap<String, String> additionalCommands, URL lastURL, String ctext) {
                try {
                    URL url = new URL(lastURL, ctext);
                    if(loadedURLs.contains(url)) return;
                    loadedURLs.add(url);
                    URLConnection conn = url.openConnection();
                    conn.connect();
                    BufferedReader buffre = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String cline;
                    while((cline = buffre.readLine()) != null) {
                        if(cline.isEmpty()) continue;
                        switch(cline.charAt(0)) {
                            case '@':
                                addToHashmap(additionalCommands, url, cline.substring(1));
                                break;
                            case ';':
                                //Ignore the line, its a comment!
                                break;
                            default:
                                int splpos = cline.indexOf('|');
                                if(splpos <= 0) break;
                                String cmd = cline.substring(0, splpos);
                                String cmdUsage = cline.substring(splpos + 1);
                                additionalCommands.put(cmd, cmdUsage);
                                break;
                        }

                    }
                    buffre.close();
                }
                catch(Exception e) { System.out.println("Error parsing: " + ctext); e.printStackTrace(); }
            }
        }.start();
    }
}
