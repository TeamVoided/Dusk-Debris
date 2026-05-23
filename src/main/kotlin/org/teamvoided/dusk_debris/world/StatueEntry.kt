package org.teamvoided.dusk_debris.world

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.nbt.CompoundTag
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.EntityType

class StatueEntry(var entity: CompoundTag = CompoundTag()) {

    init {
        if (entity.contains("id")) {
            val identifier = ResourceLocation.tryParse(entity.getString("id"))
            if (identifier != null) {
                entity.putString("id", identifier.toString())
            } else {
                entity.remove("id")
            }
        }
    }

    companion object {

        fun create(entityType: EntityType<*>): StatueEntry {
            val nbtCompound = CompoundTag()
            nbtCompound.putString("id", entityType.builtInRegistryHolder().unwrapKey().get().location().toString())
            return StatueEntry(nbtCompound)
        }

        val CODEC: Codec<StatueEntry> = RecordCodecBuilder.create { instance ->
            instance.group(
                CompoundTag.CODEC.fieldOf("entity").forGetter { it.entity },
            ).apply(instance, ::StatueEntry)
        }
    }
}