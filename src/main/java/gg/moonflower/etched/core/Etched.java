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
    }

    /* TODO
    private static void onGrindstoneChange(GrindstoneEvent.OnPlaceItem event) {
        ItemStack top = event.getTopItem();
        ItemStack bottom = event.getBottomItem();

        if (top.isEmpty() == bottom.isEmpty()) {
            return;
        }

        ItemStack stack = top.isEmpty() ? bottom : top;
        if (AlbumCoverItem.getCoverStack(stack).isPresent()) {
            ItemStack result = stack.copy();
            result.setCount(1);
            AlbumCoverItem.setCover(result, ItemStack.EMPTY);
            event.setOutput(result);
        }
    }
     */

    /* TODO
    private static void onItemChangedDimension(EntityTravelToDimensionEvent event) {
        if (event.getEntity() instanceof ItemEntity entity) {
            if (event.getDimension() == Level.NETHER) {
                ItemStack oldStack = entity.getItem();
                if (oldStack.getItem() != EtchedBlocks.RADIO.get().asItem()) {
                    return;
                }

                ItemStack newStack = new ItemStack(EtchedBlocks.PORTAL_RADIO_ITEM.get(), oldStack.getCount());
                newStack.setTag(oldStack.getTag());
                entity.setItem(newStack);
            }
        }
    }
     */
}
