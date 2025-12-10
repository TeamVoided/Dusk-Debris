package org.teamvoided.dusk_debris.block.not_blocks

import kotlinx.serialization.modules.serializersModuleOf
import net.minecraft.util.StringIdentifiable
import net.minecraft.util.math.Direction

enum class DirectionOrNullState(val direction: Direction?) : StringIdentifiable {
    DOWN(Direction.DOWN),
    UP(Direction.UP),
    NORTH(Direction.NORTH),
    SOUTH(Direction.SOUTH),
    WEST(Direction.WEST),
    EAST(Direction.EAST),
    NONE(null);

    override fun toString(): String = string()

    override fun asString(): String = string()

    private fun string(): String = direction?.toString() ?: "source"

    companion object {
        fun fromDirection(direction: Direction?): DirectionOrNullState =
            if (direction != null)
                fromInt(direction.id)
            else
                NONE

        fun fromInt(int: Int): DirectionOrNullState {
            return when (int) {
                0 -> DOWN
                1 -> UP
                2 -> NORTH
                3 -> SOUTH
                4 -> WEST
                5 -> EAST
                6 -> NONE
                else -> throw MatchException(
                    "tried to get a DirectionOrNullState, but gave an unusable number: $int",
                    null as Throwable?
                )
            }
        }
    }
}