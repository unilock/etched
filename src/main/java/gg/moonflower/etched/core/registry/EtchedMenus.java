package gg.moonflower.etched.core.registry;

import gg.moonflower.etched.common.menu.AlbumCoverMenu;
import gg.moonflower.etched.common.menu.AlbumJukeboxMenu;
import gg.moonflower.etched.common.menu.BoomboxMenu;
import gg.moonflower.etched.common.menu.EtchingMenu;
import gg.moonflower.etched.common.menu.RadioMenu;
import gg.moonflower.etched.core.Etched;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

public class EtchedMenus {

    public static final MenuType<EtchingMenu> ETCHING_MENU = register("etching_table", new MenuType<>(EtchingMenu::new, FeatureFlags.VANILLA_SET));
    public static final MenuType<AlbumJukeboxMenu> ALBUM_JUKEBOX_MENU = register("album_jukebox", new MenuType<>(AlbumJukeboxMenu::new, FeatureFlags.VANILLA_SET));
    public static final MenuType<BoomboxMenu> BOOMBOX_MENU = register("boombox", new MenuType<>(BoomboxMenu::new, FeatureFlags.VANILLA_SET));
    public static final MenuType<AlbumCoverMenu> ALBUM_COVER_MENU = register("album_cover", new MenuType<>(AlbumCoverMenu::new, FeatureFlags.VANILLA_SET));
    public static final MenuType<RadioMenu> RADIO_MENU = register("radio", new MenuType<>(RadioMenu::new, FeatureFlags.VANILLA_SET));

    public static void init() {}

    private static <R extends AbstractContainerMenu> MenuType<R> register(String path, MenuType<R> type) {
        Registry.register(BuiltInRegistries.MENU, new ResourceLocation(Etched.MOD_ID, path), type);
        return type;
    }
}
