package org.teamvoided.dusk_debris.init.worldgen.structure

import com.mojang.serialization.MapCodec
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.structure.pool.StructurePoolElement
import net.minecraft.structure.pool.StructurePoolElementType
import org.teamvoided.dusk_debris.structure.pool.CavityPoolElement

object DuskStructurePoolElementType {

    val CAVITY = register("cavity", CavityPoolElement.CODEC)

    fun init() {}

    fun <P : StructurePoolElement> register(id: String, codec: MapCodec<P>): StructurePoolElementType<P> {
        return Registry.register(Registries.STRUCTURE_POOL_ELEMENT_TYPE, id, StructurePoolElementType { codec })
    }
}