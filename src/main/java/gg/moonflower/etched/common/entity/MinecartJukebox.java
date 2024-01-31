package gg.moonflower.etched.common.entity;

import gg.moonflower.etched.api.record.PlayableRecord;
import gg.moonflower.etched.api.sound.SoundTracker;
import gg.moonflower.etched.core.Etched;
import gg.moonflower.etched.core.registry.EtchedEntities;
import gg.moonflower.etched.core.registry.EtchedItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.stats.Stats;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.JukeboxBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

/**
 * @author Ocelot
 */
public class MinecartJukebox extends AbstractMinecart implements WorldlyContainer {

    private static final EntityDataAccessor<Boolean> DATA_ID_HAS_RECORD = SynchedEntityData.defineId(MinecartJukebox.class, EntityDataSerializers.BOOLEAN);
    private static final int[] SLOTS = {0};

    private ItemStack record;

    public MinecartJukebox(EntityType<?> entityType, Level level) {
        super(entityType, level);
        this.record = ItemStack.EMPTY;
    }

    public MinecartJukebox(Level level, double d, double e, double f) {
        super(EtchedEntities.JUKEBOX_MINECART, level, d, e, f);
        this.record = ItemStack.EMPTY;
    }

    private void startPlaying(ItemStack stack, boolean restart) {
        if (PlayableRecord.isPlayableRecord(stack)) {
            PlayableRecord.playEntityRecord(this, stack, restart);
            this.entityData.set(DATA_ID_HAS_RECORD, true);
        }
    }

    private void stopPlaying() {
        if (this.entityData.get(DATA_ID_HAS_RECORD)) {
            PlayableRecord.stopEntityRecord(this);
            this.entityData.set(DATA_ID_HAS_RECORD, false);
        }
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_ID_HAS_RECORD, false);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide()) {
            if (Etched.CLIENT_CONFIG.showNotes.get() && this.random.nextInt(6) == 0) {
                SoundInstance instance = SoundTracker.getEntitySound(this.getId());
                if (instance != null && Minecraft.getInstance().getSoundManager().isActive(instance)) {
                    this.level().addParticle(ParticleTypes.NOTE, this.getX(), this.getY() + 1.2D, this.getZ(), this.random.nextInt(25) / 24D, 0, 0);
                }
            }
        }
    }

    @Override
    public InteractionResult interact(Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (this.entityData.get(DATA_ID_HAS_RECORD)) {
            if (!this.level().isClientSide()) {
                ItemStack itemStack = this.record;
                if (!itemStack.isEmpty()) {
                    this.clearContent();
                    ItemEntity itemEntity = new ItemEntity(this.level(), this.getX(), this.getY() + 0.8, this.getZ(), itemStack.copy());
                    itemEntity.setDefaultPickUpDelay();
                    this.level().addFreshEntity(itemEntity);
                }
            }
            return InteractionResult.sidedSuccess(this.level().isClientSide());
        } else if (stack.getItem() instanceof PlayableRecord) {
            if (!this.level().isClientSide()) {
                this.setItem(0, stack.copy());
                stack.shrink(1);
                player.awardStat(Stats.PLAY_RECORD);
            }
            return InteractionResult.sidedSuccess(this.level().isClientSide());
        }
        return InteractionResult.PASS;
    }

    @Override
    public void activateMinecart(int x, int y, int z, boolean powered) {
        if (!this.record.isEmpty()) {
            this.startPlaying(this.record.copy(), true);
        }
    }

    @Override
    public void destroy(DamageSource damageSource) {
        super.destroy(damageSource);
        if (this.level().getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS)) {
            Containers.dropItemStack(this.level(), this.getX(), this.getY(), this.getZ(), this.record);
        }
    }

    @Override
    public void remove(RemovalReason reason) {
        if (!this.level().isClientSide() && reason.shouldDestroy()) {
            Containers.dropContents(this.level(), this, this);
        }
        super.remove(reason);
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag nbt) {
        super.addAdditionalSaveData(nbt);
        nbt.putBoolean("HasRecord", this.entityData.get(DATA_ID_HAS_RECORD));
        if (!this.record.isEmpty()) {
            nbt.put("RecordItem", this.record.save(new CompoundTag()));
        }
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag nbt) {
        super.readAdditionalSaveData(nbt);
        this.entityData.set(DATA_ID_HAS_RECORD, nbt.getBoolean("HasRecord"));
        if (nbt.contains("RecordItem", 10)) {
            this.record = ItemStack.of(nbt.getCompound("RecordItem"));
        }
    }

    @Override
    public BlockState getDefaultDisplayBlockState() {
        return Blocks.JUKEBOX.defaultBlockState().setValue(JukeboxBlock.HAS_RECORD, this.entityData.get(DATA_ID_HAS_RECORD));
    }

    @Override
    public Item getDropItem() {
        return EtchedItems.JUKEBOX_MINECART;
    }

    @Override
    public Type getMinecartType() {
        return Type.SPAWNER;
    }

    @Override
    public int[] getSlotsForFace(Direction side) {
        return SLOTS;
    }

    @Override
    public boolean canPlaceItemThroughFace(int index, ItemStack stack, @Nullable Direction direction) {
        return PlayableRecord.isPlayableRecord(stack);
    }

    @Override
    public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction direction) {
        return true;
    }

    @Override
    public int getContainerSize() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return this.record.isEmpty();
    }

    @Override
    public ItemStack getItem(int index) {
        return index == 0 ? this.record : ItemStack.EMPTY;
    }

    @Override
    public ItemStack removeItem(int index, int count) {
        if (index != 0) {
            return ItemStack.EMPTY;
        }
        ItemStack split = this.record.split(count);
        this.setChanged();
        if (this.record.isEmpty()) {
            this.stopPlaying();
        }
        return split;
    }

    @Override
    public ItemStack removeItemNoUpdate(int index) {
        return this.removeItem(index, this.record.getCount());
    }

    @Override
    public void setItem(int index, ItemStack stack) {
        if (index == 0) {
            if (!this.record.isEmpty()) {
                this.stopPlaying();
            }
            this.startPlaying(stack.copy(), false);
            this.record = stack;
        }
    }

    @Override
    public boolean stillValid(Player player) {
        return false;
    }

    @Override
    public void clearContent() {
        if (!this.record.isEmpty()) {
            this.setItem(0, ItemStack.EMPTY);
        }
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }

    @Override
    public void setChanged() {
    }
}
