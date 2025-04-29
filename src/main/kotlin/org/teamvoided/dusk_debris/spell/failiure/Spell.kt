//package org.teamvoided.dusk_debris.spell.failiure
//
//import com.mojang.serialization.Codec
//import com.mojang.serialization.MapCodec
//import net.minecraft.registry.Registries
//import net.minecraft.registry.Registry
//import net.minecraft.world.gen.feature.Feature
//import net.minecraft.world.gen.feature.FeatureConfig
//import org.teamvoided.dusk_debris.DuskDebris
//import org.teamvoided.dusk_debris.init.DuskRegistries
//import org.teamvoided.dusk_debris.spell.SpellConfig
//import org.teamvoided.dusk_debris.spell.codec.VengefulSpiritSpellCodec
//
//open class Spell<SC : SpellConfig>(codec: Codec<SC>) {
//    val codec: MapCodec<ConfiguredSpell<SC, Spell<SC>>> = codec.fieldOf("config").xmap(
//        { ConfiguredSpell(this, it) },
//        { it.spellConfig })
//
//    open fun test() {
//        throw NotImplementedError("Function must be overridden")
//    }
//
//    companion object {
//        //move out later
//        val VENGEFUL_SPIRIT = register("vengeful_spirit", VengefulSpiritSpell(VengefulSpiritSpellCodec.CODEC))
//
//        private fun <SC : SpellConfig, S : Spell<SC>> register(name: String, spell: S): S {
//            return Registry.register(DuskRegistries.SPELLS_KEY, name, spell)
//        }
//
//
//        private fun <C : FeatureConfig?, F : Feature<C>> register(name: String, feature: F): F =
//            Registry.register(Registries.FEATURE, DuskDebris.id(name), feature)
//    }
//}