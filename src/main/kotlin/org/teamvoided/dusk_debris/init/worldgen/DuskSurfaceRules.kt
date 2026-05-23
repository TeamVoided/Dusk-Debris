package org.teamvoided.dusk_debris.init.worldgen

import com.mojang.serialization.MapCodec
import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.util.KeyDispatchDataCodec
import net.minecraft.world.level.levelgen.SurfaceRules.ConditionSource
import net.minecraft.world.level.levelgen.SurfaceRules.RuleSource
import org.teamvoided.dusk_debris.DuskDebris.id

object DuskSurfaceRules {

    fun init() {}

    private fun <C : RuleSource, F : KeyDispatchDataCodec<C>> register(id: String, surfaceRule: F): MapCodec<C> =
        Registry.register(BuiltInRegistries.MATERIAL_RULE, id(id), surfaceRule.codec())

    private fun <C : ConditionSource, F : KeyDispatchDataCodec<C>> registerC(id: String, condition: F): MapCodec<C> =
        Registry.register(BuiltInRegistries.MATERIAL_CONDITION, id(id), condition.codec())
}