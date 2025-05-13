package org.teamvoided.dusk_debris.util.world_helper

import net.minecraft.world.biome.source.util.MultiNoiseUtil


fun range(min: Number, max: Number): MultiNoiseUtil.ParameterRange =
    MultiNoiseUtil.ParameterRange.of(min.toFloat(), max.toFloat())

fun range(num: Number): MultiNoiseUtil.ParameterRange =
    MultiNoiseUtil.ParameterRange.of(num.toFloat())

fun MultiNoiseUtil.ParameterRange.mult(value: Float): MultiNoiseUtil.ParameterRange =
    range(this.min * value, this.max * value)