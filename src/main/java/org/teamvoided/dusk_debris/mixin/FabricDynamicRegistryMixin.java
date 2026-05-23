package org.teamvoided.dusk_debris.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.resources.RegistryDataLoader;
import org.jetbrains.annotations.Unmodifiable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Mixin(FabricDynamicRegistryProvider.Entries.class)
public class FabricDynamicRegistryMixin {
    @ModifyExpressionValue(at = @At(value = "INVOKE", target = "Lnet/fabricmc/fabric/api/event/registry/DynamicRegistries;getDynamicRegistries()Ljava/util/List;"), method = "<init>")
    private @Unmodifiable List<RegistryDataLoader.RegistryData<?>> init(@Unmodifiable List<RegistryDataLoader.RegistryData<?>> original) {
        return Stream.concat(original.stream(), RegistryDataLoader.DIMENSION_REGISTRIES.stream()).collect(Collectors.toList());
    }
}
