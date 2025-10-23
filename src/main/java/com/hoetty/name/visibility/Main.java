package com.hoetty.name.visibility;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.option.KeyBinding.Category;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main implements ModInitializer {

    private static KeyBinding ToggleNamesKey = new KeyBinding(
        "key.name_visibility.toggleNames",
        GLFW.GLFW_KEY_RIGHT_BRACKET,
        Category.MISC
    );

    public static boolean NamesToggled = true;
    public static final Logger LOGGER = LoggerFactory.getLogger(
        "name_visibility"
    );

    /**
     * Mod Entrypoint.
     * Registers a keybinding for toggling the mod and loads the config.
     */
    @Override
    public void onInitialize() {
        NameConfig.HANDLER.load();

        KeyBindingHelper.registerKeyBinding(ToggleNamesKey);

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (ToggleNamesKey.wasPressed()) {
                if (NamesToggled) {
                    NamesToggled = false;

                    client.player.sendMessage(
                        Text.of("Better Name Visibility §cOFF"),
                        true
                    );
                } else {
                    NamesToggled = true;

                    client.player.sendMessage(
                        Text.of("Better Name Visibility §aON"),
                        true
                    );
                }
            }
        });
    }
}
