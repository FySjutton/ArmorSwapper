package avox.armorswapper.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ElytraItem;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import avox.armorswapper.config.ConfigSystem;

import static net.minecraft.entity.LivingEntity.getPreferredEquipmentSlot;

@Mixin(HandledScreen.class)
public abstract class ArmorSwapMixin {
    @Inject(method = "mouseClicked", at = @At("HEAD"), cancellable = true)
    private void onMouseClicked(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> cir) {
        if (!ConfigSystem.CONFIG.instance().enableMod) return;
        if (button != 1) return;
        if (ConfigSystem.CONFIG.instance().requireShift) {
            if (!Screen.hasShiftDown()) return;
        }
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null || client.interactionManager == null) return;

        HandledScreen<?> screen = (HandledScreen<?>)(Object)this;
        Slot slot = screen.getSlotAt(mouseX, mouseY);
        if (slot == null || !slot.hasStack()) return;

        ItemStack stack = slot.getStack();
        if (!(stack.getItem() instanceof ArmorItem || stack.getItem() instanceof ElytraItem)) return;

        ScreenHandler handler = screen.getScreenHandler();
        int armorSlotIndex = 8 - getPreferredEquipmentSlot(stack).getEntitySlotId();

        int clickedIndex = slot.getIndex();
        if (clickedIndex != armorSlotIndex) {
            client.interactionManager.clickSlot(handler.syncId, clickedIndex, 0, SlotActionType.PICKUP, client.player);
            client.interactionManager.clickSlot(handler.syncId, armorSlotIndex, 0, SlotActionType.PICKUP, client.player);
            client.interactionManager.clickSlot(handler.syncId, clickedIndex, 0, SlotActionType.PICKUP, client.player);

            cir.setReturnValue(true);
        }
    }
}