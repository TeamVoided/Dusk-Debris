package org.teamvoided.dusk_debris.mixin.snow;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SnowLayerBlock;
import net.minecraft.world.level.block.SpreadingSnowyDirtBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Debug(export = true)
@Mixin(SpreadingSnowyDirtBlock.class)
public class SpreadingSnowyDirtBlockMixin {
    @Inject(method = "canBeGrass", at = @At("HEAD"), cancellable = true)
    private static void gamerule(BlockState state, LevelReader world, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        if (world instanceof ServerLevel serverWorld){
            int snowHeight =  serverWorld.getGameRules().getInt(GameRules.RULE_SNOW_ACCUMULATION_HEIGHT);
            BlockPos posUp = pos.above();
            BlockState stateUp = world.getBlockState(posUp);
            if (stateUp.is(Blocks.SNOW) && stateUp.getValue(SnowLayerBlock.LAYERS) <= snowHeight) {
                cir.setReturnValue(true);
            }
        }
    }
}
