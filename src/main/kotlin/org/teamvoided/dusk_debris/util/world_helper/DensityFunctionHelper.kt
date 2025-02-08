package org.teamvoided.dusk_debris.util.world_helper

import com.mojang.serialization.MapCodec
import net.minecraft.util.dynamic.CodecHolder


fun <O> makeCodec(codec: MapCodec<O>): CodecHolder<O> {
    return CodecHolder.method_42116(codec)
}