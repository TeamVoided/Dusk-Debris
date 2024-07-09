package org.teamvoided.dusk_debris.mixin;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.decoration.painting.PaintingEntity;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Debug(export = true)
@Mixin(PaintingEntity.class)
public class PaintingEntityMixin extends Entity {
    public PaintingEntityMixin(EntityType<?> variant, World world) {
        super(variant, world);
    }

    @Shadow
    protected void initDataTracker(DataTracker.Builder builder) {

    }

    @Shadow
    protected void readCustomDataFromNbt(NbtCompound nbt) {

    }

    @Shadow
    protected void writeCustomDataToNbt(NbtCompound nbt) {

    }

    //    @Inject(method = "method_6889", at=@At("RETURN"), cancellable = true)
//    private void dropsVariant(Entity entity, CallbackInfo ci){
//ci.
//    }
//    @Override
//    public ItemEntity dropItem(ItemConvertible item, int yOffset) {
//        var stack = new ItemStack(item);
//        var nbt = new NbtCompound();
//        this.writeCustomDataToNbt(nbt);
//        stack.set(DataComponentTypes.ENTITY_DATA, NbtComponent.of(nbt));
//        return this.dropStack(stack, yOffset);
//    }
}
