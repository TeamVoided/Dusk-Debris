package org.teamvoided.dusk_debris.mixin.spell;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.teamvoided.dusk_debris.entity.helper.DuskSpellStuff;
import org.teamvoided.dusk_debris.entity.helper.SpellController;

@Mixin(Player.class)
abstract public class PlayerSpellMixin extends LivingEntity implements DuskSpellStuff {

    protected PlayerSpellMixin(EntityType<? extends LivingEntity> entityType, Level world) {
        super(entityType, world);
    }

    @Unique
    public SpellController spellController = new SpellController();


    @Inject(method = "aiStep", at = @At("HEAD"))
    public void spellTick(CallbackInfo ci) {
        if (this.level().isClientSide) return;
        spellController.tick(this);
    }

    @NotNull
    @Override
    public SpellController getSpellController() {
        return spellController;
    }
}
