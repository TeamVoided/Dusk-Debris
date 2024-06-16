package org.teamvoided.dusk_debris

import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry
import org.teamvoided.dusk_autumn.init.DuskEntities
import org.teamvoided.dusk_debris.DuskDebris.log
import org.teamvoided.dusk_debris.entity.gunpowder_barrel.GunpowderBarrelEntityRenderer

@Suppress("unused")
object DuskDebrisClient{
    fun init() {
        log.info("Hello from Client")
        EntityRendererRegistry.register(DuskEntities.GUNPOWDER_BARREL,::GunpowderBarrelEntityRenderer)
    }
}
