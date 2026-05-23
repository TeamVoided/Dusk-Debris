package org.teamvoided.dusk_debris.mixin.directional_sculk.blockstates;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.SculkCatalystBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(SculkCatalystBlockEntity.CatalystListener.class)
public class SculkCatalystBlockEntityMixin {
    @Redirect(method = "bloom", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerLevel;sendParticles(Lnet/minecraft/core/particles/ParticleOptions;DDDIDDDD)I"))
    private <T extends ParticleOptions> int bloomParticles(ServerLevel world, T particle, double x, double y, double z, int count, double deltaX, double deltaY, double deltaZ, double speed, @Local BlockPos pos, @Local BlockState state) {
        var facing = state.getValue(BlockStateProperties.FACING);
        if (facing != Direction.UP) {
            var posReal = pos.getCenter();
            var delta = Vec3.ZERO;
            switch (facing) {
                case Direction.DOWN:
                    posReal = posReal.add(0.0, - 0.65, 0.0);
                    delta = new Vec3(deltaX, -deltaY, deltaZ);
                    break;
                case Direction.NORTH:
                    posReal = posReal.add(0.0, 0.0, - 0.65);
                    delta = new Vec3(deltaX, deltaZ, -deltaY);
                    break;
                case Direction.SOUTH:
                    posReal = posReal.add(0.0, 0.0, 0.65);
                    delta = new Vec3(deltaX, deltaZ, deltaY);
                    break;
                case Direction.WEST:
                    posReal = posReal.add(- 0.65, 0.0, 0.0);
                    delta = new Vec3(-deltaY, deltaX, deltaZ);
                    break;
                case Direction.EAST:
                    posReal = posReal.add(0.65, 0.0, 0.0);
                    delta = new Vec3(deltaY, deltaX, deltaZ);
                    break;
            }
            return world.sendParticles(
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
            return world.sendParticles(particle, x, y, z, count, deltaX, deltaY, deltaZ, speed);
        }
    }
}