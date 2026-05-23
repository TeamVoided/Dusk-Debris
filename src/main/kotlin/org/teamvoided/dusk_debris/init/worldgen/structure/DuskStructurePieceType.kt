package org.teamvoided.dusk_debris.init.worldgen.structure

import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType
import org.teamvoided.dusk_debris.DuskDebris
import org.teamvoided.dusk_debris.world.gen.structure.piece.PoolNoJigsawStructurePiece

object DuskStructurePieceType {

    val SIMPLE: StructurePieceType = register("simpool", ::PoolNoJigsawStructurePiece)
    fun init() {}

    private fun register(id: String, type: StructurePieceType): StructurePieceType {
        return Registry.register(BuiltInRegistries.STRUCTURE_PIECE, DuskDebris.id(id), type)
    }
}