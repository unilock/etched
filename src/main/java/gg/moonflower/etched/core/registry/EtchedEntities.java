package gg.moonflower.etched.core.registry;

import gg.moonflower.etched.common.entity.MinecartJukebox;
import gg.moonflower.etched.core.Etched;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

public class EtchedEntities {
    public static final EntityType<MinecartJukebox> JUKEBOX_MINECART = Registry.register(BuiltInRegistries.ENTITY_TYPE, new ResourceLocation(Etched.MOD_ID, "jukebox_minecart"), EntityType.Builder.<MinecartJukebox>of(MinecartJukebox::new, MobCategory.MISC).sized(0.98F, 0.7F).clientTrackingRange(8).build("minecart_jukebox"));

    public static void init() {}
}
