package org.teamvoided.dusk_debris.mixin;

import net.minecraft.block.*;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.state.property.Property;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.teamvoided.dusk_debris.block.StrongScaffoldingBlock;
import org.teamvoided.dusk_debris.block.not_blocks.DuskProperties;

@Debug(export = true)
@Mixin(ScaffoldingBlock.class)
public class ScaffoldingBlockMixin extends Block implements Waterloggable {
    public ScaffoldingBlockMixin(Settings settings) {
        super(settings);
    }

//    @Redirect(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;with(Lnet/minecraft/state/property/Property;Ljava/lang/Comparable;)Ljava/lang/Object;", ordinal = 0))
//    private Object defaultState(BlockState state, Property<?> property, Comparable<IntProperty> comparable) {
//        if (state.getBlock() instanceof StrongScaffoldingBlock) {
//            return state.with(DuskProperties.INSTANCE.getDISTANCE_0_14(), comparable);
//        } else {
//            return state.with(property, comparable);
//        }
//    }
//
//    @Redirect(method = "appendProperties", at = @At(value = "INVOKE", target = "Lnet/minecraft/state/StateManager$Builder;add([Lnet/minecraft/state/property/Property;)Lnet/minecraft/state/StateManager$Builder;"))
//    public StateManager.Builder<Block, BlockState> properties(StateManager.Builder<Block, BlockState> instance, Property<?>[] properties) {
//        if (this.asBlock() instanceof StrongScaffoldingBlock) {
//            return StrongScaffoldingBlock.strongScaffoldingProperties(instance, properties);
//        } else {
//            return instance.add(properties);
//        }
//    }
//
//    @Redirect(method = "getPlacementState", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;with(Lnet/minecraft/state/property/Property;Ljava/lang/Comparable;)Ljava/lang/Object;"))
//    private Object placement(BlockState state, Property property, Comparable comparable) {
//        if (property == Properties.DISTANCE_0_7 && state.getBlock() instanceof StrongScaffoldingBlock) {
//            return state.with(DuskProperties.INSTANCE.getDISTANCE_0_14(), comparable);
//        } else {
//            return state.with(property, comparable);
//        }
//    }

}
