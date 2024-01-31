package gg.moonflower.etched.core.mixin.client;

import net.minecraft.client.gui.Gui;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(Gui.class)
public class GuiMixin {

    @ModifyArg(method = "render(Lnet/minecraft/client/gui/GuiGraphics;F)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/Mth;hsvToRgb(FFF)I"), index = 0)
    public float modifyHue(float hue) {
        return ((hue * 50.0F) % 50.0F) / 50.0F;
    }
}
