package org.teamvoided.dusk_debris.mixin.debugger;

import net.minecraft.state.StateManager;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Debug(export = true)
@Mixin(StateManager.Builder.class)
public abstract class StateManagerMixin {
    @Redirect(method = "validate", at = @At(value = "INVOKE", target = "Ljava/lang/String;valueOf(Ljava/lang/Object;)Ljava/lang/String;"))
    public String dropItem(Object obj) {
        return obj.toString() + ", class: " + obj.getClass().getName() + ", ";
    }
}
