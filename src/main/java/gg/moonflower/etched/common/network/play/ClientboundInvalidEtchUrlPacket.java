package gg.moonflower.etched.common.network.play;

import gg.moonflower.etched.common.network.EtchedMessages;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.network.FriendlyByteBuf;
import org.jetbrains.annotations.ApiStatus;

/**
 * @param exception The exception to set in the etching table
 * @author Jackson
 */
@ApiStatus.Internal
public record ClientboundInvalidEtchUrlPacket(String exception) implements FabricPacket {

    public ClientboundInvalidEtchUrlPacket(FriendlyByteBuf buf) {
        this(buf.readUtf());
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeUtf(this.exception);
    }

    @Override
    public PacketType<?> getType() {
        return EtchedMessages.CLIENTBOUND_INVALID_ETCH_URL;
    }
}
