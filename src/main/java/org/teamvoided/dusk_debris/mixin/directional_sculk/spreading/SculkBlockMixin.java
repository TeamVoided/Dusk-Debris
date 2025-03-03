package org.teamvoided.dusk_debris.mixin.directional_sculk.spreading;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.sculk.SculkBehavior;
import net.minecraft.block.sculk.SculkBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.random.RandomGenerator;
import net.minecraft.world.WorldAccess;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.teamvoided.dusk_debris.block.not_blocks.SculkDirectionalStuff;

import static net.minecraft.Bootstrap.println;
@Debug(export = true)
@Mixin(SculkBlock.class)
public class SculkBlockMixin {
    @Redirect(method = "tryUseCharge", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/random/RandomGenerator;nextInt(I)I", ordinal = 1))
    private int randomGrowth(RandomGenerator random, int j, @Local SculkBehavior.ChargeCursor charge) {
        return charge.getCharge() + 1;
    }

    @Redirect(method = "tryUseCharge", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/sculk/SculkBlock;canSpreadTo(Lnet/minecraft/world/WorldAccess;Lnet/minecraft/util/math/BlockPos;)Z"))
    private boolean canGrow(WorldAccess world, BlockPos pos, @Local SculkBehavior.ChargeCursor charge, @Local SculkBehavior sculkChargeHandler, @Local RandomGenerator random) {
        return SculkDirectionalStuff.tryUseChargeSpreadRewrite(world, pos, charge, sculkChargeHandler, random, sculkChargeHandler.isWorldGen());
    }

    @Redirect(method = "tryUseCharge", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/sculk/SculkBlock;getRandomGrowthState(Lnet/minecraft/world/WorldAccess;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/random/RandomGenerator;Z)Lnet/minecraft/block/BlockState;"))
    private BlockState theStopper(SculkBlock instance, WorldAccess world, BlockPos pos, RandomGenerator random, boolean randomize) {
        return Blocks.GLOWSTONE.getDefaultState(); //instance.getRandomGrowthState(world, pos, random, randomize);
    }
}