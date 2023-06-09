package com.hoetty.name.visibility;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main implements ModInitializer {

	private static KeyBinding toggleNamesKey;
	public static boolean NamesToggled = true;
	public static final Logger LOGGER = LoggerFactory.getLogger("name_visibility");

	@Override
	public void onInitialize() {
		toggleNamesKey = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.name_visibility.toggleNames", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_RIGHT_BRACKET,"category.name_visibility.keybindcategory"));
		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			while (toggleNamesKey.wasPressed()){
				if (NamesToggled){
					NamesToggled = false;
					client.player.sendMessage(Text.of("Better Name Visibility §cOFF"), true);
				}else{
					NamesToggled = true;
					client.player.sendMessage(Text.of("Better Name Visibility §aON"), true);
				}
			}
		});
	}
}
