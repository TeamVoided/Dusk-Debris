package org.teamvoided.dusk_debris.block.not_blocks

import net.minecraft.state.property.EnumProperty
import net.minecraft.util.StringIdentifiable

enum class GodhomeBronzePhase(val phaseName: String, val id: Int) : StringIdentifiable {
    SOMBER("somber", 0),
    SHINING("shining", 1),
    RADIANT("radiant", 2);

    override fun toString(): String {
        return phaseName
    }

    override fun asString(): String {
        return phaseName
    }

    companion object {
        val GODHOME_BRONZE_PHASE = EnumProperty.of("godhome_bronze_phase", GodhomeBronzePhase::class.java)

        fun fromInt(int: Int): GodhomeBronzePhase {
            return when (int) {
                0 -> SOMBER
                1 -> SHINING
                2 -> RADIANT
                else -> throw MatchException(
                    "tried to set a GodhomeBronzePhase, but gave an unusable number: $int",
                    null as Throwable?
                )
            }
        }
    }
}