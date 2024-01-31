package gg.moonflower.etched.common.network.play;

import gg.moonflower.etched.common.network.EtchedMessages;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.network.FriendlyByteBuf;
import org.jetbrains.annotations.ApiStatus;

/**
 * @param url The URL to set in the etching table
 * @author Jackson
 */
@ApiStatus.Internal
public record ServerboundSetUrlPacket(String url) implements FabricPacket {

    public ServerboundSetUrlPacket(FriendlyByteBuf buf) {
        this(buf.readUtf());
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeUtf(this.url);
    }

    @Override
    public PacketType<?> getType() {
        return EtchedMessages.SERVERBOUND_SET_URL;
    }

}
