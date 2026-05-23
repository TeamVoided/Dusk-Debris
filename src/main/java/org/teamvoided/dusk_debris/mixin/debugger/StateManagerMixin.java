package org.teamvoided.dusk_debris.mixin.debugger;

import net.minecraft.world.level.block.state.StateDefinition;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Debug(export = true)
@Mixin(StateDefinition.Builder.class)
public abstract class StateManagerMixin {
    @Redirect(method = "validateProperty", at = @At(value = "INVOKE", target = "Ljava/lang/String;valueOf(Ljava/lang/Object;)Ljava/lang/String;"))
    public String dropItem(Object obj) {
        return obj.toString() + ", class: " + obj.getClass().getName() + ", ";
    }
}
