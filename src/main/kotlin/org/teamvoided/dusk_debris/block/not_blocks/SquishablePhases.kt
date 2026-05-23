package org.teamvoided.dusk_debris.block.not_blocks

import net.minecraft.util.StringRepresentable

enum class SquishablePhases(val phaseName: String, val id: Int) : StringRepresentable {
    SQUISHED("squished", 0),
    BRIMMING("brimming", 1),
    VOLATILE("volatile", 2);

    override fun toString(): String {
        return phaseName
    }

    override fun getSerializedName(): String {
        return phaseName
    }

    companion object {
        fun fromInt(int: Int): SquishablePhases {
            return when (int) {
                0 -> SQUISHED
                1 -> BRIMMING
                2 -> VOLATILE
                else -> throw MatchException(
                    "tried to set a ExplosiveBubblePhase, but gave an unusable number: $int",
                    null as Throwable?
                )
            }
        }
    }
}