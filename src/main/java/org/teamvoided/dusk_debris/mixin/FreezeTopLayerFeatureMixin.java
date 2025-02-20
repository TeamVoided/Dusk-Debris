package org.teamvoided.dusk_debris.mixin;

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
            BlockPos.Mutable snowPos = new BlockPos.Mutable();
            BlockPos.Mutable blockBelow = new BlockPos.Mutable();

            for (int loopX = 0; loopX < 16; ++loopX) {
                int x = blockPos.getX() + loopX;
                for (int loopZ = 0; loopZ < 16; ++loopZ) {
                    int z = blockPos.getZ() + loopZ;
                    int y = structureWorldAccess.getTopY(Heightmap.Type.MOTION_BLOCKING, x, z);
                    snowPos.set(x, y, z);
                    blockBelow.set(snowPos).move(Direction.DOWN, 1);
                    Biome biome = structureWorldAccess.getBiome(snowPos).value();
                    if (biome.canSetIce(structureWorldAccess, blockBelow, false)) {
                        structureWorldAccess.setBlockState(blockBelow, Blocks.ICE.getDefaultState(), 2);
                    }

                    if (biome.canSetSnow(structureWorldAccess, snowPos)) {
                        BlockState downState = structureWorldAccess.getBlockState(blockBelow);
                        placeSnowLayers(structureWorldAccess, snowHeight, snowPos);
                        if (downState.contains(SnowyBlock.SNOWY)) {
                            structureWorldAccess.setBlockState(blockBelow, downState.with(SnowyBlock.SNOWY, true), 2);
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
                int layerheight = i == height ? snowHeight % SnowBlock.MAX_LAYERS : SnowBlock.MAX_LAYERS;
                world.setBlockState(pos, Blocks.SNOW.getDefaultState().with(Properties.LAYERS, layerheight), 2);
            }
        } else {
            world.setBlockState(blockPos, Blocks.SNOW.getDefaultState().with(Properties.LAYERS, snowHeight), 2);
        }
    }
}
