package org.teamvoided.dusk_debris.init.worldgen.structure

import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.structure.piece.StructurePieceType
import org.teamvoided.dusk_debris.DuskDebris
import org.teamvoided.dusk_debris.world.gen.structure.piece.PoolNoJigsawStructurePiece

object DuskStructurePieceType {

    val SIMPLE: StructurePieceType = register("simpool", ::PoolNoJigsawStructurePiece)
    fun init() {}

    private fun register(id: String, type: StructurePieceType): StructurePieceType {
        return Registry.register(Registries.STRUCTURE_PIECE_TYPE, DuskDebris.id(id), type)
    }
}