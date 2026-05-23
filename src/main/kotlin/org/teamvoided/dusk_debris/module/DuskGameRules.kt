package org.teamvoided.dusk_debris.module

import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory.createIntRule
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry.register
import net.minecraft.world.level.GameRules.Category

object DuskGameRules {
    fun init() = Unit

//    val TUFF_GOLEM_DISABLE = register("dusk_debris.disableTuffGolem", Category.MOBS, createBooleanRule(false))
    val MAX_ENTITY_SHIFT = register("duskDebris.maxEntityShift", Category.MOBS, createIntRule(20))

//    fun <T : GameRules.AbstractGameRule<T>> World.getRule(key: GameRules.Key<T>): T = gameRules.get(key)

//    @JvmStatic
//    fun <T : GameRules.AbstractGameRule<T>> getRuleValue(world: World, key: GameRules.Key<T>): T = world.getRule(key)

//    @JvmStatic
//    fun World.getBoolRule(key: GameRules.Key<GameRules.BooleanGameRule>): Boolean = this.gameRules.get(key).value
}