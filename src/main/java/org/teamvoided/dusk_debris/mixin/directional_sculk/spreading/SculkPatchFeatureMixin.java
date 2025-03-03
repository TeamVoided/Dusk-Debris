package org.teamvoided.dusk_debris.mixin.directional_sculk.spreading;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.int_provider.ConstantIntProvider;
import net.minecraft.util.math.int_provider.IntProvider;
import net.minecraft.util.random.RandomGenerator;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.SculkPatchFeature;
import net.minecraft.world.gen.feature.SculkPatchFeatureConfig;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.teamvoided.dusk_debris.block.not_blocks.SculkDirectionalStuff;

@Debug(export = true)
@Mixin(SculkPatchFeature.class)
public class SculkPatchFeatureMixin {
    @Redirect(method = "place", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/StructureWorldAccess;getBlockState(Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/BlockState;", ordinal = 0))
    private BlockState extraGrowthCatalyst(StructureWorldAccess structureWorldAccess, BlockPos blockPos, @Local RandomGenerator random) {
        var retrn = SculkDirectionalStuff.extraGrowthCatalyst(structureWorldAccess, blockPos);
        if (!retrn) {
            return structureWorldAccess.getBlockState(blockPos);
        } else {
            return Blocks.AIR.getDefaultState();
        }
    }

    @Redirect(method = "place", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/gen/feature/SculkPatchFeatureConfig;extraRareGrowths()Lnet/minecraft/util/math/int_provider/IntProvider;"))
    private IntProvider extraGrowthShrieker(SculkPatchFeatureConfig config, @Local StructureWorldAccess structureWorldAccess, @Local RandomGenerator random, @Local(ordinal = 0) BlockPos blockPos) {
        int k = config.extraRareGrowths().get(random);
        for (int l = 0; l < k; ++l) {
            SculkDirectionalStuff.extraGrowthShrieker(structureWorldAccess, random, blockPos);
        }
        return ConstantIntProvider.create(-10);
    }
}