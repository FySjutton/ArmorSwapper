package avox.armorswapper;

import avox.armorswapper.config.ConfigSystem;
import net.fabricmc.api.ModInitializer;

public class ArmorSwapper implements ModInitializer {
    @Override
    public void onInitialize() {
        ConfigSystem.CONFIG.load();
    }
}