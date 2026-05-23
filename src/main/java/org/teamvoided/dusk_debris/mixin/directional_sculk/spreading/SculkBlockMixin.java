package org.teamvoided.dusk_debris.mixin.directional_sculk.spreading;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SculkBlock;
import net.minecraft.world.level.block.SculkSpreader;
import net.minecraft.world.level.block.state.BlockState;
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
    @Inject(method = "attemptUseCharge", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/SculkBlock;canPlaceGrowth(Lnet/minecraft/world/level/LevelAccessor;Lnet/minecraft/core/BlockPos;)Z"), cancellable = true)
    private void randomGrowth(SculkSpreader.ChargeCursor chargeCursor, LevelAccessor world, BlockPos pos, RandomSource random, SculkSpreader sculkChargeHandler, boolean spread, CallbackInfoReturnable<Integer> cir) {
        int charge = chargeCursor.getCharge();
        int cost = sculkChargeHandler.growthSpawnCost();
        SculkDirectionalStuff.tryUseChargeSpreadRewrite(world, chargeCursor.getPos(), charge, cost, random, sculkChargeHandler.isWorldGeneration());
        cir.setReturnValue(Math.max(0, charge - cost));
    }

    /* - But Here's the Stopper -*/

    @Redirect(method = "attemptUseCharge", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/SculkBlock;getRandomGrowthState(Lnet/minecraft/world/level/LevelAccessor;Lnet/minecraft/core/BlockPos;Lnet/minecraft/util/RandomSource;Z)Lnet/minecraft/world/level/block/state/BlockState;"))
    private BlockState theStopper(SculkBlock instance, LevelAccessor world, BlockPos pos, RandomSource random, boolean randomize) {
        return Blocks.WHITE_STAINED_GLASS.defaultBlockState(); //instance.getRandomGrowthState(world, pos, random, randomize);
    }
}