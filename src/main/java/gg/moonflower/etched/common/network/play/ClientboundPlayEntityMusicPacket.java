package gg.moonflower.etched.common.network.play;

import gg.moonflower.etched.common.network.EtchedMessages;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.ApiStatus;

/**
 * @author Ocelot
 */
@ApiStatus.Internal
public class ClientboundPlayEntityMusicPacket implements FabricPacket {

    private final Action action;
    private final ItemStack record;
    private final int entityId;

    public ClientboundPlayEntityMusicPacket(ItemStack record, Entity entity, boolean restart) {
        this.action = restart ? Action.RESTART : Action.START;
        this.record = record;
        this.entityId = entity.getId();
    }

    public ClientboundPlayEntityMusicPacket(Entity entity) {
        this.action = Action.STOP;
        this.record = ItemStack.EMPTY;
        this.entityId = entity.getId();
    }

    public ClientboundPlayEntityMusicPacket(FriendlyByteBuf buf) {
        this.action = buf.readEnum(Action.class);
        this.record = this.action == Action.STOP ? ItemStack.EMPTY : buf.readItem();
        this.entityId = buf.readVarInt();
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeEnum(this.action);
        if (this.action != Action.STOP) {
            buf.writeItem(this.record);
        }
        buf.writeVarInt(this.entityId);
    }

    @Override
    public PacketType<?> getType() {
        return EtchedMessages.CLIENTBOUND_PLAY_ENTITY_MUSIC;
    }

    /**
     * @return The action to be performed on the client
     */
    public Action getAction() {
        return this.action;
    }

    /**
     * @return The id of the record item
     */
    public ItemStack getRecord() {
        return this.record;
    }

    /**
     * @return The id of the minecart entity
     */
    public int getEntityId() {
        return this.entityId;
    }

    /**
     * @author Ocelot
     */
    public enum Action {
        START, STOP, RESTART
    }
}
