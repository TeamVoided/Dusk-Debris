package org.teamvoided.dusk_debris

import org.teamvoided.dusk_debris.DuskDebris.log
import org.teamvoided.dusk_debris.entity.DuskEntityModelLayers
import org.teamvoided.dusk_debris.init.*

@Suppress("unused")
object DuskDebrisClient {
    fun init() {
        log.info("Hello from Client")

        DuskEntityModelLayers.init()
        DuskBlocksClient.init()
        DuskFluidsClient.init()
        DuskItemsClient.init()
        DuskParticlesClient.init()
        DuskEntitiesClient.init()
        DuskBlockEntitiesClient.init()
    }
}
