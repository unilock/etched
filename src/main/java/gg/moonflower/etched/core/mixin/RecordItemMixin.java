package gg.moonflower.etched.core.mixin;

import com.google.common.base.Suppliers;
import gg.moonflower.etched.api.record.AlbumCover;
import gg.moonflower.etched.api.record.PlayableRecord;
import gg.moonflower.etched.api.record.TrackData;
import gg.moonflower.etched.client.render.item.AlbumCoverItemRenderer;
import gg.moonflower.etched.client.sound.EntityRecordSoundInstance;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.RecordItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.net.Proxy;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

@Mixin(RecordItem.class)
public abstract class RecordItemMixin extends Item implements PlayableRecord {

    @Shadow
    public abstract SoundEvent getSound();

    @Unique
    private final Supplier<TrackData[]> etched$track = Suppliers.memoize(() -> {
        Component desc = Component.translatable(this.getDescriptionId() + ".desc");

        String[] parts = desc.getString().split("-", 2);
        if (parts.length < 2) {
            return new TrackData[]{new TrackData(this.getSound().getLocation().toString(), "Minecraft", desc)};
        }
        return new TrackData[]{new TrackData(this.getSound().getLocation().toString(), parts[0].trim(), Component.literal(parts[1].trim()).withStyle(desc.getStyle()))};
    });

    private RecordItemMixin(Properties properties) {
        super(properties);
    }

    @Override
    public boolean canPlay(ItemStack stack) {
        return true;
    }

    @Environment(EnvType.CLIENT)
    @Override
    public Optional<? extends SoundInstance> createEntitySound(ItemStack stack, Entity entity, int track, int attenuationDistance) {
        if (track != 0 || !(stack.getItem() instanceof RecordItem record)) {
            return Optional.empty();
        }

        if (PlayableRecord.canShowMessage(entity.getX(), entity.getY(), entity.getZ())) {
            Minecraft.getInstance().gui.setNowPlaying(record.getDisplayName());
        }
        return Optional.of(new EntityRecordSoundInstance(record.getSound(), entity));
    }

    @Environment(EnvType.CLIENT)
    @Override
    public CompletableFuture<AlbumCover> getAlbumCover(ItemStack stack, Proxy proxy, ResourceManager resourceManager) {
        ResourceLocation key = BuiltInRegistries.ITEM.getKey(this);
        return resourceManager.getResource(new ResourceLocation(key.getNamespace(), "models/item/" + AlbumCoverItemRenderer.FOLDER_NAME + "/" + key.getPath() + ".json")).isPresent() ?
                CompletableFuture.completedFuture(AlbumCover.of(new ResourceLocation(key.getNamespace(), AlbumCoverItemRenderer.FOLDER_NAME + "/" + key.getPath()))) :
                CompletableFuture.completedFuture(AlbumCover.EMPTY);
    }

    @Override
    public Optional<TrackData[]> getMusic(ItemStack stack) {
        return Optional.of(this.etched$track.get());
    }

    @Override
    public Optional<TrackData> getAlbum(ItemStack stack) {
        return Optional.empty();
    }

    @Override
    public int getTrackCount(ItemStack stack) {
        return 1;
    }
}
