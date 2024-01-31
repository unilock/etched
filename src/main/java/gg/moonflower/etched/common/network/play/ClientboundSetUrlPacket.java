package gg.moonflower.etched.common.network.play;

import gg.moonflower.etched.common.network.EtchedMessages;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.network.FriendlyByteBuf;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

/**
 * @param url The URL to set in the etching table
 * @author Ocelot
 */
@ApiStatus.Internal
public record ClientboundSetUrlPacket(String url) implements FabricPacket {

    public ClientboundSetUrlPacket(@Nullable String url) {
        this.url = url != null ? url : "";
    }

    public ClientboundSetUrlPacket(FriendlyByteBuf buf) {
        this(buf.readUtf());
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeUtf(this.url);
    }

    @Override
    public PacketType<?> getType() {
        return EtchedMessages.CLIENTBOUND_SET_URL;
    }
}
