package org.teamvoided.dusk_debris.data

import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.world.level.storage.loot.LootTable
import org.teamvoided.dusk_debris.DuskDebris.id

object DuskLootTables {

    val RAIDER_BAD_OMEN_BOTTLE = createEntity("inject/raider_bad_omen_bottle")

    val ENDERMAN_HOLDS = createEntity("enderman/holds")
    val ENDERMAN_OVERWORLD_GENERIC = createEntity("enderman/overworld_generic")
    val ENDERMAN_NETHER_GENERIC = createEntity("enderman/nether_generic")
    val ENDERMAN_END_GENERIC = createEntity("enderman/end_generic")
    val ENDERMAN_OVERWORLD_DESERT = createEntity("enderman/overworld_desert")
    val ENDERMAN_OVERWORLD_BADLANDS = createEntity("enderman/overworld_badlands")
    val ENDERMAN_OVERWORLD_ICE = createEntity("enderman/overworld_ice")
    val ENDERMAN_OVERWORLD_FLOWER = createEntity("enderman/overworld_flower")
    fun createEntity(id: String): ResourceKey<LootTable> = create("entity/$id")
    fun create(id: String): ResourceKey<LootTable> = ResourceKey.create(Registries.LOOT_TABLE, id(id))
}