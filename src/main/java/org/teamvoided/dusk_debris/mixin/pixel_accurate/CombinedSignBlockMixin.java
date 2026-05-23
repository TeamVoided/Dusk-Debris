package org.teamvoided.dusk_debris.mixin.pixel_accurate;

import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SignBlock;
import net.minecraft.world.level.block.StandingSignBlock;
import net.minecraft.world.level.block.WallSignBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.WoodType;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({StandingSignBlock.class, WallSignBlock.class})
public abstract class CombinedSignBlockMixin extends SignBlock {
    protected CombinedSignBlockMixin(WoodType type, Properties settings) {
        super(type, settings);
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }
}