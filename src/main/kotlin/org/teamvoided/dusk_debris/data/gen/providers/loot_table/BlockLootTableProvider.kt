package org.teamvoided.dusk_debris.data.gen.providers.loot_table

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider
import net.minecraft.registry.HolderLookup
import java.util.concurrent.CompletableFuture

class BlockLootTableProvider(o: FabricDataOutput, r: CompletableFuture<HolderLookup.Provider>) :
    FabricBlockLootTableProvider(o, r) {


//    val dropsItSelf = listOf()

    override fun generate() {
//        dropsItSelf.forEach(::addDrop)
    }
}