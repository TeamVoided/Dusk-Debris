package org.teamvoided.dusk_debris.mixin.directional_sculk.blockstates;

import net.minecraft.block.*;
import net.minecraft.block.sculk.SculkSensorBlock;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.state.property.Property;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CalibratedSculkSensorBlock.class)
public class CalibratedSculkSensorBlockMixin extends SculkSensorBlock {
    public CalibratedSculkSensorBlockMixin(Settings settings) {
        super(settings);
    }

    @Redirect(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;with(Lnet/minecraft/state/property/Property;Ljava/lang/Comparable;)Ljava/lang/Object;"))
    public Object addDefaultState(BlockState instance, Property property, Comparable comparable) {
        return instance.with(property, comparable);
    }

    @Inject(method = "appendProperties", at = @At("HEAD"), cancellable = true)
    public void addDirectionalProperties(StateManager.Builder<Block, BlockState> builder, CallbackInfo ci) {
//        super.appendProperties(builder);
//        ci.cancel();
//        builder.add(Properties.FACING);
    }
}