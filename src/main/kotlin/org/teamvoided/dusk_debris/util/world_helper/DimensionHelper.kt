package org.teamvoided.dusk_debris.util.world_helper

import net.minecraft.world.level.biome.Climate.Parameter


fun range(min: Number, max: Number = min): Parameter = Parameter.span(min.toFloat(), max.toFloat())

fun Parameter.mult(value: Float) = range(this.min * value, this.max * value)