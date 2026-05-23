package org.teamvoided.dusk_debris.init.worldgen.structure

import com.mojang.serialization.MapCodec
import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElementType
import org.teamvoided.dusk_debris.structure.pool.CavityPoolElement

object DuskStructurePoolElementType {

    val CAVITY = register("cavity", CavityPoolElement.CODEC)

    fun init() {}

    fun <P : StructurePoolElement> register(id: String, codec: MapCodec<P>): StructurePoolElementType<P> {
        return Registry.register(BuiltInRegistries.STRUCTURE_POOL_ELEMENT, id, StructurePoolElementType { codec })
    }
}