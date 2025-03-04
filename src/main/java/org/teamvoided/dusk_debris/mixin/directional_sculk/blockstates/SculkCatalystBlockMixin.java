package org.teamvoided.dusk_debris.mixin.directional_sculk.blockstates;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.sculk.SculkCatalystBlock;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.teamvoided.dusk_debris.block.mixin.SculkDirectionalStuff;

@Mixin(SculkCatalystBlock.class)
public class SculkCatalystBlockMixin extends Block {

    public SculkCatalystBlockMixin(Settings settings) {
        super(settings);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    public void addDefaultState(Settings settings, CallbackInfo ci) {
        this.setDefaultState(this.getDefaultState().with(Properties.FACING, Direction.UP));
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return SculkDirectionalStuff.getPlacementState(super.getPlacementState(ctx), ctx);
    }

    @Override
    protected BlockState rotate(BlockState state, BlockRotation rotation) {
        return SculkDirectionalStuff.spin(state, rotation);
    }

    @Override
    protected BlockState mirror(BlockState state, BlockMirror mirror) {
        return SculkDirectionalStuff.spin(state, mirror);
    }

    @Inject(method = "appendProperties", at = @At("TAIL"))
    public void addDirectionalProperties(StateManager.Builder<Block, BlockState> builder, CallbackInfo ci) {
        builder.add(Properties.FACING);
    }
}