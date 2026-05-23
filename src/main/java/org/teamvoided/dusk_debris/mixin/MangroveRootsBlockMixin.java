package org.teamvoided.dusk_debris.mixin;

import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MangroveRootsBlock.class)
public class MangroveRootsBlockMixin extends Block implements SimpleWaterloggedBlock {

    public MangroveRootsBlockMixin(Properties settings) {
        super(settings);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    public void addDefaultState(BlockBehaviour.Properties settings, CallbackInfo ci) {
        this.registerDefaultState(this.defaultBlockState().setValue(BlockStateProperties.AXIS, Direction.Axis.Y));
    }

    @Inject(method = "skipRendering", at = @At("HEAD"), cancellable = true)
    public void addDirectionality(BlockState state, BlockState stateFrom, Direction direction, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(stateFrom.is(Blocks.MANGROVE_ROOTS) && direction.getAxis() == state.getValue(BlockStateProperties.AXIS));
    }

    @Inject(method = "getStateForPlacement", at = @At("RETURN"), cancellable = true)
    public void addDirectionalPlacement(BlockPlaceContext ctx, CallbackInfoReturnable<BlockState> cir) {
        var supr = cir.getReturnValue();
        if (supr == null) return;
        cir.setReturnValue(supr.setValue(BlockStateProperties.AXIS, ctx.getClickedFace().getAxis()));
    }

    @Override
    protected BlockState rotate(BlockState state, Rotation rotation) {
        return RotatedPillarBlock.rotatePillar(state, rotation);
    }

    @Inject(method = "createBlockStateDefinition", at = @At("TAIL"))
    public void addDirectionalSideInvisible(StateDefinition.Builder<Block, BlockState> builder, CallbackInfo ci) {
        builder.add(BlockStateProperties.AXIS);
    }
}