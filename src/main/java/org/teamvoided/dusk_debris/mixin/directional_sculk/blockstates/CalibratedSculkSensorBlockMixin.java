package org.teamvoided.dusk_debris.mixin.directional_sculk.blockstates;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CalibratedSculkSensorBlock;
import net.minecraft.world.level.block.SculkSensorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.Property;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CalibratedSculkSensorBlock.class)
public class CalibratedSculkSensorBlockMixin extends SculkSensorBlock {
    public CalibratedSculkSensorBlockMixin(Properties settings) {
        super(settings);
    }

    @Redirect(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;setValue(Lnet/minecraft/world/level/block/state/properties/Property;Ljava/lang/Comparable;)Ljava/lang/Object;"))
    public Object addDefaultState(BlockState instance, Property property, Comparable comparable) {
        return instance.setValue(property, comparable);
    }

    @Inject(method = "createBlockStateDefinition", at = @At("HEAD"), cancellable = true)
    public void addDirectionalProperties(StateDefinition.Builder<Block, BlockState> builder, CallbackInfo ci) {
//        super.appendProperties(builder);
//        ci.cancel();
//        builder.add(Properties.FACING);
    }
}