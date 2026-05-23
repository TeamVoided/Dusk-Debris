package org.teamvoided.dusk_debris.block

import net.minecraft.world.level.block.SkullBlock

enum class DuskSkullType(private val type: String) : SkullBlock.Type {
    STRAY("stray"),
    BOGGED("bogged"),
    GLOOM("gloomed");

    init {
        SkullBlock.Type.TYPES[type] = this
    }

    override fun getSerializedName(): String = this.type
}