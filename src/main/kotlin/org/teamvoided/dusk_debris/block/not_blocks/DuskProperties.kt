package org.teamvoided.dusk_debris.block.not_blocks

import net.minecraft.state.property.BooleanProperty
import net.minecraft.state.property.DirectionProperty
import net.minecraft.state.property.EnumProperty
import net.minecraft.state.property.IntProperty
import net.minecraft.util.math.Direction

object DuskProperties {
    @kotlin.jvm.JvmField
    val ACTIVE: BooleanProperty = BooleanProperty.of("active")

    val ACTIVE_STATE_INT: IntProperty = IntProperty.of("active_state_int", 0, 2)
    val CHALICES: IntProperty = IntProperty.of("chalices", 1, 4)
    val DISTANCE_1_15: IntProperty = IntProperty.of("distance", 1, 15)
    val DISTANCE_0_14: IntProperty = IntProperty.of("distance", 0, 14)

    val SQUISHED: BooleanProperty = BooleanProperty.of("squished")
    val LID: BooleanProperty = BooleanProperty.of("lid")
    val COCOON: BooleanProperty = BooleanProperty.of("cocoon")

    val CHEST_PHASE: EnumProperty<ChestPhase> = EnumProperty.of("chest_phase", ChestPhase::class.java)

    val ACTIVE_STATE: EnumProperty<ActiveState> =
        EnumProperty.of("active_state", ActiveState::class.java)
    val SQUISHED_VOLATILE: EnumProperty<SquishablePhases> =
        EnumProperty.of("bubble_phase", SquishablePhases::class.java)
    val GODHOME_BRONZE_PHASE: EnumProperty<GodhomeBronzePhase> =
        EnumProperty.of("godhome_bronze_phase", GodhomeBronzePhase::class.java)

    val FACING_OR_NULL =
        EnumProperty.of("facing_or_none", DirectionOrNullState::class.java)

}