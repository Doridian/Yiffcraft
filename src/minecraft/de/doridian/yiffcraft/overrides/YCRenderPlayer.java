package de.doridian.yiffcraft.overrides;

import de.doridian.yiffcraft.Yiffcraft;
import net.minecraft.src.*;

public class YCRenderPlayer extends RenderPlayer {
    public static YCRenderPlayer instance;

    protected Entity renderPlayerAs;
    private Render renderProcessor;
    protected EntityLiving renderPlayerAsLiving;

    private static float thirdPersonDistanceDefault = 0.0F;

    public double yOffset;
    public float yawOffset;

    public void setRenderAs(Entity otherEnt) {
        if(thirdPersonDistanceDefault <= 0.0F) {
            thirdPersonDistanceDefault = Yiffcraft.minecraft.entityRenderer.thirdPersonDistance;
        }
        if(otherEnt == null) {
            renderProcessor = null;
        } else {
            renderProcessor = RenderManager.instance.getEntityRenderObject(otherEnt);
        }
        if(renderProcessor == null) {
            renderPlayerAs = null;
            renderPlayerAsLiving = null;
            Yiffcraft.minecraft.entityRenderer.thirdPersonDistance = thirdPersonDistanceDefault;
        } else {
            renderPlayerAs = otherEnt;
            if(otherEnt instanceof EntityLiving) {
                renderPlayerAsLiving = (EntityLiving)renderPlayerAs;

                if(otherEnt instanceof EntityDragon) {
                    Yiffcraft.minecraft.entityRenderer.thirdPersonDistance = 14.0F;
                } else if(otherEnt instanceof EntityGhast) {
                    Yiffcraft.minecraft.entityRenderer.thirdPersonDistance = 12.0F;
                } else {
                    Yiffcraft.minecraft.entityRenderer.thirdPersonDistance = thirdPersonDistanceDefault;
                }
            } else {
                renderPlayerAsLiving = null;
            }
        }

        setCamRoll(0.0F);
    }
    
    public YCRenderPlayer() {
        super();
        instance = this;
    }

    public void setCamRoll(float roll) {
        while(roll > 180F)
            roll -= 360F;

        while(roll < -180F)
            roll += 360F;

        if(roll > 90F)
            roll = 90F;
        else if(roll < -90F)
            roll = -90F;

        Yiffcraft.minecraft.entityRenderer.camRoll = roll;
    }

    @Override
    public void renderPlayer(EntityPlayer var1, double var2, double var4, double var6, float var8, float var9) {
        if(renderPlayerAs == null || renderProcessor == null) {
            super.renderPlayer(var1, var2, var4, var6, var8, var9);
            return;
        }

        if(renderPlayerAsLiving != null) {
            renderPlayerAsLiving.field_705_Q = var1.field_705_Q;
            renderPlayerAsLiving.field_704_R = var1.field_704_R;
            renderPlayerAsLiving.field_703_S = var1.field_703_S;

            renderPlayerAsLiving.hurtTime = var1.hurtTime;
            renderPlayerAsLiving.attackedAtYaw = var1.attackedAtYaw;
            renderPlayerAsLiving.attackTime = var1.attackTime;

            renderPlayerAsLiving.prevRenderYawOffset = var1.prevRenderYawOffset;
            renderPlayerAsLiving.renderYawOffset = var1.renderYawOffset;

            if(renderPlayerAsLiving instanceof EntityDragon) {
                EntityDragon dragonLiving = (EntityDragon)renderPlayerAsLiving;
                setCamRoll((float)(dragonLiving.func_40160_a(1, var9)[0] - dragonLiving.func_40160_a(10, var9)[0]));
            }
        }
        
        renderPlayerAs.prevRotationPitch = var1.prevRotationPitch;
        renderPlayerAs.rotationPitch = var1.rotationPitch;
        renderPlayerAs.prevRotationYaw = var1.prevRotationYaw + yawOffset;
        renderPlayerAs.rotationYaw = var1.rotationYaw + yawOffset;

        renderPlayerAs.fire = ((Entity)var1).fire;

        renderProcessor.doRender(renderPlayerAs, var2, (var4 - var1.yOffset) + yOffset, var6, var8 + yawOffset, var9);
    }
}
