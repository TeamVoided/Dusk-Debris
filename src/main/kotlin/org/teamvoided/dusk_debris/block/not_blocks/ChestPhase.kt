package org.teamvoided.dusk_debris.block.not_blocks

import net.minecraft.util.StringRepresentable

enum class ChestPhase(val phaseName: String) : StringRepresentable {
    CLOSED("closed"),
    CLOSING("closing"),
    OPEN("open");

    override fun toString(): String = serializedName

    override fun getSerializedName(): String = phaseName

    companion object {
        fun fromInt(int: Int): ChestPhase {
            return when (int) {
                0 -> CLOSED
                1 -> CLOSING
                2 -> OPEN
                else -> throw MatchException(
                    "tried to set a ChestPhase, but gave an unusable number: $int",
                    null as Throwable?
                )
            }
        }
    }
}