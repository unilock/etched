package gg.moonflower.etched.common.network.play;

import gg.moonflower.etched.common.network.EtchedMessages;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.network.FriendlyByteBuf;
import org.jetbrains.annotations.ApiStatus;

/**
 * @param playingIndex The playing index to set the jukebox to
 * @param track        The track to set the jukebox to
 * @author Ocelot
 */
@ApiStatus.Internal
public record SetAlbumJukeboxTrackPacket(int playingIndex, int track) implements FabricPacket {

    public SetAlbumJukeboxTrackPacket(FriendlyByteBuf buf) {
        this(buf.readVarInt(), buf.readVarInt());
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeVarInt(this.playingIndex);
        buf.writeVarInt(this.track);
    }

    @Override
    public PacketType<?> getType() {
        return EtchedMessages.SET_ALBUM_JUKEBOX_TRACK;
    }
}
