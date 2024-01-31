package gg.moonflower.etched.core;

import gg.moonflower.etched.client.render.EtchedModelLayers;
import gg.moonflower.etched.client.render.JukeboxMinecartRenderer;
import gg.moonflower.etched.client.render.item.AlbumCoverItemRenderer;
import gg.moonflower.etched.client.screen.AlbumCoverScreen;
import gg.moonflower.etched.client.screen.AlbumJukeboxScreen;
import gg.moonflower.etched.client.screen.BoomboxScreen;
import gg.moonflower.etched.client.screen.EtchingScreen;
import gg.moonflower.etched.client.screen.RadioScreen;
import gg.moonflower.etched.common.item.BlankMusicDiscItem;
import gg.moonflower.etched.common.item.BoomboxItem;
import gg.moonflower.etched.common.item.ComplexMusicLabelItem;
import gg.moonflower.etched.common.item.EtchedMusicDiscItem;
import gg.moonflower.etched.common.item.MusicLabelItem;
import gg.moonflower.etched.common.network.EtchedMessages;
import gg.moonflower.etched.core.registry.EtchedBlocks;
import gg.moonflower.etched.core.registry.EtchedEntities;
import gg.moonflower.etched.core.registry.EtchedItems;
import gg.moonflower.etched.core.registry.EtchedMenus;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.model.MinecartModel;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTabs;

public class EtchedClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ItemProperties.register(EtchedItems.BOOMBOX, new ResourceLocation(Etched.MOD_ID, "playing"), (stack, level, entity, i) -> {
            if (!(entity instanceof Player)) {
                return 0;
            }
            InteractionHand hand = BoomboxItem.getPlayingHand(entity);
            return hand != null && stack == entity.getItemInHand(hand) ? 1 : 0;
        });
        ItemProperties.register(EtchedItems.ETCHED_MUSIC_DISC, new ResourceLocation(Etched.MOD_ID, "pattern"), (stack, level, entity, i) -> EtchedMusicDiscItem.getPattern(stack).ordinal() / 10F);

//            ItemBlockRenderTypes.setRenderLayer(EtchedBlocks.ETCHING_TABLE.get(), ChunkRenderTypeSet.of(RenderType.cutout()));
//            ItemBlockRenderTypes.setRenderLayer(EtchedBlocks.RADIO.get(), ChunkRenderTypeSet.of(RenderType.cutout()));

//            ItemRendererRegistry.registerHandModel(EtchedItems.BOOMBOX.get(), new ModelResourceLocation(new ResourceLocation(Etched.MOD_ID, "boombox_in_hand"), "inventory"));
//            ItemRendererRegistry.registerRenderer(EtchedItems.ALBUM_COVER.get(), AlbumCoverItemRenderer.INSTANCE);

        MenuScreens.register(EtchedMenus.ETCHING_MENU, EtchingScreen::new);
        MenuScreens.register(EtchedMenus.ALBUM_JUKEBOX_MENU, AlbumJukeboxScreen::new);
        MenuScreens.register(EtchedMenus.BOOMBOX_MENU, BoomboxScreen::new);
        MenuScreens.register(EtchedMenus.ALBUM_COVER_MENU, AlbumCoverScreen::new);
        MenuScreens.register(EtchedMenus.RADIO_MENU, RadioScreen::new);

        ResourceManagerHelper.get(PackType.CLIENT_RESOURCES).registerReloadListener(AlbumCoverItemRenderer.INSTANCE);

        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.TOOLS_AND_UTILITIES).register(entries -> {
            entries.accept(EtchedItems.MUSIC_LABEL);
            entries.accept(EtchedItems.BLANK_MUSIC_DISC);
            entries.accept(EtchedItems.BOOMBOX);
            entries.accept(EtchedItems.ALBUM_COVER);
        });

        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.REDSTONE_BLOCKS).register(entries -> {
            entries.accept(EtchedItems.JUKEBOX_MINECART);
        });

        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.FUNCTIONAL_BLOCKS).register(entries -> {
            entries.accept(EtchedBlocks.ETCHING_TABLE);
            entries.accept(EtchedBlocks.ALBUM_JUKEBOX);
            entries.accept(EtchedBlocks.RADIO);
        });

        ModelLoadingPlugin.register(ctx -> {
            ResourceManager resourceManager = Minecraft.getInstance().getResourceManager();
            String folder = "models/item/" + AlbumCoverItemRenderer.FOLDER_NAME;
            ctx.addModels(new ModelResourceLocation(new ResourceLocation(Etched.MOD_ID, "boombox_in_hand"), "inventory"));
            for (ResourceLocation location : resourceManager.listResources(folder, name -> name.getPath().endsWith(".json")).keySet()) {
                ctx.addModels(new ModelResourceLocation(new ResourceLocation(location.getNamespace(), location.getPath().substring(12, location.getPath().length() - 5)), "inventory"));
            }
        });

        EntityRendererRegistry.register(EtchedEntities.JUKEBOX_MINECART, JukeboxMinecartRenderer::new);

        EntityModelLayerRegistry.registerModelLayer(EtchedModelLayers.JUKEBOX_MINECART, MinecartModel::createBodyLayer);

        ColorProviderRegistry.ITEM.register((stack, index) -> index == 0 || index == 1 ? MusicLabelItem.getLabelColor(stack) : -1, EtchedItems.MUSIC_LABEL);
        ColorProviderRegistry.ITEM.register((stack, index) -> index == 0 ? ComplexMusicLabelItem.getPrimaryColor(stack) : index == 1 ? ComplexMusicLabelItem.getSecondaryColor(stack) : -1, EtchedItems.COMPLEX_MUSIC_LABEL);

        ColorProviderRegistry.ITEM.register((stack, index) -> index > 0 ? -1 : ((BlankMusicDiscItem) stack.getItem()).getColor(stack), EtchedItems.BLANK_MUSIC_DISC);
        ColorProviderRegistry.ITEM.register((stack, index) -> {
            if (index == 0) {
                return EtchedMusicDiscItem.getDiscColor(stack);
            }
            if (EtchedMusicDiscItem.getPattern(stack).isColorable()) {
                if (index == 1) {
                    return EtchedMusicDiscItem.getLabelPrimaryColor(stack);
                }
                if (index == 2) {
                    return EtchedMusicDiscItem.getLabelSecondaryColor(stack);
                }
            }
            return -1;
        }, EtchedItems.ETCHED_MUSIC_DISC);

        EtchedMessages.initClient();
    }
}
