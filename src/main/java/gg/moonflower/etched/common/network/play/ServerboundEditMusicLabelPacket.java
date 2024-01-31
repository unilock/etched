package gg.moonflower.etched.common.network.play;

import gg.moonflower.etched.common.network.EtchedMessages;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.network.FriendlyByteBuf;
import org.jetbrains.annotations.ApiStatus;

/**
 * @param slot   The slot the music label is in
 * @param author The new author
 * @param title  The new title
 */
@ApiStatus.Internal
public record ServerboundEditMusicLabelPacket(int slot, String author, String title) implements FabricPacket {

    public ServerboundEditMusicLabelPacket(FriendlyByteBuf buf) {
        this(buf.readVarInt(), buf.readUtf(128), buf.readUtf(128));
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeVarInt(this.slot);
        buf.writeUtf(this.author, 128);
        buf.writeUtf(this.title, 128);
    }

    @Override
    public PacketType<?> getType() {
        return EtchedMessages.SERVERBOUND_EDIT_MUSIC_LABEL;
    }
}
