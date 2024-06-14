package org.teamvoided.dusk_debris.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.AbstractSkeletonEntity;
import net.minecraft.entity.mob.WitherSkeletonEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.random.RandomGenerator;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Debug(export = true)
@Mixin(WitherSkeletonEntity.class)
public abstract class WitherSkeletonEntityMixin extends AbstractSkeletonEntity {

    protected WitherSkeletonEntityMixin(EntityType<? extends AbstractSkeletonEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(at = @At("RETURN"), method = "method_6996", cancellable = true)
    private void applyWitherToArrow(ItemStack itemStack, float f, ItemStack itemStack2, CallbackInfoReturnable<PersistentProjectileEntity> cir) {
        PersistentProjectileEntity persistentProjectileEntity = super.method_6996(itemStack, f, itemStack2);
        if (persistentProjectileEntity instanceof ArrowEntity arrowEntity) {
            arrowEntity.addEffect(new StatusEffectInstance(StatusEffects.WITHER, 160));
        }
        cir.setReturnValue(persistentProjectileEntity);
    }

    @Inject(at = @At("HEAD"), method = "initEquipment", cancellable = true)
    protected void initEquipment(RandomGenerator random, LocalDifficulty difficulty, CallbackInfo ci) {
        if (random.nextFloat() > 0.8f) {
            if (random.nextInt(3) < 2) {
                this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.STONE_AXE));
            } else {
                this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.BOW));
            }
        } else {
            this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.STONE_SWORD));
        }
        ci.cancel();
    }
}