package org.teamvoided.dusk_debris.init.worldgen

import com.mojang.serialization.MapCodec
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.util.dynamic.CodecHolder
import net.minecraft.world.gen.surfacebuilder.SurfaceRules.*
import org.teamvoided.dusk_debris.DuskDebris.id
import org.teamvoided.dusk_debris.world.gen.surface_rules.NoiseThresholdThreeMaterialCondition

object DuskSurfaceRules {

    val threeDNoiseThresholdCondition =
        registerC("noise_threshold_3_condition", NoiseThresholdThreeMaterialCondition.CONDITION_CODEC)


    fun init() {}

    private fun <C : MaterialRule, F : CodecHolder<C>> register(id: String, surfaceRule: F): MapCodec<C> =
        Registry.register(Registries.MATERIAL_RULE, id(id), surfaceRule.codec())

    private fun <C : MaterialCondition, F : CodecHolder<C>> registerC(id: String, condition: F): MapCodec<C> =
        Registry.register(Registries.MATERIAL_CONDITION, id(id), condition.codec())
}