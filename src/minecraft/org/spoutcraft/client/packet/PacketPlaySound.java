/*
 * This file is part of Spoutcraft (http://www.spout.org/).
 *
 * Spoutcraft is licensed under the SpoutDev License Version 1.
 *
 * Spoutcraft is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * In addition, 180 days after any changes are published, you can use the
 * software, incorporating those changes, under the terms of the MIT license,
 * as described in the SpoutDev License Version 1.
 *
 * Spoutcraft is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License,
 * the MIT license and the SpoutDev license version 1 along with this program.
 * If not, see <http://www.gnu.org/licenses/> for the GNU Lesser General Public
 * License and see <http://www.spout.org/SpoutDevLicenseV1.txt> for the full license,
 * including the MIT license.
 */
package org.spoutcraft.client.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.src.*;

import org.spoutcraft.client.SpoutClient;
import org.spoutcraft.spoutcraftapi.sound.Music;
import org.spoutcraft.spoutcraftapi.sound.SoundEffect;

public class PacketPlaySound implements SpoutPacket{
	short soundId;
	boolean location = false;
	int x, y, z;
	int volume, distance;

	public PacketPlaySound() {

	}

	public int getNumBytes() {
		return 23;
	}

	public void readData(DataInputStream input) throws IOException {
		soundId = input.readShort();
		location = input.readBoolean();
		x = input.readInt();
		y = input.readInt();
		z = input.readInt();
		distance = input.readInt();
		volume = input.readInt();
	}

	public void writeData(DataOutputStream output) throws IOException {
		output.writeShort(soundId);
		output.writeBoolean(location);
		if (!location) {
			output.writeInt(-1);
			output.writeInt(-1);
			output.writeInt(-1);
			output.writeInt(-1);
		} else {
			output.writeInt(x);
			output.writeInt(y);
			output.writeInt(z);
			output.writeInt(distance);
		}
		output.writeInt(volume);
	}

	public void run(int entityId) {
		EntityPlayer e = SpoutClient.getInstance().getPlayerFromId(entityId);
		if (e != null) {
				SoundManager sndManager = SpoutClient.getHandle().sndManager;
				if (soundId > -1 && soundId <= SoundEffect.getMaxId()) {
					SoundEffect effect = SoundEffect.getSoundEffectFromId(soundId);
					if (!location) {
						sndManager.playSoundFX(effect.getName(), 0.5F, 0.7F, effect.getSoundId(), volume / 100F);
					} else {
						sndManager.playSound(effect.getName(), x, y, z, 0.5F, (distance / 16F), effect.getSoundId(), volume / 100F);
					}
				}
				soundId -= (1 + SoundEffect.getMaxId());
				if (soundId > -1 && soundId <= Music.getMaxId()) {
					Music music = Music.getMusicFromId(soundId);
					sndManager.playMusic(music.getName(), music.getSoundId(), volume / 100F);
				}
		}
	}

	public PacketType getPacketType() {
		return PacketType.PacketPlaySound;
	}

	public int getVersion() {
		return 0;
	}

	public void failure(int playerId) {

	}
}
