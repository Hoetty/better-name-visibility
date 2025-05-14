package com.hoetty.name.visibility.mixin;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import com.hoetty.name.visibility.Main;
import com.hoetty.name.visibility.NameConfig;

/**
 * This Mixin modifies the central EntityRenderer to change how name tags are
 * rendered.
 * 
 * If the mod is toggled it does the following:
 * - Set the text color
 * - Set the background color
 * - Enable fullbright names if enabled
 * - Strips team colors if enabled
 * - Scales distant names if enabled
 * - Removes the name tag distance limit if enabled
 */
@Mixin(EntityRenderer.class)
public abstract class NameTagRenderer {

    /**
     * Fullbright light level.
     */
    private static final int FULLBRIGHT = LightmapTextureManager.pack(15, 15);

    /**
     * A style that removes Color from text.
     */
    private static final Style NO_COLOR = Style.EMPTY.withColor(Colors.WHITE);

    @Shadow
    private TextRenderer textRenderer;

    /**
     * Main mixin for the render function. Functionality described above.
     */
    @ModifyArgs(method = "renderLabelIfPresent", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/font/TextRenderer;draw(Lnet/minecraft/text/Text;FFIZLorg/joml/Matrix4f;Lnet/minecraft/client/render/VertexConsumerProvider;Lnet/minecraft/client/font/TextRenderer$TextLayerType;II)I"))
    private void modify(Args args) {
        if (!Main.NamesToggled) {
            return;
        }

        args.set(3, NameConfig.foregroundColor.getRGB());
        args.set(8, NameConfig.backgroundColor.getRGB());
        args.set(9, NameConfig.enableFullbrightNames ? FULLBRIGHT : args.get(9));

        if (NameConfig.disableTeamColors) {
            Text text = args.get(0);

            // The text should always be mutable.
            if (text instanceof MutableText mutableText) {
                mutableText.fillStyle(NO_COLOR);

                // Text is made of multiple fragments, and each of them has to be cleared.
                for (Text siblingText : text.getSiblings()) {
                    if (siblingText instanceof MutableText mutableSiblingText) {
                        mutableSiblingText.fillStyle(NO_COLOR);
                    }
                }
            }

            args.set(0, text);
        }

        if (NameConfig.distantNameScaleExponent != 0) {
            Matrix4f matrix = args.get(5);

            // The farther away the name tag, the more it gets scaled.
            // The distance is stored in the last column of the matrix.
            float scaleFactor = (float) Math.pow((matrix.getColumn(3, new Vector3f())).length(),
                    NameConfig.distantNameScaleExponent);

            Matrix4f scaledMatrix = new Matrix4f(matrix);

            // The matrix is first shifted up, making the name remain above the entity.
            // Otherwise the name will cover the entity it describes,
            scaledMatrix.translate(0, textRenderer.fontHeight, 0).scale(scaleFactor).translate(0,
                    -textRenderer.fontHeight, 0);

            args.set(5, scaledMatrix);
        }
    }

    /**
     * The game state saves the name to be displayed.
     * Normally it writes null if the squared distance to the entity is > 4096.
     * This is equal to 64 blocks.
     * When enabled in the config, this limit is disabled.
     * This is only usefull in combination with scaling.
     */
    @ModifyConstant(method = "updateRenderState", constant = @Constant(doubleValue = 4096.0))
    private double maxNameDistance(double original) {
        if (Main.NamesToggled && NameConfig.disableDistanceCheck) {
            return Double.MAX_VALUE;
        } else {
            return original;
        }
    }
}