package org.teamvoided.dusk_debris.mixin.spell;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.teamvoided.dusk_debris.entity.helper.DuskSpellStuff;
import org.teamvoided.dusk_debris.spell.Spell;

@Mixin(PlayerEntity.class)
abstract public class PlayerEntitySpellMixin extends LivingEntity implements DuskSpellStuff {

    protected PlayerEntitySpellMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "tickMovement", at = @At("HEAD"))
    public void spellTick(CallbackInfo ci) {
        if (getSpell() != null) {
            var spell = getSpell();
            spell.castTick(this);
            if (getSpellTicksLeft() <= 0) {
                spell.onCastEnd(this);
                setSpellTicksLeft(0);
                setSpell(null);
            } else {
                setSpellTicksLeft(getSpellTicksLeft() - 1);
            }
        }
    }

    @Override
    public void setSpell(@Nullable Spell spell) {
        DuskDebris$spell = spell;
    }

    @Nullable
    @Override
    public Spell getSpell() {
        return DuskDebris$spell;
    }

    @Override
    public int getSpellTicksLeft() {
        return DuskDebris$spellTicksLeft;
    }

    @Override
    public void setSpellTicksLeft(int spellTicksLeft) {
        DuskDebris$spellTicksLeft = spellTicksLeft;
    }

    @Unique
    public int DuskDebris$spellTicksLeft = 0;

    @Unique
    @Nullable
    public Spell DuskDebris$spell = null;
}
