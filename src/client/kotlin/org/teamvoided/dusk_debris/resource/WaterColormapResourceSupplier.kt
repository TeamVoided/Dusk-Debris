package org.teamvoided.dusk_debris.resource

import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener
import net.minecraft.client.resources.LegacyStuffWrapper
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.packs.resources.ResourceManager
import net.minecraft.server.packs.resources.SimplePreparableReloadListener
import net.minecraft.util.profiling.ProfilerFiller
import org.teamvoided.dusk_debris.DuskDebris.id
import org.teamvoided.dusk_debris.world.WaterColors
import java.io.IOException

@Environment(EnvType.CLIENT)
class WaterColormapResourceSupplier : SimplePreparableReloadListener<IntArray>(),
    IdentifiableResourceReloadListener {
    override fun prepare(resourceManager: ResourceManager, profiler: ProfilerFiller): IntArray {
        try {
            return LegacyStuffWrapper.getPixels(resourceManager, WATER_COLORMAP_LOC)
        } catch (var4: IOException) {
            throw IllegalStateException("Failed to load water color texture", var4)
        }
    }

    override fun apply(`is`: IntArray, resourceManager: ResourceManager, profiler: ProfilerFiller) =
        WaterColors.setColorMap(`is`)

    override fun getFabricId(): ResourceLocation = id

    companion object {
        private val id: ResourceLocation = id("water_colors")
        private val WATER_COLORMAP_LOC: ResourceLocation = id("textures/colormap/water.png")
    }
}