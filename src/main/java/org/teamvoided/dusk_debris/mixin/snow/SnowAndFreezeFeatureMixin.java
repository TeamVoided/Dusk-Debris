package org.teamvoided.dusk_debris.mixin.snow;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SnowLayerBlock;
import net.minecraft.world.level.block.SnowyDirtBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.SnowAndFreezeFeature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

@Mixin(SnowAndFreezeFeature.class)
public class SnowAndFreezeFeatureMixin {
    @Inject(method = "place", at = @At("HEAD"), cancellable = true)
    public void place(FeaturePlaceContext<NoneFeatureConfiguration> context, CallbackInfoReturnable<Boolean> cir) {
        WorldGenLevel structureWorldAccess = context.level();
        int snowHeight = Objects.requireNonNull(structureWorldAccess.getServer()).getGameRules().getInt(GameRules.RULE_SNOW_ACCUMULATION_HEIGHT);

        if (snowHeight < 1) {
            cir.setReturnValue(false);
        } else if (snowHeight > 1) {
            BlockPos blockPos = context.origin();
            BlockPos.MutableBlockPos snowPos = new BlockPos.MutableBlockPos();
            BlockPos.MutableBlockPos blockBelow = new BlockPos.MutableBlockPos();

            for (int loopX = 0; loopX < 16; ++loopX) {
                int x = blockPos.getX() + loopX;
                for (int loopZ = 0; loopZ < 16; ++loopZ) {
                    int z = blockPos.getZ() + loopZ;
                    int y = structureWorldAccess.getHeight(Heightmap.Types.MOTION_BLOCKING, x, z);
                    snowPos.set(x, y, z);
                    blockBelow.set(snowPos).move(Direction.DOWN, 1);
                    Biome biome = structureWorldAccess.getBiome(snowPos).value();
                    if (biome.shouldFreeze(structureWorldAccess, blockBelow, false)) {
                        structureWorldAccess.setBlock(blockBelow, Blocks.ICE.defaultBlockState(), 2);
                    }

                    if (biome.shouldSnow(structureWorldAccess, snowPos)) {
                        BlockState downState = structureWorldAccess.getBlockState(blockBelow);
                        placeSnowLayers(structureWorldAccess, snowHeight, snowPos);
                        if (downState.hasProperty(SnowyDirtBlock.SNOWY)) {
                            structureWorldAccess.setBlock(blockBelow, downState.setValue(SnowyDirtBlock.SNOWY, true), 2);
                        }
                    }
                }
            }
            cir.setReturnValue(true);
        }
    }

    @Unique
    public void placeSnowLayers(WorldGenLevel world, int snowHeight, BlockPos blockPos) {
        if (snowHeight > 8) {
            int height = snowHeight / 8;
            for (int i = 1; i <= height; ++i) {
                BlockPos pos = blockPos.above(i - 1);
                int layerheight = i == height ? snowHeight % SnowLayerBlock.MAX_HEIGHT : SnowLayerBlock.MAX_HEIGHT;
                world.setBlock(pos, Blocks.SNOW.defaultBlockState().setValue(BlockStateProperties.LAYERS, layerheight), 2);
            }
        } else {
            world.setBlock(blockPos, Blocks.SNOW.defaultBlockState().setValue(BlockStateProperties.LAYERS, snowHeight), 2);
        }
    }
}
