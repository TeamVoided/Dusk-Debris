package org.teamvoided.dusk_debris.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.decoration.painting.PaintingEntity;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistrySetBuilder;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Iterator;

import static org.teamvoided.dusk_debris.DuskDebris.log;

@Debug(export = true)
@Mixin(RegistrySetBuilder.BuildState.class)
public class RegistrySetBuilderMixin {
    @Shadow @Final private RegistrySetBuilder.UniversalLookup lookup;

    @Inject(method = "reportUnreferencedKeys", at = @At("HEAD"), cancellable = true)
    void x(CallbackInfo ci) {
//        Iterator var1 = this.lookup.holders.keySet().iterator();
//        while(var1.hasNext()) {
//            RegistryKey<Object> registryKey = (RegistryKey)var1.next();
//            var x = registryKey == null ? "null" : registryKey.toString();
//            log.info(x);
//        }

        ci.cancel();
    }
}
