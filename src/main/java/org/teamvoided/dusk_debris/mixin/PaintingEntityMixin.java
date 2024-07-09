package org.teamvoided.dusk_debris.mixin;

import com.mojang.serialization.Codec;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.decoration.painting.PaintingEntity;
import net.minecraft.entity.decoration.painting.PaintingVariant;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtOps;
import net.minecraft.registry.Holder;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.teamvoided.dusk_debris.data.tags.DuskPaintingVariantTags;

@Debug(export = true)
@Mixin(PaintingEntity.class)
public abstract class PaintingEntityMixin extends Entity {
    public PaintingEntityMixin(EntityType<?> variant, World world) {
        super(variant, world);
    }

    @Shadow
    protected void initDataTracker(DataTracker.Builder builder) {
    }

    @Shadow
    public void readCustomDataFromNbt(NbtCompound nbt) {
    }

    @Shadow
    public void writeCustomDataToNbt(NbtCompound nbt) {
    }

    @Shadow
    @Final
    public static Codec<Holder<PaintingVariant>> CODEC;

    @Shadow
    public abstract Holder<PaintingVariant> getVariant();

    @Override
    public ItemEntity dropItem(ItemConvertible item, int yOffset) {
        if (this.getVariant().isIn(DuskPaintingVariantTags.DROPS_SELF)) {
            var stack = new ItemStack(item);
            var nbt = new NbtCompound();
            nbt.putString("id", "minecraft:painting");
            CODEC.encodeStart(this.getRegistryManager().createSerializationContext(NbtOps.INSTANCE), this.getVariant()).ifSuccess((nbtElement) -> nbt.copyFrom((NbtCompound) nbtElement));
            stack.set(DataComponentTypes.ENTITY_DATA, NbtComponent.of(nbt));
            return this.dropStack(stack, yOffset);
        } else return super.dropItem(item, yOffset);
    }
}
