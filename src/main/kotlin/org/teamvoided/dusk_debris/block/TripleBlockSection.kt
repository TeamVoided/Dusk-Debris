package org.teamvoided.dusk_debris.block

import net.minecraft.util.StringIdentifiable
import net.minecraft.util.math.Direction

enum class TripleBlockSection(val direction: Direction) : StringIdentifiable {
    TOP(Direction.DOWN),
    MIDDLE(Direction.UP),
    BOTTOM(Direction.UP);

    override fun toString(): String = this.asString()

    override fun asString(): String {
        return if (this == TOP) "top" else if (this == MIDDLE) "middle" else "bottom"
    }

    fun getSection(): TripleBlockSection {
        return if (this == TOP) TOP else if (this == MIDDLE) MIDDLE else BOTTOM
    }

    fun getNumber(): Int {
        return if (this == TOP) 3 else if (this == MIDDLE) 2 else 1
    }

    companion object {
        fun getBelowSection(section: TripleBlockSection): TripleBlockSection? {
            return if (section == TOP) MIDDLE else if (section == MIDDLE) BOTTOM else null
        }
    }
}