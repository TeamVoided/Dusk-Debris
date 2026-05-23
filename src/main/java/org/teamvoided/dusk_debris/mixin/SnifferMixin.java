package org.teamvoided.dusk_debris.mixin;

import net.minecraft.core.Holder;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.sniffer.Sniffer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.biome.Biome;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.teamvoided.dusk_debris.data.gen.providers.variants.SnifferVariants;
import org.teamvoided.dusk_debris.entity.variant.SnifferVariant;
import org.teamvoided.dusk_debris.init.DuskAttachmentTypes;
import org.teamvoided.dusk_debris.init.DuskRegistryKeys;

@Debug(export = true)
@Mixin(Sniffer.class)
public abstract class SnifferMixin extends Mob implements VariantHolder<Holder<SnifferVariant>> {


    protected SnifferMixin(EntityType<? extends Mob> entityType, Level world) {
        super(entityType, world);
    }

    @Override
    @Nullable
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor world, DifficultyInstance difficulty, MobSpawnType spawnReason, @Nullable SpawnGroupData entityData) {
        Holder<Biome> biome = world.getBiome(this.blockPosition());
        Holder<SnifferVariant> variant = SnifferVariants.fromBiome(this.registryAccess(), biome);
        this.setVariant(variant);
        return super.finalizeSpawn(world, difficulty, spawnReason, entityData);
    }

    @Override
    public Holder<SnifferVariant> getVariant() {
        var id = this.getAttachedOrElse(DuskAttachmentTypes.SNIFFER_VARIANT, SnifferVariants.DEFAULT);
        return this.level().registryAccess().lookupOrThrow(DuskRegistryKeys.SNIFFER_VARIANT).getOrThrow(id);
    }

    @Override
    public void setVariant(Holder<SnifferVariant> holder) {
        this.setAttached(DuskAttachmentTypes.SNIFFER_VARIANT, holder.unwrapKey().get());
    }

    static {
//        SNOWY = DataTracker.registerData(SnifferEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    }
}
