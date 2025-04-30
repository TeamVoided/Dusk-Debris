package org.teamvoided.dusk_debris.mixin.spell;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.teamvoided.dusk_debris.entity.helper.DuskSpellStuff;
import org.teamvoided.dusk_debris.entity.helper.SpellController;
import org.teamvoided.dusk_debris.spell.Spell;

@Mixin(PlayerEntity.class)
abstract public class PlayerEntitySpellMixin extends LivingEntity implements DuskSpellStuff {

    protected PlayerEntitySpellMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Unique
    public SpellController spellController = new SpellController();


    @Inject(method = "tickMovement", at = @At("HEAD"))
    public void spellTick(CallbackInfo ci) {
        if (this.getWorld().isClient) return;
        spellController.tick(this);
    }

    @NotNull
    @Override
    public SpellController getSpellController() {
        return spellController;
    }
}
