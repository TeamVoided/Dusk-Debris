package org.teamvoided.dusk_debris.block.not_blocks

import net.minecraft.world.level.block.state.properties.BooleanProperty
import net.minecraft.world.level.block.state.properties.EnumProperty
import net.minecraft.world.level.block.state.properties.IntegerProperty

object DuskProperties {
    @kotlin.jvm.JvmField
    val ACTIVE: BooleanProperty = BooleanProperty.create("active")

    val ACTIVE_STATE_INT: IntegerProperty = IntegerProperty.create("active_state_int", 0, 2)
    val CHALICES: IntegerProperty = IntegerProperty.create("chalices", 1, 4)
    val DISTANCE_1_15: IntegerProperty = IntegerProperty.create("distance", 1, 15)
    val DISTANCE_0_14: IntegerProperty = IntegerProperty.create("distance", 0, 14)

    val SQUISHED: BooleanProperty = BooleanProperty.create("squished")
    val LID: BooleanProperty = BooleanProperty.create("lid")
    val COCOON: BooleanProperty = BooleanProperty.create("cocoon")

    val CHEST_PHASE: EnumProperty<ChestPhase> = EnumProperty.create("chest_phase", ChestPhase::class.java)

    val ACTIVE_STATE: EnumProperty<ActiveState> =
        EnumProperty.create("active_state", ActiveState::class.java)
    val SQUISHED_VOLATILE: EnumProperty<SquishablePhases> =
        EnumProperty.create("bubble_phase", SquishablePhases::class.java)
    val GODHOME_BRONZE_PHASE: EnumProperty<GodhomeBronzePhase> =
        EnumProperty.create("godhome_bronze_phase", GodhomeBronzePhase::class.java)

    val FACING_OR_NULL =
        EnumProperty.create("facing_or_none", DirectionOrNullState::class.java)

}