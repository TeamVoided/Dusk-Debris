package org.teamvoided.dusk_debris.mixin.directional_sculk.spreading;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.sculk.SculkBehavior;
import net.minecraft.block.sculk.SculkBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.random.RandomGenerator;
import net.minecraft.world.WorldAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.teamvoided.dusk_debris.block.not_blocks.SculkDirectionalStuff;
import org.teamvoided.dusk_debris.data.tags.DuskFluidTags;

@Mixin(SculkBlock.class)
public class SculkBlockMixin {
    @Redirect(method = "tryUseCharge", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/random/RandomGenerator;nextInt(I)I", ordinal = 1))
    private int fluidTag(RandomGenerator random, int j, @Local SculkBehavior.ChargeCursor charge, @Local WorldAccess world, @Local SculkBehavior sculkChargeHandler) {
        int i = charge.getCharge();
        if (random.nextInt(j) < i) {
            BlockPos blockPos = charge.getPos();
            SculkDirectionalStuff.getRandomGrowthStateWithoutOffset(world, blockPos, random, sculkChargeHandler.isWorldGen());
        }
        return i + 1;
    }
}