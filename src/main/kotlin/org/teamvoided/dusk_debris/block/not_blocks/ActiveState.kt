package org.teamvoided.dusk_debris.block.not_blocks

import net.minecraft.util.StringRepresentable

enum class ActiveState(val phaseName: String, val id: Int) : StringRepresentable {
    COOLDOWN("cooldown", 0),
    IDLE("idle", 1),
    ACTIVE("active", 2);

    override fun toString(): String = phaseName

    override fun getSerializedName(): String = phaseName

    companion object {
        fun fromInt(int: Int): ActiveState {
            return when (int) {
                0 -> COOLDOWN
                1 -> IDLE
                2 -> ACTIVE
                else -> throw MatchException(
                    "tried to set a ActiveState Enum, but gave an unusable number: $int",
                    null as Throwable?
                )
            }
        }
    }
}