package org.teamvoided.dusk_debris

import net.fabricmc.fabric.api.client.rendering.v1.LivingEntityFeatureRendererRegistrationCallback
import net.fabricmc.fabric.api.resource.ResourceManagerHelper
import net.minecraft.client.render.CameraSubmersionType
import net.minecraft.client.render.entity.SnifferEntityRenderer
import net.minecraft.entity.EntityType
import net.minecraft.resource.ResourceType
import net.minecraft.util.math.Vec3d
import org.teamvoided.dusk_debris.DuskDebris.log
import org.teamvoided.dusk_debris.entity.DuskEntityModelLayers
import org.teamvoided.dusk_debris.entity.variant.SnifferOverlayFeatureRenderer
import org.teamvoided.dusk_debris.init.*
import org.teamvoided.dusk_debris.resource.WaterColormapResourceSupplier

@Suppress("unused")
object DuskDebrisClient {
    @JvmStatic
    val SIGN_RENDER_OFFSET = Vec3d(0.0, 0.33333, 0.1)

    fun init() {
        log.info("Hello from Client")

        DuskEntityModelLayers.init()
        DuskBlocksClient.init()
        DuskFluidsClient.init()
        DuskItemsClient.init()
        DuskParticlesClient.init()
        DuskEntitiesClient.init()
        DuskBlockEntitiesClient.init()

        LivingEntityFeatureRendererRegistrationCallback.EVENT.register { entityType, entityRenderer, registrationHelper, context ->
            if (entityType != EntityType.SNIFFER) return@register
            if (entityRenderer !is SnifferEntityRenderer) return@register
            registrationHelper.register(SnifferOverlayFeatureRenderer(entityRenderer))
        }
        ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(WaterColormapResourceSupplier())

        //CameraSubmersionType.entries.forEach { println(it) }
    }
}