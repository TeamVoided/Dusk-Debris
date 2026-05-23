package org.teamvoided.dusk_debris.util.world_helper

import com.mojang.serialization.MapCodec
import net.minecraft.util.KeyDispatchDataCodec


fun <O> makeCodec(codec: MapCodec<O>): KeyDispatchDataCodec<O> {
    return KeyDispatchDataCodec.of(codec)
}