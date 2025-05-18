package com.hoetty.name.visibility.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import com.hoetty.name.visibility.Main;
import com.hoetty.name.visibility.NameConfig;

import net.minecraft.client.render.entity.ArmorStandEntityRenderer;

@Mixin(ArmorStandEntityRenderer.class)
public class ArmorStandEntityRendererMixin {

    /**
     * Until 1.21.2, the game checks the name tag render limit twice.
     * This disables it for all Armorstands.
     */
    @ModifyVariable(method = "hasLabel", at = @At("STORE"), ordinal = 0)
    private double maxNameDistance(double original) {
        if (Main.NamesToggled && NameConfig.disableDistanceCheck) {
            return 0;
        } else {
            return original;
        }
    }

}
