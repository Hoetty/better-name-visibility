package com.hoetty.name.visibility.mixin;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.hoetty.name.visibility.Main;
import com.hoetty.name.visibility.NameConfig;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.command.OrderedRenderCommandQueueImpl;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;

@Mixin(OrderedRenderCommandQueueImpl.LabelCommand.class)
public class LabelCommandMixin {
    
    /**
     * Fullbright light level.
     */
    private static final int FULLBRIGHT = LightmapTextureManager.pack(15, 15);

    /**
     * The (constant) font height used by the textrenderer.
     */
    private static final int fontHeight = new TextRenderer(null).fontHeight;


    /**
     * Renders the configured background color.
     */
    @Inject(method = "backgroundColor", at = @At("HEAD"), cancellable = true)
    private void backgroundColor(CallbackInfoReturnable<Integer> cir) {
        if (Main.NamesToggled) {
            cir.setReturnValue(NameConfig.backgroundColor.getRGB());
        }
    }

    /**
     * Renders the configured foreground color.
     */
    @Inject(method = "color", at = @At("HEAD"), cancellable = true)
    private void color(CallbackInfoReturnable<Integer> cir) {
        if (Main.NamesToggled) {
            cir.setReturnValue(NameConfig.foregroundColor.getRGB());
        }
    }

    /**
     * Makes the label always fully bright
     */
    @Inject(method = "lightCoords", at = @At("HEAD"), cancellable = true)
    private void lightCoords(CallbackInfoReturnable<Integer> cir) {
        if (Main.NamesToggled && NameConfig.enableFullbrightNames) {
            cir.setReturnValue(FULLBRIGHT);
        }
    }

    @Shadow
    private Matrix4f matricesEntry;

    /**
     * Scales the the name tag between always the same screen size and always the same world size,
     * depending on the configured value.
     */
    @Inject(method = "matricesEntry", at = @At("HEAD"), cancellable = true)
    private void matricesEntry(CallbackInfoReturnable<Matrix4f> cir) {
        if (Main.NamesToggled && NameConfig.distantNameScaleExponent != 0) {
            // The farther away the name tag, the more it gets scaled.
            // The distance is stored in the last column of the matrix.
            float scaleFactor = (float) Math.pow((matricesEntry.getColumn(3, new Vector3f())).length(),
                    NameConfig.distantNameScaleExponent);

            Matrix4f scaledMatrix = new Matrix4f(matricesEntry);

            // The matrix is first shifted up, making the name remain above the entity.
            // Otherwise the name will cover the entity it describes,
            scaledMatrix.translate(0, fontHeight, 0).scale(scaleFactor).translate(0, -fontHeight, 0);

            cir.setReturnValue(scaledMatrix);
        }
    }

    @Shadow
    private Text text;

    /**
     * Clears all text color, that minecraft natively applies, if configured.
     */
    @Inject(method = "text", at = @At("HEAD"), cancellable = true)
    private void text(CallbackInfoReturnable<Text> cir) {
        if (Main.NamesToggled && NameConfig.disableTeamColors) {
            // The text should always be mutable.
            if (text instanceof MutableText mutableText) {
                Style style = mutableText.getStyle();

                // Clear the color, if present.
                if (style.getColor() != null) {
                    mutableText.setStyle(style.withColor((TextColor) null));
                }

                // Text is made of multiple fragments, and each of them has to be cleared.
                for (Text siblingText : text.getSiblings()) {
                    if (siblingText instanceof MutableText mutableSiblingText) {

                        style = mutableSiblingText.getStyle();
                        if (style.getColor() != null) {
                            mutableSiblingText.setStyle(style.withColor((TextColor) null));
                        }
                    }
                }
            }

            cir.setReturnValue(text);
        }
    }
}
