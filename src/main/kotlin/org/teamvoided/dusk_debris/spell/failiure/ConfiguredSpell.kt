//package org.teamvoided.dusk_debris.spell.failiure
//
//import com.mojang.serialization.Codec
//import net.minecraft.registry.*
//import net.minecraft.util.dynamic.RegistryElementCodec
//import net.minecraft.util.math.BlockPos
//import net.minecraft.util.random.RandomGenerator
//import net.minecraft.world.StructureWorldAccess
//import net.minecraft.world.gen.chunk.ChunkGenerator
//import org.teamvoided.dusk_debris.spell.codec.SpellConfig
//import org.teamvoided.dusk_debris.init.DuskRegistries
//import java.util.stream.Stream
//
//@JvmRecord
//data class ConfiguredSpell<SC : SpellConfig, S : Spell<SC>>(val spell: S, val spellConfig: SC) {
//    fun generate(
//        world: StructureWorldAccess,
//        chunkGenerator: ChunkGenerator,
//        random: RandomGenerator,
//        origin: BlockPos
//    ): Boolean {
//        return spell.placeIfValid(this.spellConfig, world, chunkGenerator, random, origin)
//    }
//
//    val decoratedFeatures: Stream<Record>
//        get() = Stream.concat(
//            Stream.of(this),
//            spellConfig.decoratedFeatures
//        )
//
//    override fun toString(): String {
//        return "Spell: $spell: $spellConfig"
//    }
//
//    companion object {
//        val CODEC_WRONG: Codec<ConfiguredSpell<*, *>> =
//            Registries.FEATURE.codec.dispatch(
//                { it.spell },
//                { it.codec })
//        val CODEC:Codec<ConfiguredSpell<*, *>> =
//            DuskRegistries.SPELLS_KEY
//
//        val REGISTRY_CODEC: Codec<Holder<ConfiguredSpell<*, *>>>
//        val LIST_CODEC: Codec<HolderSet<ConfiguredSpell<*, *>>>
//
//        init {
//            REGISTRY_CODEC = RegistryElementCodec.of(RegistryKeys.CONFIGURED_FEATURE, CODEC)
//            LIST_CODEC = RegistryCodecs.homogeneousList(RegistryKeys.CONFIGURED_FEATURE, CODEC)
//        }
//    }
//}
