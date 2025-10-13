package avox.armorswapper;

import avox.armorswapper.config.ConfigSystem;
import net.fabricmc.api.ModInitializer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.EquippableComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;

public class ArmorSwapper implements ModInitializer {
    @Override
    public void onInitialize() {
        ConfigSystem.CONFIG.load();
    }

    public static boolean handleClick(Click click, HandledScreen<?> screen, ItemStack cursorStack) {
        // TODO: Fix cursorStack
        if (!ConfigSystem.CONFIG.instance().enableMod || cursorStack != null) return false;
        if (click.button() != 1) return false;
        if (ConfigSystem.CONFIG.instance().requireShift) {
            if (!click.hasShift()) return false;
        }
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null || client.interactionManager == null) return false;

        Slot slot = screen.getSlotAt(click.x(), click.y());
        if (slot == null || !slot.hasStack()) return false;

        ItemStack stack = slot.getStack();
        EquippableComponent piece = stack.get(DataComponentTypes.EQUIPPABLE);

        if (piece != null) {
            ScreenHandler handler = screen.getScreenHandler();
            int clickedIndex = slot.id;
            int armorSlot = 8 - piece.slot().getEntitySlotId();

            if (clickedIndex != armorSlot) {
                client.interactionManager.clickSlot(handler.syncId, clickedIndex, 0, SlotActionType.PICKUP, client.player);
                client.interactionManager.clickSlot(handler.syncId, armorSlot, 0, SlotActionType.PICKUP, client.player);
                client.interactionManager.clickSlot(handler.syncId, clickedIndex, 0, SlotActionType.PICKUP, client.player);

                return true;
            }
        }
        return false;
    }
}