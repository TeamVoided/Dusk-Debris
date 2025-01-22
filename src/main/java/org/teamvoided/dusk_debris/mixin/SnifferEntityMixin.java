package org.teamvoided.dusk_debris.mixin;

import net.minecraft.entity.*;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.SnifferEntity;
import net.minecraft.registry.Holder;
import net.minecraft.util.random.RandomGenerator;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.apache.commons.io.LineIterator;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.teamvoided.dusk_debris.DuskDebris;
import org.teamvoided.dusk_debris.data.gen.providers.variants.SnifferVariants;
import org.teamvoided.dusk_debris.data.variants.DuskSnifferVariants;
import org.teamvoided.dusk_debris.entity.sniffer.SnifferVariant;
import org.teamvoided.dusk_debris.init.DuskRegistries;

@Debug(export = true)
@Mixin(SnifferEntity.class)
public abstract class SnifferEntityMixin extends MobEntity implements VariantProvider<Holder<SnifferVariant>> {


    protected SnifferEntityMixin(EntityType<? extends MobEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    @Nullable
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData) {
        entityData = super.initialize(world, difficulty, spawnReason, entityData);
        RandomGenerator randomGenerator = world.getRandom();

        this.setAttached(DuskDebris.SNIFFER_VARIANT, DuskSnifferVariants.INSTANCE.getPINK());
        return entityData;
    }

    @Override
    public Holder<SnifferVariant> getVariant() {
        var id = this.getAttachedOrElse(DuskDebris.SNIFFER_VARIANT, SnifferVariants.DEFAULT);
        return this.getWorld().getRegistryManager().getLookupOrThrow(DuskRegistries.SNIFFER_VARIANT).getHolderOrThrow(id);
    }

    @Override
    public void setVariant(Holder<SnifferVariant> holder) {
        this.setAttached(DuskDebris.SNIFFER_VARIANT, holder.getKey().get());
    }

    static {
//        SNOWY = DataTracker.registerData(SnifferEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    }
}
