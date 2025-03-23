package org.teamvoided.dusk_debris.init.worldgen

import com.mojang.serialization.MapCodec
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.structure.StructureType
import net.minecraft.world.gen.feature.StructureFeature
import org.teamvoided.dusk_debris.DuskDebris
import org.teamvoided.dusk_debris.world.gen.structure.CaveJigsawStructureFeature
import org.teamvoided.dusk_debris.world.gen.structure.CaveStructureFeature

object DuskStructureType {

    val SIMPLE_POOL: StructureType<CaveStructureFeature> = register( "cave_structure", CaveStructureFeature.CODEC)
    val CAVE_JIGSAW: StructureType<CaveJigsawStructureFeature> = register( "cave_jigsaw", CaveJigsawStructureFeature.CODEC)
    fun init() {}

    private fun <S : StructureFeature> register(id: String, codec: MapCodec<S>): StructureType<S> {
        return Registry.register(Registries.STRUCTURE_TYPE, DuskDebris.id(id), StructureType { codec })
    }
}