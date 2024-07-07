package org.teamvoided.dusk_debris.block

import net.minecraft.block.SkullBlock

enum class DuskSkullType(private val type: String) : SkullBlock.SkullType {
    STRAY("stray"),
    BOGGED("bogged"),
    GLOOM("gloomed");

    init {
        SkullBlock.SkullType.TYPES[type] = this
    }

    override fun asString(): String = this.type
}