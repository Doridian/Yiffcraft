package de.doridian.yiffcraft.overrides;

import de.doridian.yiffcraft.PlayerData;
import de.doridian.yiffcraft.Yiffcraft;
import de.doridian.yiffcraft.gui.ingame.Radar;
import net.minecraft.client.Minecraft;
import net.minecraft.src.*;

public class YCEntityClientPlayerMP extends EntityClientPlayerMP {

	public YCEntityClientPlayerMP(Minecraft var1, World var2, Session var3, NetClientHandler var4) {
		super(var1, var2, var3, var4);
	}

    @Override
    public boolean handleLavaMovement() {
        if(Yiffcraft.enableWaterwalk) return false;
        return super.handleLavaMovement();
    }

    @Override
    public boolean handleWaterMovement() {
        if(Yiffcraft.enableWaterwalk) return false;
        return super.handleWaterMovement();
    }
	
	@Override
	public boolean canBePushed() {
		return !Yiffcraft.enableUnpushablePlayer;
	}

    @Override
    public void onUpdate2() {
        Radar.updateLocalPlayerPosition();

        if(Yiffcraft.enableOutOfBody) {
            this.noClip = Yiffcraft.enableFly;
            this.sendQueue.addToSendQueue(new Packet10Flying(Yiffcraft.realOnGround));
            return;
        }

        this.noClip = false;
        super.onUpdate2();
    }
}
