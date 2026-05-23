package org.teamvoided.dusk_debris.mixin;

import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.teamvoided.dusk_debris.world.gen.CustomAquiferSampler;

import java.util.Objects;

@Mixin(NoiseChunk.class)
public class NoiseChunkMixin {
    @Redirect(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/levelgen/Aquifer;create(Lnet/minecraft/world/level/levelgen/NoiseChunk;Lnet/minecraft/world/level/ChunkPos;Lnet/minecraft/world/level/levelgen/NoiseRouter;Lnet/minecraft/world/level/levelgen/PositionalRandomFactory;IILnet/minecraft/world/level/levelgen/Aquifer$FluidPicker;)Lnet/minecraft/world/level/levelgen/Aquifer;"))
    private Aquifer sampler(NoiseChunk chunkNoiseSampler, ChunkPos pos, NoiseRouter noiseRouter, PositionalRandomFactory positionalRandomFactory, int startY, int height, Aquifer.FluidPicker globalFluidPicker) {
        if (Objects.equals(noiseRouter.lavaNoise(), DensityFunctions.constant(6.0)))
            return CustomAquiferSampler.Companion.netherSeaLevel(chunkNoiseSampler, pos, noiseRouter, positionalRandomFactory, startY, height, globalFluidPicker);
        else
            return Aquifer.create(chunkNoiseSampler, pos, noiseRouter, positionalRandomFactory, startY, height, globalFluidPicker);
    }
}
