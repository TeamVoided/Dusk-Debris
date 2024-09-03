package org.teamvoided.dusk_debris

import net.minecraft.util.Identifier
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.teamvoided.dusk_debris.entity.data.DuskTrackedDataHandlerRegistry
import org.teamvoided.dusk_debris.init.DuskEntities
import org.teamvoided.dusk_debris.init.DuskWorldgen
import org.teamvoided.dusk_debris.init.*
import org.teamvoided.dusk_debris.init.worldgen.DuskBiomeModifications
import org.teamvoided.dusk_debris.module.DuskGameRules

@Suppress("unused")
object DuskDebris {
    const val MODID = "dusk_debris"

    @JvmField
    val log: Logger = LoggerFactory.getLogger(DuskDebris::class.simpleName)

    fun init() {
        log.info("Hello from Common")
        DuskBlocks.init()
        DuskBlockEntities.init()
        DuskItems.init()
        DuskItemGroups.init()
        DuskTrackedDataHandlerRegistry.init()
        DuskEntities.init()
        DuskWorldgen.init()
        DuskBiomeModifications.init()
        DuskParticles.init()
        DuskSoundEvents.init()
        DuskGameRules.init()
    }

    fun id(path: String) = Identifier.of(MODID, path)
    fun mc(path: String) = Identifier.ofDefault(path)
    fun id(modId: String, path: String) = Identifier.of(modId, path)
}
