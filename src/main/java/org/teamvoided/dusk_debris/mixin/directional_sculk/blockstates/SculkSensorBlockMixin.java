package org.teamvoided.dusk_debris.mixin.directional_sculk.blockstates;

import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.SculkSensorBlockEntity;
import net.minecraft.block.entity.SculkShriekerBlockEntity;
import net.minecraft.block.enums.SculkSensorPhase;
import net.minecraft.block.sculk.SculkSensorBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.particle.DustColorTransitionParticleEffect;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.random.RandomGenerator;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.teamvoided.dusk_debris.block.not_blocks.SculkDirectionalStuff;

import static net.minecraft.block.sculk.SculkSensorBlock.isInactive;
import static org.teamvoided.dusk_debris.util.UtilsHelperFunctionsKt.rotateVoxelShape;
import static org.teamvoided.dusk_debris.util.UtilsHelperFunctionsKt.toVec3d;

@Mixin(SculkSensorBlock.class)
public class SculkSensorBlockMixin extends Block implements Waterloggable {

    public SculkSensorBlockMixin(Settings settings) {
        super(settings);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    public void addDefaultState(Settings settings, CallbackInfo ci) {
        if (SculkDirectionalStuff.isNotCalibrated(this.asBlock())) {
            this.setDefaultState(this.getDefaultState().with(Properties.FACING, Direction.UP));
        }
    }

    @Inject(method = "onSteppedOn", at = @At("HEAD"), cancellable = true)
    public void onSteppedOnIfUp(World world, BlockPos pos, BlockState state, Entity entity, CallbackInfo ci) {
        if (SculkDirectionalStuff.isNotUpCalibrated(state)) {
            super.onSteppedOn(world, pos, state, entity);
            ci.cancel();
        }
    }

    @Override
    protected void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        if (SculkDirectionalStuff.isNotUpCalibrated(state) && !world.isClient() && isInactive(state) && entity.getType() != EntityType.WARDEN && SculkDirectionalStuff.noCreativeFlightAnnoyance(entity)) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof SculkSensorBlockEntity sculkSensorBlockEntity) {
                if (world instanceof ServerWorld serverWorld) {
                    if (sculkSensorBlockEntity.getVibrationCallback().accepts(serverWorld, pos, GameEvent.STEP, GameEvent.Context.create(state))) {
                        sculkSensorBlockEntity.getListener().forceScheduleVibration(serverWorld, GameEvent.STEP, GameEvent.Context.create(entity), entity.getPos());
                    }
                }
            }
        }
        super.onEntityCollision(state, world, pos, entity);
    }

    @Inject(method = "getStrongRedstonePower", at = @At("HEAD"), cancellable = true)
    public void addDirectionalRedstone(BlockState state, BlockView world, BlockPos pos, Direction direction, CallbackInfoReturnable<Integer> cir) {
        if (SculkDirectionalStuff.isNotCalibrated(this.asBlock())) {
            var facing = state.get(Properties.FACING);
            if (facing != Direction.UP)
                cir.setReturnValue(direction == facing ? state.getWeakRedstonePower(world, pos, direction) : 0);
        }
    }

    @Inject(method = "getOutlineShape", at = @At("HEAD"), cancellable = true)
    public void getDirectionalShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context, CallbackInfoReturnable<VoxelShape> cir) {
        if (SculkDirectionalStuff.isNotCalibrated(this.asBlock())) {
            SculkDirectionalStuff.getDirectionalSlabShape(state, cir);
        }
    }

    @Inject(method = "randomDisplayTick", at = @At("HEAD"), cancellable = true)
    public void addDirectionalRandomDisplayTick(BlockState state, World world, BlockPos pos, RandomGenerator random, CallbackInfo ci) {
        if (SculkDirectionalStuff.isNotCalibrated(this.asBlock())) {
            var facing = state.get(Properties.FACING);
            if (facing != Direction.UP) {
                if (SculkSensorBlock.getPhase(state) == SculkSensorPhase.ACTIVE) {
                    Direction direction = Direction.random(random);
                    if (direction != facing && direction != facing.getOpposite()) {
                        Vec3d posFacing = toVec3d(pos);
                        Vec3d velFacing = Vec3d.ZERO;
                        double x = 0.5 + (direction.getOffsetX() == 0 ? 0.5 - random.nextDouble() : (double) direction.getOffsetX() * 0.6);
                        double y = 0.25;
                        double z = 0.5 + (direction.getOffsetZ() == 0 ? 0.5 - random.nextDouble() : (double) direction.getOffsetZ() * 0.6);
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
                                DustColorTransitionParticleEffect.DEFAULT,
                                posFacing.x, posFacing.y, posFacing.z,
                                velFacing.x, velFacing.y, velFacing.z
                        );
                    }
                }
                ci.cancel();
            }
        }
    }

    @Inject(method = "getPlacementState", at = @At("RETURN"), cancellable = true)
    public void addDirectionalPlacement(ItemPlacementContext ctx, CallbackInfoReturnable<BlockState> cir) {
        if (SculkDirectionalStuff.isNotCalibrated(this.asBlock())) {
            cir.setReturnValue(SculkDirectionalStuff.getPlacementState(cir.getReturnValue(), ctx));
        }
    }

    @Override
    protected BlockState rotate(BlockState state, BlockRotation rotation) {
        if (SculkDirectionalStuff.isNotCalibrated(this.asBlock())) {
            return SculkDirectionalStuff.spin(state, rotation);
        } else {
            return super.rotate(state, rotation);
        }
    }

    @Override
    protected BlockState mirror(BlockState state, BlockMirror mirror) {
        if (SculkDirectionalStuff.isNotCalibrated(this.asBlock())) {
            return SculkDirectionalStuff.spin(state, mirror);
        } else {
            return super.mirror(state, mirror);
        }
    }

    @Inject(method = "appendProperties", at = @At("TAIL"))
    public void addDirectionalProperties(StateManager.Builder<Block, BlockState> builder, CallbackInfo ci) {
        if (SculkDirectionalStuff.isNotCalibrated(this.asBlock())) {
            builder.add(Properties.FACING);
        }
    }
}