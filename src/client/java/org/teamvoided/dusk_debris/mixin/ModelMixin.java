package org.teamvoided.dusk_debris.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.client.model.Model;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Model.class)
public class ModelMixin {
    @ModifyReturnValue(method = "getLayer", at = @At("RETURN"))
    private RenderLayer run(RenderLayer original, Identifier texture) {
        return original;
//        return DuskRenderTypes.STATUE.apply(texture, false);
    }
}
