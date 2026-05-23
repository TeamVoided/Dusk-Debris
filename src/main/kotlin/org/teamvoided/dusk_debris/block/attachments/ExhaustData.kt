package org.teamvoided.dusk_debris.block.attachments

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import com.mojang.serialization.codecs.UnboundedMapCodec
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import java.util.*
import kotlin.jvm.optionals.getOrNull

data class ExhaustData(
    val sourcePos: BlockPos? = null,
    val sourceDirection: Direction? = null,
    val age: Int = 0,
    val note: Int = 0
) {
    fun isSource(): Boolean = sourcePos == null

    companion object {
        val CODEC: Codec<ExhaustData> = RecordCodecBuilder.create { instance ->
            instance.group(
                BlockPos.CODEC.optionalFieldOf("source_pos")
                    .forGetter { Optional.ofNullable(it.sourcePos) },
                Direction.CODEC.optionalFieldOf("source_direction")
                    .forGetter { Optional.ofNullable(it.sourceDirection) },
                Codec.INT.fieldOf("age").forGetter { it.age },
                Codec.INT.fieldOf("note").forGetter { it.note },
            ).apply(instance) { pos: Optional<BlockPos>, dir: Optional<Direction>, age: Int, note: Int ->
                ExhaustData(pos.getOrNull(), dir.getOrNull(), age, note)
            }
        }
        val MAP_CODEC: UnboundedMapCodec<BlockPos, ExhaustData> = Codec.unboundedMap(BlockPos.CODEC, CODEC)
    }
}