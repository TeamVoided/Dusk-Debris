package org.teamvoided.dusk_debris.mixin.directional_sculk.blockstates;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.SculkShriekerBlockEntity;
import net.minecraft.block.sculk.SculkShriekerBlock;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.teamvoided.dusk_debris.block.mixin.SculkDirectionalStuff;

@Mixin(SculkShriekerBlock.class)
public class SculkShriekerBlockMixin extends Block {

    public SculkShriekerBlockMixin(Settings settings) {
        super(settings);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    public void addDefaultState(Settings settings, CallbackInfo ci) {
        this.setDefaultState(this.getDefaultState().with(Properties.FACING, Direction.UP));
    }

    @Inject(method = "getCollisionShape", at = @At("HEAD"), cancellable = true)
    public void directionalCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context, CallbackInfoReturnable<VoxelShape> cir) {
        SculkDirectionalStuff.getDirectionalSlabShape(state, cir);
    }

    @Inject(method = "getCullingShape", at = @At("HEAD"), cancellable = true)
    public void directionalCullingShape(BlockState state, BlockView world, BlockPos pos, CallbackInfoReturnable<VoxelShape> cir) {
        SculkDirectionalStuff.getDirectionalSlabShape(state, cir);
    }

    @Inject(method = "onSteppedOn", at = @At("HEAD"), cancellable = true)
    public void onSteppedOnIfUp(World world, BlockPos pos, BlockState state, Entity entity, CallbackInfo ci) {
        if (SculkDirectionalStuff.isNotUp(state)) {
            super.onSteppedOn(world, pos, state, entity);
            ci.cancel();
        }
    }

    @Override
    protected void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        if (SculkDirectionalStuff.isNotUp(state) && world instanceof ServerWorld serverWorld) {
            ServerPlayerEntity serverPlayerEntity = SculkShriekerBlockEntity.findResponsiblePlayerFromEntity(entity);
            if (serverPlayerEntity != null && SculkDirectionalStuff.noCreativeFlightAnnoyance(serverPlayerEntity)) {
                serverWorld.getBlockEntity(pos, BlockEntityType.SCULK_SHRIEKER).ifPresent((blockEntity) -> blockEntity.shriek(serverWorld, serverPlayerEntity));
            }
        }
        super.onEntityCollision(state, world, pos, entity);
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