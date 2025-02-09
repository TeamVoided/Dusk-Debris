package org.teamvoided.dusk_debris.mixin;

import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.random.PositionalRandomFactory;
import net.minecraft.world.gen.DensityFunctions;
import net.minecraft.world.gen.chunk.AquiferSampler;
import net.minecraft.world.gen.chunk.ChunkNoiseSampler;
import net.minecraft.world.gen.noise.NoiseRouter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.teamvoided.dusk_debris.world.gen.CustomAquiferSampler;

import java.util.Objects;

@Mixin(ChunkNoiseSampler.class)
public class ChunkNoiseSamplerMixin {
    @Redirect(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/gen/chunk/AquiferSampler;aquifer(Lnet/minecraft/world/gen/chunk/ChunkNoiseSampler;Lnet/minecraft/util/math/ChunkPos;Lnet/minecraft/world/gen/noise/NoiseRouter;Lnet/minecraft/util/random/PositionalRandomFactory;IILnet/minecraft/world/gen/chunk/AquiferSampler$FluidPicker;)Lnet/minecraft/world/gen/chunk/AquiferSampler;"))
    private AquiferSampler sampler(ChunkNoiseSampler chunkNoiseSampler, ChunkPos pos, NoiseRouter noiseRouter, PositionalRandomFactory positionalRandomFactory, int startY, int height, AquiferSampler.FluidPicker globalFluidPicker) {
        if (Objects.equals(noiseRouter.lavaNoise(), DensityFunctions.constant(6.0)))
            return CustomAquiferSampler.Companion.netherSeaLevel(chunkNoiseSampler, pos, noiseRouter, positionalRandomFactory, startY, height, globalFluidPicker);
        else
            return AquiferSampler.aquifer(chunkNoiseSampler, pos, noiseRouter, positionalRandomFactory, startY, height, globalFluidPicker);
    }
}
