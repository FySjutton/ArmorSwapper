package avox.armorswapper.config;

import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.*;
import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.SerialEntry;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class ConfigSystem {
    public static final ConfigClassHandler<ConfigSystem> CONFIG = ConfigClassHandler.createBuilder(ConfigSystem.class)
            .serializer(config -> GsonConfigSerializerBuilder.create(config)
                    .setPath(FabricLoader.getInstance().getConfigDir().resolve("armorswapper.json"))
                    .build())
            .build();

    @SerialEntry public boolean enableMod = true;
    @SerialEntry public boolean requireShift = false;

    public static Screen configScreen(Screen parent) {
        return YetAnotherConfigLib.create(CONFIG, ((defaults, config, builder) -> builder
                .title(Text.translatable("armorswapper.category"))
                .category(ConfigCategory.createBuilder()
                        .name(Text.translatable("armorswapper.config.category.general"))

                        .option(Option.<Boolean>createBuilder()
                                .name(Text.translatable("armorswapper.config.option.enable_mod"))
                                .description(OptionDescription.of(Text.translatable("armorswapper.config.option.enable_mod.desc")))
                                .binding(true, () -> config.enableMod, newVal -> config.enableMod = newVal)
                                .controller(opt -> BooleanControllerBuilder.create(opt).coloured(true))
                                .build())
                        .option(Option.<Boolean>createBuilder()
                                .name(Text.translatable("armorswapper.config.option.require_shift"))
                                .description(OptionDescription.of(Text.translatable("armorswapper.config.option.require_shift.desc")))
                                .binding(false, () -> config.requireShift, newVal -> config.requireShift = newVal)
                                .controller(opt -> BooleanControllerBuilder.create(opt).coloured(true))
                                .build())
                        .build())
        )).generateScreen(parent);
    }
}