package wecui;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import wecui.event.CUIEvent;
import wecui.event.ChatCommandEvent;
import wecui.event.listeners.CUIListener;
import wecui.event.ChatEvent;
import wecui.event.listeners.ChatListener;
import wecui.event.WorldRenderEvent;
import wecui.event.listeners.WorldEditCommandListener;
import wecui.event.listeners.WorldRenderListener;
import wecui.exception.InitializationException;
import wecui.fevents.EventManager;
import wecui.fevents.Order;
import wecui.obfuscation.Obfuscation;
import wecui.render.CUIRegion;
import wecui.render.CuboidRegion;

/**
 * Main controller class
 * 
 * TODO: Multiworld brekas it
 * TODO: Ensure that this works by putting into both mods/ and minecraft.jar/
 * 
 * @author lahwran
 * @author yetanotherx
 */
public class WorldEditCUI {

    public static final String VERSION = "1.0beta for Minecraft version 1.0";
    public static final List<String> WEVERSIONS;
    protected Minecraft minecraft;
    protected EventManager eventManager;
    protected Obfuscation obfuscation;
    protected CUIRegion selection;
    protected CUIDebug debugger;
    protected CUISettings settings;
    
    static {
        List<String> list = new ArrayList<String>();
        list.add("4.8");
        list.add("4.8-SNAPSHOT");
        WEVERSIONS = list;
    }

    public WorldEditCUI(Minecraft minecraft) {
        this.minecraft = minecraft;
    }

    public void initialize() {
        this.eventManager = new EventManager(this);
        this.obfuscation = new Obfuscation(this);
        this.selection = new CuboidRegion(this);
        this.settings = new CUISettings(this);
        this.debugger = new CUIDebug(this);

        try {
            this.eventManager.initialize();
            this.obfuscation.initialize();
            this.selection.initialize();
            this.settings.initialize();
            this.debugger.initialize();
        } catch (InitializationException e) {
            e.printStackTrace(System.err);
            return;
        }

        this.registerListeners();
    }

    protected void registerListeners() {
        CUIEvent.handlers.register(new CUIListener(this), Order.Default);
        ChatEvent.handlers.register(new ChatListener(this), Order.Default);
        WorldRenderEvent.handlers.register(new WorldRenderListener(this), Order.Default);
        
        //Register the individual /commands
        WorldEditCommandListener commListener = new WorldEditCommandListener(this);
        ChatCommandEvent.getHandlers("worldedit").register(commListener, Order.Default);
        ChatCommandEvent.getHandlers("we").register(commListener, Order.Default);
    }

    public CUIDebug getDebugger() {
        return debugger;
    }

    public Minecraft getMinecraft() {
        return minecraft;
    }

    public Obfuscation getObfuscation() {
        return obfuscation;
    }

    public CUIRegion getSelection() {
        return selection;
    }

    public void setSelection(CUIRegion selection) {
        this.selection = selection;
    }

    public CUISettings getSettings() {
        return settings;
    }

    public EventManager getEventManager() {
        return eventManager;
    }
}
