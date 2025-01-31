package org.teamvoided.dusk_debris.block.not_blocks

import net.minecraft.state.property.BooleanProperty
import net.minecraft.state.property.EnumProperty
import net.minecraft.state.property.IntProperty

object DuskProperties {
    val ACTIVE: BooleanProperty = BooleanProperty.of("active")

    val CHALICES: IntProperty = IntProperty.of("chalices", 1, 4)
    val DISTANCE_1_15: IntProperty = IntProperty.of("distance", 1, 15)

    val SQUISHED: BooleanProperty = BooleanProperty.of("squished")
    val SQUISHED_VOLATILE: EnumProperty<ExplosiveBubblePhase> =
        EnumProperty.of("bubble_phase", ExplosiveBubblePhase::class.java)
    val GODHOME_BRONZE_PHASE: EnumProperty<GodhomeBronzePhase> =
        EnumProperty.of("godhome_bronze_phase", GodhomeBronzePhase::class.java)
}