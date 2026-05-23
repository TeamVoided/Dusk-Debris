package org.teamvoided.dusk_debris.init.worldgen.structure

import com.mojang.serialization.MapCodec
import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.level.levelgen.structure.Structure
import net.minecraft.world.level.levelgen.structure.StructureType
import org.teamvoided.dusk_debris.DuskDebris
import org.teamvoided.dusk_debris.world.gen.structure.CaveJigsawStructureFeature
import org.teamvoided.dusk_debris.world.gen.structure.CaveStructureFeature

object DuskStructureType {

    val SIMPLE_POOL: StructureType<CaveStructureFeature> = register( "cave_structure", CaveStructureFeature.CODEC)
    val CAVE_JIGSAW: StructureType<CaveJigsawStructureFeature> = register( "cave_jigsaw", CaveJigsawStructureFeature.CODEC)
    fun init() {}

    private fun <S : Structure> register(id: String, codec: MapCodec<S>): StructureType<S> {
        return Registry.register(BuiltInRegistries.STRUCTURE_TYPE, DuskDebris.id(id), StructureType { codec })
    }
}