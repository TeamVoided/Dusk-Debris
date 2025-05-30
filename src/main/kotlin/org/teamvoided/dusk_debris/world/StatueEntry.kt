package org.teamvoided.dusk_debris.world

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.entity.EntityType
import net.minecraft.entity.EquipmentTable
import net.minecraft.nbt.NbtCompound
import net.minecraft.util.Identifier
import net.minecraft.util.collection.DataPool
import java.util.*

class StatueEntry(var entity: NbtCompound = NbtCompound()) {

    init {
        if (entity.contains("id")) {
            val identifier = Identifier.tryParse(entity.getString("id"))
            if (identifier != null) {
                entity.putString("id", identifier.toString())
            } else {
                entity.remove("id")
            }
        }
    }

    companion object {

        fun create(entityType: EntityType<*>): StatueEntry {
            val nbtCompound = NbtCompound()
            nbtCompound.putString("id", entityType.builtInRegistryHolder.key.get().value.toString())
            return StatueEntry(nbtCompound)
        }

        val CODEC: Codec<StatueEntry> = RecordCodecBuilder.create { instance ->
            instance.group(
                NbtCompound.CODEC.fieldOf("entity").forGetter { it.entity },
            ).apply(instance, ::StatueEntry)
        }
    }
}