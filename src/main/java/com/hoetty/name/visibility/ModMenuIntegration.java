package com.hoetty.name.visibility;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

import dev.isxander.yacl3.api.ConfigCategory;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.OptionDescription;
import dev.isxander.yacl3.api.YetAnotherConfigLib;
import dev.isxander.yacl3.api.controller.ColorControllerBuilder;
import dev.isxander.yacl3.api.controller.FloatSliderControllerBuilder;
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder;
import java.awt.Color;
import net.minecraft.text.Text;

/**
 * Config Screen for ModMenu Integration.
 */
public class ModMenuIntegration implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parentScreen -> YetAnotherConfigLib.createBuilder()
                .title(Text.literal("Better Name Visibility Configuration."))
                .category(ConfigCategory.createBuilder()
                        .name(Text.literal("General"))
                        .tooltip(Text.literal(
                                "General Mod Configuration Options."))
                        .option(Option.<Color>createBuilder()
                                .name(Text.literal("Name tag foreground color"))
                                .description(OptionDescription.of(Text.literal(
                                        "Controls the foreground color of the name tag. Note: Unless team color overwrite is turned on, only the alpha component will be used for team colored names.")))
                                .binding(NameConfig.defaultForegroundColor, () -> NameConfig.foregroundColor,
                                        newVal -> NameConfig.foregroundColor = newVal)
                                .controller((color) -> ColorControllerBuilder.create(color).allowAlpha(true))
                                .build())
                        .option(Option.<Color>createBuilder()
                                .name(Text.literal("Name tag background color"))
                                .description(OptionDescription.of(Text.literal(
                                        "Controls the background color of the name tag. Note: Non black and white backgrounds may make team-colored names unreadable.")))
                                .binding(NameConfig.defaultBackgroundColor, () -> NameConfig.backgroundColor,
                                        newVal -> NameConfig.backgroundColor = newVal)
                                .controller((color) -> ColorControllerBuilder.create(color).allowAlpha(true))
                                .build())
                        .option(Option.<Boolean>createBuilder()
                                .name(Text.literal("Fullbright names"))
                                .description(OptionDescription.of(Text.literal(
                                        "Names are no longer affected by the current light level.")))
                                .binding(NameConfig.defaultEnableFullbrightNames,
                                        () -> NameConfig.enableFullbrightNames,
                                        newVal -> NameConfig.enableFullbrightNames = newVal)
                                .controller(TickBoxControllerBuilder::create)
                                .build())
                        .option(Option.<Float>createBuilder()
                                .name(Text.literal("Distant name scaling"))
                                .description(OptionDescription.of(Text.literal(
                                        "Names are scaled, so distant names are still readable. At 0.0 names are not scaled at all and remain the same size in relation to the world. At 1.0 names are scaled so they remain the same size relative to the game window/screen.")))
                                .binding(NameConfig.defaultDistantNameScaleExponent,
                                        () -> NameConfig.distantNameScaleExponent,
                                        newVal -> NameConfig.distantNameScaleExponent = newVal)
                                .controller((option) -> FloatSliderControllerBuilder.create(option).range(0.0f, 1.0f)
                                        .step(0.01f))
                                .build())
                        .option(Option.<Boolean>createBuilder()
                                .name(Text.literal("Render names at any distance"))
                                .description(OptionDescription.of(Text.literal(
                                        "Names no longer disappear at a distance of 64 blocks. This is only recommended with 'Distant name scaling', otherwise names won't be legible.")))
                                .binding(NameConfig.defaultDisableDistanceCheck, () -> NameConfig.disableDistanceCheck,
                                        newVal -> NameConfig.disableDistanceCheck = newVal)
                                .controller(TickBoxControllerBuilder::create)
                                .build())
                        .option(Option.<Boolean>createBuilder()
                                .name(Text.literal("Disable team colors"))
                                .description(OptionDescription.of(Text.literal(
                                        "Names are no longer colored in their teams color. This guarantees that the selected foreground color will always be used.")))
                                .binding(NameConfig.defaultDisableTeamColors, () -> NameConfig.disableTeamColors,
                                        newVal -> NameConfig.disableTeamColors = newVal)
                                .controller(TickBoxControllerBuilder::create)
                                .build())
                        .build())
                .save(NameConfig.HANDLER::save)
                .build()
                .generateScreen(parentScreen);
    }
}