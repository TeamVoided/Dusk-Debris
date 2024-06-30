package org.teamvoided.dusk_debris

import net.minecraft.util.Identifier
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.teamvoided.dusk_autumn.init.DuskEntities
import org.teamvoided.dusk_autumn.init.DuskWorldgen
import org.teamvoided.dusk_debris.init.*

@Suppress("unused")
object DuskDebris {
    const val MODID = "dusk_debris"

    @JvmField
    val log: Logger = LoggerFactory.getLogger(DuskDebris::class.simpleName)

    fun init() {
        log.info("Hello from Common")
        DuskBlocks.init()
        DuskItems.init()
        DuskItemGroups.init()
        DuskEntities.init()
        DuskWorldgen.init()
        DuskParticles.init()
        DuskSoundEvents.init()
    }

    fun id(path: String) = Identifier.of(MODID, path)
}
