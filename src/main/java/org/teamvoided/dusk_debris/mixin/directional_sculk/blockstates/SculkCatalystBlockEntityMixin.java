package org.teamvoided.dusk_debris.mixin.directional_sculk.blockstates;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.SculkCatalystBlockEntity;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.gen.DensityFunctions;
import net.minecraft.world.gen.chunk.AquiferSampler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.teamvoided.dusk_debris.world.gen.CustomAquiferSampler;

import java.util.Objects;

import static org.teamvoided.dusk_debris.util.UtilsHelperFunctionsKt.toVec3d;

@Mixin(SculkCatalystBlockEntity.CatalystListener.class)
public class SculkCatalystBlockEntityMixin {
    @Redirect(method = "bloom", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;spawnParticles(Lnet/minecraft/particle/ParticleEffect;DDDIDDDD)I"))
    private <T extends ParticleEffect> int bloomParticles(ServerWorld world, T particle, double x, double y, double z, int count, double deltaX, double deltaY, double deltaZ, double speed, @Local BlockPos pos, @Local BlockState state) {
        var facing = state.get(Properties.FACING);
        if (facing != Direction.UP) {
            var posReal = pos.ofCenter();
            var delta = Vec3d.ZERO;
            switch (facing) {
                case Direction.DOWN:
                    posReal = posReal.add(0.0, - 0.65, 0.0);
                    delta = new Vec3d(deltaX, -deltaY, deltaZ);
                    break;
                case Direction.NORTH:
                    posReal = posReal.add(0.0, 0.0, - 0.65);
                    delta = new Vec3d(deltaX, deltaZ, -deltaY);
                    break;
                case Direction.SOUTH:
                    posReal = posReal.add(0.0, 0.0, 0.65);
                    delta = new Vec3d(deltaX, deltaZ, deltaY);
                    break;
                case Direction.WEST:
                    posReal = posReal.add(- 0.65, 0.0, 0.0);
                    delta = new Vec3d(-deltaY, deltaX, deltaZ);
                    break;
                case Direction.EAST:
                    posReal = posReal.add(0.65, 0.0, 0.0);
                    delta = new Vec3d(deltaY, deltaX, deltaZ);
                    break;
            }
            return world.spawnParticles(
                    particle,
                    posReal.x,
                    posReal.y,
                    posReal.z,
                    count,
                    delta.x,
                    delta.y,
                    delta.z,
                    speed);
        } else {
            return world.spawnParticles(particle, x, y, z, count, deltaX, deltaY, deltaZ, speed);
        }
    }
}