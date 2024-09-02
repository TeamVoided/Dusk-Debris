package org.teamvoided.dusk_debris.module

import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory.createBooleanRule
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry.register
import net.minecraft.world.GameRules
import net.minecraft.world.GameRules.Category
import net.minecraft.world.World

object DuskGameRules {
    fun init() = Unit

    @JvmField
    val TUFF_GOLEM_DISABLE = register("nullium.disableTuffGolem", Category.MOBS, createBooleanRule(false))

//    fun <T : GameRules.AbstractGameRule<T>> World.getRule(key: GameRules.Key<T>): T = gameRules.get(key)
//
//    @JvmStatic
//    fun <T : GameRules.AbstractGameRule<T>> getRuleValue(world: World, key: GameRules.Key<T>): T = world.getRule(key)
//
//    @JvmStatic
//    fun World.getBoolRule(key: GameRules.Key<GameRules.BooleanGameRule>): Boolean = this.gameRules.get(key).value
}