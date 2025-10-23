package com.hoetty.name.visibility.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.hoetty.name.visibility.Main;
import com.hoetty.name.visibility.NameConfig;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;

import net.minecraft.client.render.entity.EntityRenderer;

@Mixin(value = EntityRenderer.class, priority = 10000)
public class EntityRendererMixin {
    
    /**
     * Ensures the label does not get discarded during rendering, when further away then 64 blocks.
     */
    @ModifyExpressionValue(method = "updateRenderState", at = @At(value = "CONSTANT", args = "doubleValue=4096.0"))
    private double maxNametagDistance(double original) {
        return Main.NamesToggled && NameConfig.disableDistanceCheck ? Double.MAX_VALUE : original;
    }

}
