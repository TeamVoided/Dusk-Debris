package org.teamvoided.dusk_debris.data

import net.minecraft.loot.LootTable
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import org.teamvoided.dusk_debris.DuskDebris.id

object DuskLootTables {

    val ENDERMAN_HOLDS = create("enderman/holds")
    val ENDERMAN_OVERWORLD_GENERIC = create("enderman/overworld_generic")
    val ENDERMAN_NETHER_GENERIC = create("enderman/nether_generic")
    val ENDERMAN_END_GENERIC = create("enderman/end_generic")
    val ENDERMAN_OVERWORLD_DESERT = create("enderman/overworld_desert")
    val ENDERMAN_OVERWORLD_BADLANDS = create("enderman/overworld_badlands")
    val ENDERMAN_OVERWORLD_ICE = create("enderman/overworld_ice")
    val ENDERMAN_OVERWORLD_FLOWER = create("enderman/overworld_flower")
    fun create(id: String): RegistryKey<LootTable> = RegistryKey.of(RegistryKeys.LOOT_TABLE, id(id))

}