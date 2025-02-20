package org.teamvoided.dusk_debris.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SnowBlock;
import net.minecraft.block.SpreadableBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameRules;
import net.minecraft.world.WorldView;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Debug(export = true)
@Mixin(SpreadableBlock.class)
public class SpreadableBlockMixin {
    @Inject(method = "canSurvive", at = @At("HEAD"), cancellable = true)
    private static void gamerule(BlockState state, WorldView world, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        if (world instanceof ServerWorld serverWorld){
            int snowHeight =  serverWorld.getGameRules().getIntValue(GameRules.SNOW_ACCUMULATION_HEIGHT);
            BlockPos posUp = pos.up();
            BlockState stateUp = world.getBlockState(posUp);
            if (stateUp.isOf(Blocks.SNOW) && stateUp.get(SnowBlock.LAYERS) <= snowHeight) {
                cir.setReturnValue(true);
            }
        }
    }
}
