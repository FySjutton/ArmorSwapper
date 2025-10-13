package avox.armorswapper.mixin;

import avox.armorswapper.ArmorSwapper;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.screen.ScreenHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(HandledScreen.class)
public abstract class ArmorSwapMixin {

    @Shadow
    public abstract ScreenHandler getScreenHandler();

    @Inject(method = "mouseClicked", at = @At("HEAD"), cancellable = true)
    private void onMouseClicked(Click click, boolean doubled, CallbackInfoReturnable<Boolean> cir) {
        if (ArmorSwapper.handleClick(click, (HandledScreen<?>)(Object)this, this.getScreenHandler().getCursorStack())) {
            cir.setReturnValue(true);
            cir.cancel();
        }
    }
}