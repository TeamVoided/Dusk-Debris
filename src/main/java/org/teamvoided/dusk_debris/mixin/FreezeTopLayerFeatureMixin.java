package org.teamvoided.dusk_debris.mixin;

import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SnowBlock;
import net.minecraft.block.SnowyBlock;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.GameRules;
import net.minecraft.world.Heightmap;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.FreezeTopLayerFeature;
import net.minecraft.world.gen.feature.util.FeatureContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

@Mixin(FreezeTopLayerFeature.class)
public class FreezeTopLayerFeatureMixin {
    @Inject(method = "place", at = @At("HEAD"), cancellable = true)
    public void place(FeatureContext<DefaultFeatureConfig> context, CallbackInfoReturnable<Boolean> cir) {
        StructureWorldAccess structureWorldAccess = context.getWorld();
        int snowHeight = Objects.requireNonNull(structureWorldAccess.getServer()).getGameRules().getIntValue(GameRules.SNOW_ACCUMULATION_HEIGHT);

        if (snowHeight < 1) {
            cir.setReturnValue(false);
        } else if (snowHeight > 1) {
            BlockPos blockPos = context.getOrigin();
            BlockPos.Mutable mutable = new BlockPos.Mutable();
            BlockPos.Mutable mutable2 = new BlockPos.Mutable();

            for (int i = 0; i < 16; ++i) {
                for (int j = 0; j < 16; ++j) {
                    int k = blockPos.getX() + i;
                    int l = blockPos.getZ() + j;
                    int m = structureWorldAccess.getTopY(Heightmap.Type.MOTION_BLOCKING, k, l);
                    mutable.set(k, m, l);
                    mutable2.set(mutable).move(Direction.DOWN, 1);
                    Biome biome = structureWorldAccess.getBiome(mutable).value();
                    if (biome.canSetIce(structureWorldAccess, mutable2, false)) {
                        structureWorldAccess.setBlockState(mutable2, Blocks.ICE.getDefaultState(), 2);
                    }

                    if (biome.canSetSnow(structureWorldAccess, mutable)) {
                        BlockState blockState = structureWorldAccess.getBlockState(mutable2);
                        placeSnowLayers(structureWorldAccess, snowHeight, mutable);
                        if (blockState.contains(SnowyBlock.SNOWY)) {
                            structureWorldAccess.setBlockState(mutable2, blockState.with(SnowyBlock.SNOWY, true), 2);
                        }
                    }
                }
            }
            cir.setReturnValue(true);
        }
    }

    @Unique
    public void placeSnowLayers(StructureWorldAccess world, int snowHeight, BlockPos blockPos) {
        if (snowHeight > 8) {
            int height = snowHeight / 8;
            for (int i = 1; i <= height; ++i) {
                BlockPos pos = blockPos.up(i - 1);
                if (i == height) {
                    world.setBlockState(pos, Blocks.SNOW.getDefaultState().with(Properties.LAYERS, snowHeight % 8), 2);
                } else {
                    world.setBlockState(pos, Blocks.SNOW.getDefaultState().with(Properties.LAYERS, SnowBlock.MAX_LAYERS), 2);
                }
            }
        } else {
            world.setBlockState(blockPos, Blocks.SNOW.getDefaultState().with(Properties.LAYERS, snowHeight), 2);
        }
    }
}
