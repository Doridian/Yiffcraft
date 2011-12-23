package de.doridian.yiffcraft;

import de.doridian.yiffcraft.overrides.YCGuiChat;
import de.doridian.yiffcraft.overrides.YCRenderPlayer;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityCreeper;
import net.minecraft.src.World;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.HashSet;

public class ClientCommands {
    public static void incoming(char cmd, final String args)
    {
        switch(cmd) {
            case 'c':
                commandSheet(args);
                break;
            case 't':
                transmute(args.toLowerCase());
                break;
            default:
                Chat.addChat("Unknown YCC command: " + cmd + "|" + args);
                break;
        }
    }
    
    private static HashMap<String, Class> simpleTransmutes = new HashMap<String, Class>();

    static {
        simpleTransmutes.put("creeper", EntityCreeper.class);
    }
    
    private static void transmute(final String args) {
        if(simpleTransmutes.containsKey(args)) {
            try {
                Class cls = simpleTransmutes.get(args);
                Constructor cnst = cls.getConstructor(World.class);
                Entity ent = (Entity)cnst.newInstance(Yiffcraft.minecraft.theWorld);
                if(ent == null) {
                    throw new Exception("Could not construct class " + cls.getName() + " apparently?!");
                }
                YCRenderPlayer.instance.setRenderAs(ent);
            }
            catch(Exception e) { e.printStackTrace(); }
        }
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
