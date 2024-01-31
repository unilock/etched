package gg.moonflower.etched.core;

import fuzs.forgeconfigapiport.api.config.v2.ForgeConfigRegistry;
import gg.moonflower.etched.api.sound.download.SoundSourceManager;
import gg.moonflower.etched.common.network.EtchedMessages;
import gg.moonflower.etched.common.sound.download.BandcampSource;
import gg.moonflower.etched.common.sound.download.SoundCloudSource;
import gg.moonflower.etched.core.registry.EtchedBlocks;
import gg.moonflower.etched.core.registry.EtchedEntities;
import gg.moonflower.etched.core.registry.EtchedItems;
import gg.moonflower.etched.core.registry.EtchedMenus;
import gg.moonflower.etched.core.registry.EtchedRecipes;
import gg.moonflower.etched.core.registry.EtchedSounds;
import gg.moonflower.etched.core.registry.EtchedVillagers;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityWorldChangeEvents;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.commons.lang3.tuple.Pair;

public class Etched implements ModInitializer {

    public static final String MOD_ID = "etched";
    public static final EtchedConfig.Client CLIENT_CONFIG;
    public static final EtchedConfig.Server SERVER_CONFIG;
    private static final ForgeConfigSpec clientSpec;
    private static final ForgeConfigSpec serverSpec;

    static {
        Pair<EtchedConfig.Client, ForgeConfigSpec> clientConfig = new ForgeConfigSpec.Builder().configure(EtchedConfig.Client::new);
        clientSpec = clientConfig.getRight();
        CLIENT_CONFIG = clientConfig.getLeft();

        Pair<EtchedConfig.Server, ForgeConfigSpec> serverConfig = new ForgeConfigSpec.Builder().configure(EtchedConfig.Server::new);
        serverSpec = serverConfig.getRight();
        SERVER_CONFIG = serverConfig.getLeft();
    }

    @Override
    public void onInitialize() {
        EtchedBlocks.init();
        EtchedItems.init();
        EtchedEntities.init();
        EtchedMenus.init();
        EtchedSounds.init();
        EtchedRecipes.init();

        //EtchedVillagers.init();

        ForgeConfigRegistry.INSTANCE.register(Etched.MOD_ID, ModConfig.Type.CLIENT, clientSpec);
        ForgeConfigRegistry.INSTANCE.register(Etched.MOD_ID, ModConfig.Type.SERVER, serverSpec);

        //MinecraftForge.EVENT_BUS.addListener(Etched::onGrindstoneChange);
        //MinecraftForge.EVENT_BUS.addListener(Etched::onItemChangedDimension);

        EtchedMessages.initServer();

        SoundSourceManager.registerSource(new SoundCloudSource());
        SoundSourceManager.registerSource(new BandcampSource());

        ServerEntityWorldChangeEvents.AFTER_ENTITY_CHANGE_WORLD.register((originalEntity, newEntity, origin, destination) -> {
            if (newEntity instanceof ItemEntity entity) {
                if (destination.dimension() == Level.NETHER) {
                    ItemStack oldStack = entity.getItem();
                    if (oldStack.getItem() != EtchedBlocks.RADIO.asItem()) {
                        return;
                    }

                    ItemStack newStack = new ItemStack(EtchedBlocks.PORTAL_RADIO_ITEM, oldStack.getCount());
                    newStack.setTag(oldStack.getTag());
                    entity.setItem(newStack);
                }
            }
        });
    }
}
