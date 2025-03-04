package org.teamvoided.dusk_debris.mixin.directional_sculk.spreading;

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
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.teamvoided.dusk_debris.block.mixin.SculkDirectionalStuff;

@Debug(export = true)
@Mixin(SculkBlock.class)
public class SculkBlockMixin {
    @Inject(method = "tryUseCharge", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/sculk/SculkBlock;canSpreadTo(Lnet/minecraft/world/WorldAccess;Lnet/minecraft/util/math/BlockPos;)Z"), cancellable = true)
    private void randomGrowth(SculkBehavior.ChargeCursor chargeCursor, WorldAccess world, BlockPos pos, RandomGenerator random, SculkBehavior sculkChargeHandler, boolean spread, CallbackInfoReturnable<Integer> cir) {
        int charge = chargeCursor.getCharge();
        int cost = sculkChargeHandler.getGrowthSpawnCost();
        SculkDirectionalStuff.tryUseChargeSpreadRewrite(world, chargeCursor.getPos(), charge, cost, random, sculkChargeHandler.isWorldGen());
        cir.setReturnValue(Math.max(0, charge - cost));
    }

    /* - But Here's the Stopper -*/

    @Redirect(method = "tryUseCharge", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/sculk/SculkBlock;getRandomGrowthState(Lnet/minecraft/world/WorldAccess;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/random/RandomGenerator;Z)Lnet/minecraft/block/BlockState;"))
    private BlockState theStopper(SculkBlock instance, WorldAccess world, BlockPos pos, RandomGenerator random, boolean randomize) {
        return Blocks.WHITE_STAINED_GLASS.getDefaultState(); //instance.getRandomGrowthState(world, pos, random, randomize);
    }
}