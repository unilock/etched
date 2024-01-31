package gg.moonflower.etched.core.registry;

import gg.moonflower.etched.core.Etched;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

public class EtchedSounds {

    public static final SoundEvent UI_ETCHER_TAKE_RESULT = registerSound("ui.etching_table.take_result");

    public static void init() {}

    private static SoundEvent registerSound(String id) {
        return Registry.register(BuiltInRegistries.SOUND_EVENT, new ResourceLocation(Etched.MOD_ID, id), SoundEvent.createVariableRangeEvent(new ResourceLocation(Etched.MOD_ID, id)));
    }
}
