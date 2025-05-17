package org.teamvoided.dusk_debris.util.world_helper

import net.minecraft.world.biome.source.util.MultiNoiseUtil.*


fun range(min: Number, max: Number = min): ParameterRange = ParameterRange.of(min.toFloat(), max.toFloat())

fun ParameterRange.mult(value: Float) = range(this.min * value, this.max * value)