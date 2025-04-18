package org.teamvoided.dusk_debris.mixin.pixel_accurate;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.sign.SignBlock;
import net.minecraft.block.sign.WallSignBlock;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.teamvoided.dusk_debris.block.mixin.SignShapes;

@Mixin(WallSignBlock.class)
public class WallSignBlockMixin extends Block {
    public WallSignBlockMixin(Settings settings) {
        super(settings);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SignShapes.getWallShape(state.get(Properties.HORIZONTAL_FACING));
    }
}