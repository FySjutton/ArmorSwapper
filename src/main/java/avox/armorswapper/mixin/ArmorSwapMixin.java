package avox.armorswapper.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.EquippableComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import avox.armorswapper.config.ConfigSystem;

@Mixin(HandledScreen.class)
public abstract class ArmorSwapMixin {
    @Inject(method = "mouseClicked", at = @At("HEAD"), cancellable = true)
    private void onMouseClicked(Click click, boolean doubled, CallbackInfoReturnable<Boolean> cir) {
        if (!ConfigSystem.CONFIG.instance().enableMod) return;
        if (click.button() != 1) return;
        if (ConfigSystem.CONFIG.instance().requireShift) {
            if (!click.hasShift()) return;
        }
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null || client.interactionManager == null) return;

        HandledScreen<?> screen = (HandledScreen<?>)(Object)this;
        Slot slot = screen.getSlotAt(click.x(), click.y());
        if (slot == null || !slot.hasStack()) return;

        ItemStack stack = slot.getStack();
        EquippableComponent piece = stack.get(DataComponentTypes.EQUIPPABLE);

        if (piece != null) {
            ScreenHandler handler = screen.getScreenHandler();
            int armorSlot = 8 - piece.slot().getEntitySlotId();
            int clickedIndex = slot.getIndex();

            if (clickedIndex != armorSlot) {
                client.interactionManager.clickSlot(handler.syncId, clickedIndex, 0, SlotActionType.PICKUP, client.player);
                client.interactionManager.clickSlot(handler.syncId, armorSlot, 0, SlotActionType.PICKUP, client.player);
                client.interactionManager.clickSlot(handler.syncId, clickedIndex, 0, SlotActionType.PICKUP, client.player);

                cir.setReturnValue(true);
            }
        }
    }
}