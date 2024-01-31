package gg.moonflower.etched.core.mixin;

import gg.moonflower.etched.common.item.AlbumCoverItem;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.GrindstoneMenu;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GrindstoneMenu.class)
public class GrindstoneMenuMixin {
    @Shadow
    @Final
    Container repairSlots;

    @Shadow
    @Final
    private Container resultSlots;

    @Inject(method = "createResult", at = @At("HEAD"), cancellable = true)
    private void createResult(CallbackInfo ci) {
        ItemStack top = this.repairSlots.getItem(0);
        ItemStack bottom = this.repairSlots.getItem(1);

        if (top.isEmpty() == bottom.isEmpty()) {
            return;
        }

        ItemStack stack = top.isEmpty() ? bottom : top;
        if (AlbumCoverItem.getCoverStack(stack).isPresent()) {
            ItemStack result = stack.copy();
            result.setCount(1);
            AlbumCoverItem.setCover(result, ItemStack.EMPTY);

            this.resultSlots.setItem(0, result);
            ((GrindstoneMenu) (Object) this).broadcastChanges();
            ci.cancel();
        }
    }
}
