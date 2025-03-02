package org.teamvoided.dusk_debris.mixin;

import net.minecraft.block.*;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.state.property.Property;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MangroveRootsBlock.class)
public class MangroveRootsBlockMixin extends Block implements Waterloggable {

    public MangroveRootsBlockMixin(Settings settings) {
        super(settings);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    public void addDefaultState(AbstractBlock.Settings settings, CallbackInfo ci) {
        this.setDefaultState(this.getDefaultState().with(Properties.AXIS, Direction.Axis.Y));
    }

    @Inject(method = "isSideInvisible", at = @At("HEAD"), cancellable = true)
    public void addDirectionality(BlockState state, BlockState stateFrom, Direction direction, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(stateFrom.isOf(Blocks.MANGROVE_ROOTS) && direction.getAxis() == state.get(Properties.AXIS));
    }

    @Inject(method = "getPlacementState", at = @At("RETURN"), cancellable = true)
    public void addDirectionalPlacement(ItemPlacementContext ctx, CallbackInfoReturnable<BlockState> cir) {
        var supr = cir.getReturnValue();
        if (supr == null) return;
        cir.setReturnValue(supr.with(Properties.AXIS, ctx.getSide().getAxis()));
    }

    @Override
    protected BlockState rotate(BlockState state, BlockRotation rotation) {
        return PillarBlock.changeRotation(state, rotation);
    }

    @Inject(method = "appendProperties", at = @At("TAIL"))
    public void addDirectionalSideInvisible(StateManager.Builder<Block, BlockState> builder, CallbackInfo ci) {
        builder.add(Properties.AXIS);
    }
}