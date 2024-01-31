package gg.moonflower.etched.core.registry;

import gg.moonflower.etched.common.recipe.ComplexMusicLabelRecipe;
import gg.moonflower.etched.core.Etched;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;

public class EtchedRecipes {

    public static final SimpleCraftingRecipeSerializer<ComplexMusicLabelRecipe> COMPLEX_MUSIC_LABEL = Registry.register(BuiltInRegistries.RECIPE_SERIALIZER, new ResourceLocation(Etched.MOD_ID, "complex_music_label"), new SimpleCraftingRecipeSerializer<>(ComplexMusicLabelRecipe::new));

    public static void init() {}
}