package wecui.render;

import net.minecraft.src.Entity;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.World;
import wecui.WorldEditCUI;
import wecui.obfuscation.Obfuscation;

/**
 * Custom entity renderer, attached in the ModLoader class
 * 
 * @author lahwran
 * @author yetanotherx
 */
public class RenderEntity extends Entity {

    protected WorldEditCUI controller;

    public RenderEntity(WorldEditCUI controller, World world) {
        super(world);
        this.controller = controller;
        this.ignoreFrustumCheck = true;
        controller.getDebugger().debug("Entity spawned");
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound arg0) {
    }

    @Override
    protected void entityInit() {
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound arg0) {
    }

    @Override
    public void onEntityUpdate() {
        Obfuscation.doSomethingWithEntityCoordinates(controller.getMinecraft(), this);
    }

    @Override
    public void setEntityDead() {
    }
}
