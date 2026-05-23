package org.teamvoided.dusk_debris.mixin;

import com.mojang.serialization.Codec;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.Painting;
import net.minecraft.world.entity.decoration.PaintingVariant;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.teamvoided.dusk_debris.data.tags.DuskPaintingVariantTags;

@Debug(export = true)
@Mixin(Painting.class)
public abstract class PaintingMixin extends Entity {
    public PaintingMixin(EntityType<?> variant, Level world) {
        super(variant, world);
    }

    @Shadow
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
    }

    @Shadow
    public void readAdditionalSaveData(CompoundTag nbt) {
    }

    @Shadow
    public void addAdditionalSaveData(CompoundTag nbt) {
    }

    @Shadow
    @Final
    public static Codec<Holder<PaintingVariant>> VARIANT_CODEC;

    @Shadow
    public abstract Holder<PaintingVariant> getVariant();

    @Override
    public ItemEntity spawnAtLocation(ItemLike item, int yOffset) {
        if (this.getVariant().is(DuskPaintingVariantTags.DROPS_SELF)) {
            var stack = new ItemStack(item);
            var nbt = new CompoundTag();
            nbt.putString("id", "minecraft:painting");
            VARIANT_CODEC.encodeStart(this.registryAccess().createSerializationContext(NbtOps.INSTANCE), this.getVariant()).ifSuccess((nbtElement) -> nbt.merge((CompoundTag) nbtElement));
            stack.set(DataComponents.ENTITY_DATA, CustomData.of(nbt));
            return this.spawnAtLocation(stack, yOffset);
        } else return super.spawnAtLocation(item, yOffset);
    }
}
