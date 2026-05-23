package org.teamvoided.dusk_debris.mixin.pixel_accurate;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.WallSignBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.teamvoided.dusk_debris.block.mixin.SignShapes;

@Mixin(WallSignBlock.class)
public class WallSignBlockMixin extends Block {
    public WallSignBlockMixin(Properties settings) {
        super(settings);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return SignShapes.getWallShape(state.getValue(BlockStateProperties.HORIZONTAL_FACING));
    }
}