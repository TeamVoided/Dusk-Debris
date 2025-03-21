package org.teamvoided.dusk_debris.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Holder;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.command.SummonCommand;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SummonCommand.class)
public class SummonCommandMixin {
//    @Inject(method = "createEntity", at = @At(value = "RETURN"), cancellable = true)
//    private static void initialize(ServerCommandSource source, Holder.Reference<EntityType<?>> entityType, Vec3d pos, NbtCompound nbt, boolean initialize, CallbackInfoReturnable<Entity> cir) {
//
//    }
}
