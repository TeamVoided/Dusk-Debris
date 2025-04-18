package org.teamvoided.dusk_debris.mixin.pixel_accurate;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.WoodType;
import net.minecraft.block.sign.AbstractSignBlock;
import net.minecraft.block.sign.SignBlock;
import net.minecraft.block.sign.WallSignBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin({SignBlock.class, WallSignBlock.class})
public abstract class CombinedSignBlockMixin extends AbstractSignBlock {
    protected CombinedSignBlockMixin(WoodType type, Settings settings) {
        super(type, settings);
    }

    @Override
    protected BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }
}