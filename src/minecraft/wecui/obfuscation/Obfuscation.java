package wecui.obfuscation;

import java.io.File;

import de.doridian.yiffcraft.Chat;
import net.minecraft.client.Minecraft;
import net.minecraft.src.*;
import wecui.InitializationFactory;
import wecui.WorldEditCUI;
import wecui.render.RenderEntity;

/**
 * Main obfuscation class
 * Combines all obfuscated classes and methods into a single class
 * Eases updates, cleans up the rest of the codebase.
 * 
 * @author lahwran
 * @author yetanotherx
 * 
 * @obfuscated
 */
public class Obfuscation implements InitializationFactory {

    protected WorldEditCUI controller;
    protected Minecraft minecraft;
    protected Tessellator tessellator = Tessellator.instance;

    public Obfuscation(WorldEditCUI controller) {
        this.controller = controller;
    }

    public void initialize() {
        this.minecraft = this.controller.getMinecraft();
    }

    public boolean isMultiplayerWorld() {
        return minecraft.isMultiplayerWorld();
    }

    public void sendChat(String chat) {
        Chat.emitChatMsg(chat);
    }

    /**
     * Displays a chat message on the screen, if the player is currently playing
     * @param chat 
     */
    public void showChatMessage(String chat) {
        Chat.addChat(chat);
    }

    public void showGuiScreenIfGuiChat(GuiScreen screen) {
        GuiScreen currentScreen = minecraft.currentScreen;
        if (currentScreen != null) {
            minecraft.displayGuiScreen((GuiScreen)null);
        }
        minecraft.displayGuiScreen(screen);
    }

    public double getPlayerXGuess(float renderTick) {
        EntityPlayerSP plyr = minecraft.thePlayer;
        return plyr.prevPosX + ((plyr.posX - plyr.prevPosX) * renderTick);
    }

    public double getPlayerYGuess(float renderTick) {
        EntityPlayerSP plyr = minecraft.thePlayer;
        return plyr.prevPosY + ((plyr.posY - plyr.prevPosY) * renderTick);
    }

    public double getPlayerZGuess(float renderTick) {
        EntityPlayerSP plyr = minecraft.thePlayer;
        return plyr.prevPosZ + ((plyr.posZ- plyr.prevPosZ) * renderTick);
    }
    
    public void startDrawing(int type) {
        tessellator.startDrawing(type);
    }

    public void addVertex(double x, double y, double z) {
        tessellator.addVertex(x, y, z);
    }

    public void finishDrawing() {
        tessellator.draw();
    }

    public EntityPlayerSP getPlayer() {
        return getPlayer(minecraft);
    }

    public World getWorld() {
        return getWorld(minecraft);
    }

    public void spawnEntity(Entity entity) {
        Minecraft mc = this.controller.getMinecraft();

        entity = new RenderEntity(this.controller, getWorld(mc));
        doSomethingWithEntityCoordinates(mc, entity);
        getWorld(mc).entityJoinedWorld(entity);
        doSomethingWithEntityCoordinates(mc, entity);
        controller.getDebugger().debug("RenderEntity spawned");

    }

    public static double getPlayerX(EntityPlayerSP player) {
        return player.posX;
    }

    public static double getPlayerY(EntityPlayerSP player) {
        return player.posY;
    }

    public static double getPlayerZ(EntityPlayerSP player) {
        return player.posZ;
    }
    
    public static EntityPlayerSP getPlayer(Minecraft mc) {
        return mc.thePlayer;
    }
    
    public static World getWorld(Minecraft mc) {
        return mc.theWorld;
    }

    public static void doSomethingWithEntityCoordinates(Minecraft mc, Entity entity) {
        entity.setPosition(getPlayerX(mc.thePlayer), getPlayerY(mc.thePlayer), getPlayerZ(mc.thePlayer));
    }

    public static void disableLighting() {
        RenderHelper.disableStandardItemLighting();
    }

    public static void enableLighting() {
        RenderHelper.enableStandardItemLighting();
    }

    public NetClientHandler getNetClientHandler(EntityClientPlayerMP player) {
        return player.sendQueue;
    }

    public static String getChatFromPacket(Packet3Chat packet) {
        return packet.message;
    }

    public static File getMinecraftDir() {
        return Minecraft.getMinecraftDir();
    }

    public static File getWorldEditCUIDir() {
        return new File(getMinecraftDir(), "config");
    }
}
