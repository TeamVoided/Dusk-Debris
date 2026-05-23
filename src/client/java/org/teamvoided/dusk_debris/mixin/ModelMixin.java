package org.teamvoided.dusk_debris.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Model.class)
public class ModelMixin {
    @ModifyReturnValue(method = "renderType", at = @At("RETURN"))
    private RenderType run(RenderType original, ResourceLocation texture) {
        return original;
//        return DuskRenderTypes.STATUE.apply(texture, false);
    }
}
