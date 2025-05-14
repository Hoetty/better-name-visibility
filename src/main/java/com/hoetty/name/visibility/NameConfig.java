package com.hoetty.name.visibility;

import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.SerialEntry;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;
import net.fabricmc.loader.api.FabricLoader;
import java.awt.Color;
import net.minecraft.util.Identifier;

/**
 * Config for this mod.
 * Controlled by the ModMenuIntegration.
 */
public class NameConfig {
    /**
     * Serializer/Deserializer to save and load the config file.
     * The config is first loaded on launch and saved by the config screen.
     */
    public static ConfigClassHandler<NameConfig> HANDLER = ConfigClassHandler.createBuilder(NameConfig.class)
            .id(Identifier.of("", "name_visibility"))
            .serializer(config -> GsonConfigSerializerBuilder.create(config)
                    .setPath(FabricLoader.getInstance().getConfigDir().resolve("name_visibility.json"))
                    .build())
            .build();

    /**
     * Controls the background color and opacity of nametags.
     */
    @SerialEntry
    public static Color backgroundColor = new Color(0, 0, 0, 60);

    /**
     * Controls the color and opacity of names.
     */
    @SerialEntry
    public static Color foregroundColor = Color.WHITE;

    /**
     * Controls whether names are affected by the current light level.
     */
    @SerialEntry
    public static boolean enableFullbrightNames = true;

    /**
     * Controls how much distant names are scaled up.
     * 0.0 -> not at all.
     * 1.0 -> constant size on the screen.
     */
    @SerialEntry
    public static float distantNameScaleExponent = 0.2f;

    /**
     * Normally names only render for 64 blocks.
     * This can disable that limit.
     */
    @SerialEntry
    public static boolean disableDistanceCheck = false;

    /**
     * Normally team colors overwrite the foreground color (except alpha).
     * Disabling team colors enforces uniform use of the defined foreground color.
     */
    @SerialEntry
    public static boolean disableTeamColors = false;
}
