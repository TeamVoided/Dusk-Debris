package org.teamvoided.dusk_debris.mixin.directional_sculk.blockstates;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustColorTransitionOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.SculkSensorBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.SculkSensorPhase;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.teamvoided.dusk_debris.block.mixin.SculkDirectionalStuff;

import static net.minecraft.world.level.block.SculkSensorBlock.canActivate;
import static org.teamvoided.dusk_debris.util.UtilsHelperFunctionsKt.toVec3d;

@Mixin(SculkSensorBlock.class)
public class SculkSensorBlockMixin extends Block implements SimpleWaterloggedBlock {

    public SculkSensorBlockMixin(Properties settings) {
        super(settings);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    public void addDefaultState(Properties settings, CallbackInfo ci) {
        if (SculkDirectionalStuff.isNotCalibrated(this.asBlock())) {
            this.registerDefaultState(this.defaultBlockState().setValue(BlockStateProperties.FACING, Direction.UP));
        }
    }

    @Inject(method = "stepOn", at = @At("HEAD"), cancellable = true)
    public void onSteppedOnIfUp(Level world, BlockPos pos, BlockState state, Entity entity, CallbackInfo ci) {
        if (SculkDirectionalStuff.isNotUpCalibrated(state)) {
            super.stepOn(world, pos, state, entity);
            ci.cancel();
        }
    }

    @Override
    protected void entityInside(BlockState state, Level world, BlockPos pos, Entity entity) {
        if (SculkDirectionalStuff.isNotUpCalibrated(state) && !world.isClientSide() && canActivate(state) && entity.getType() != EntityType.WARDEN && SculkDirectionalStuff.noCreativeFlightAnnoyance(entity)) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof SculkSensorBlockEntity sculkSensorBlockEntity) {
                if (world instanceof ServerLevel serverWorld) {
                    if (sculkSensorBlockEntity.getVibrationUser().canReceiveVibration(serverWorld, pos, GameEvent.STEP, GameEvent.Context.of(state))) {
                        sculkSensorBlockEntity.getListener().forceScheduleVibration(serverWorld, GameEvent.STEP, GameEvent.Context.of(entity), entity.position());
                    }
                }
            }
        }
        super.entityInside(state, world, pos, entity);
    }

    @Inject(method = "getDirectSignal", at = @At("HEAD"), cancellable = true)
    public void addDirectionalRedstone(BlockState state, BlockGetter world, BlockPos pos, Direction direction, CallbackInfoReturnable<Integer> cir) {
        if (SculkDirectionalStuff.isNotCalibrated(this.asBlock())) {
            var facing = state.getValue(BlockStateProperties.FACING);
            if (facing != Direction.UP)
                cir.setReturnValue(direction == facing ? state.getSignal(world, pos, direction) : 0);
        }
    }

    @Inject(method = "getShape", at = @At("HEAD"), cancellable = true)
    public void getDirectionalShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context, CallbackInfoReturnable<VoxelShape> cir) {
        if (SculkDirectionalStuff.isNotCalibrated(this.asBlock())) {
            SculkDirectionalStuff.getDirectionalSlabShape(state, cir);
        }
    }

    @Inject(method = "animateTick", at = @At("HEAD"), cancellable = true)
    public void addDirectionalRandomDisplayTick(BlockState state, Level world, BlockPos pos, RandomSource random, CallbackInfo ci) {
        if (SculkDirectionalStuff.isNotCalibrated(this.asBlock())) {
            var facing = state.getValue(BlockStateProperties.FACING);
            if (facing != Direction.UP) {
                if (SculkSensorBlock.getPhase(state) == SculkSensorPhase.ACTIVE) {
                    Direction direction = Direction.getRandom(random);
                    if (direction != facing && direction != facing.getOpposite()) {
                        Vec3 posFacing = toVec3d(pos);
                        Vec3 velFacing = Vec3.ZERO;
                        double x = 0.5 + (direction.getStepX() == 0 ? 0.5 - random.nextDouble() : (double) direction.getStepX() * 0.6);
                        double y = 0.25;
                        double z = 0.5 + (direction.getStepZ() == 0 ? 0.5 - random.nextDouble() : (double) direction.getStepZ() * 0.6);
                        double yVel = (double) random.nextFloat() * 0.04;
                        switch (facing) {
                            case Direction.DOWN:
                                posFacing = posFacing.add(x, -y, z);
                                velFacing = velFacing.add(0.0, -yVel, 0.0);
                                break;
                            case Direction.NORTH:
                                posFacing = posFacing.add(x, z, -y);
                                velFacing = velFacing.add(0.0, 0.0, -yVel);
                                break;
                            case Direction.SOUTH:
                                posFacing = posFacing.add(x, z, y);
                                velFacing = velFacing.add(0.0, 0.0, yVel);
                                break;
                            case Direction.WEST:
                                posFacing = posFacing.add(-y, x, z);
                                velFacing = velFacing.add(-yVel, 0.0, 0.0);
                                break;
                            case Direction.EAST:
                                posFacing = posFacing.add(y, x, z);
                                velFacing = velFacing.add(yVel, 0.0, 0.0);
                                break;
                        }
                        world.addParticle(
                                DustColorTransitionOptions.SCULK_TO_REDSTONE,
                                posFacing.x, posFacing.y, posFacing.z,
                                velFacing.x, velFacing.y, velFacing.z
                        );
                    }
                }
                ci.cancel();
            }
        }
    }

    @Inject(method = "getStateForPlacement", at = @At("RETURN"), cancellable = true)
    public void addDirectionalPlacement(BlockPlaceContext ctx, CallbackInfoReturnable<BlockState> cir) {
        if (SculkDirectionalStuff.isNotCalibrated(this.asBlock())) {
            cir.setReturnValue(SculkDirectionalStuff.getPlacementState(cir.getReturnValue(), ctx));
        }
    }

    @Override
    protected BlockState rotate(BlockState state, Rotation rotation) {
        if (SculkDirectionalStuff.isNotCalibrated(this.asBlock())) {
            return SculkDirectionalStuff.spin(state, rotation);
        } else {
            return super.rotate(state, rotation);
        }
    }

    @Override
    protected BlockState mirror(BlockState state, Mirror mirror) {
        if (SculkDirectionalStuff.isNotCalibrated(this.asBlock())) {
            return SculkDirectionalStuff.spin(state, mirror);
        } else {
            return super.mirror(state, mirror);
        }
    }

    @Inject(method = "createBlockStateDefinition", at = @At("TAIL"))
    public void addDirectionalProperties(StateDefinition.Builder<Block, BlockState> builder, CallbackInfo ci) {
        if (SculkDirectionalStuff.isNotCalibrated(this.asBlock())) {
            builder.add(BlockStateProperties.FACING);
        }
    }
}