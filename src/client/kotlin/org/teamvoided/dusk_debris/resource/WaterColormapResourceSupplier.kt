package org.teamvoided.dusk_debris.resource

import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.util.RawTextureDataLoader
import net.minecraft.resource.ResourceManager
import net.minecraft.resource.SinglePreparationResourceReloader
import net.minecraft.util.Identifier
import net.minecraft.util.profiler.Profiler
import org.teamvoided.dusk_debris.DuskDebris.id
import org.teamvoided.dusk_debris.world.WaterColors
import java.io.IOException

@Environment(EnvType.CLIENT)
class WaterColormapResourceSupplier : SinglePreparationResourceReloader<IntArray>() {
    override fun prepare(resourceManager: ResourceManager, profiler: Profiler): IntArray {
        try {
            return RawTextureDataLoader.loadRawTextureData(resourceManager, WATER_COLORMAP_LOC)
        } catch (var4: IOException) {
            throw IllegalStateException("Failed to load water color texture", var4)
        }
    }

    override fun apply(`is`: IntArray, resourceManager: ResourceManager, profiler: Profiler) {
        WaterColors.setColorMap(`is`)
    }

    companion object {
        private val WATER_COLORMAP_LOC: Identifier = id("textures/colormap/water.png")
    }
}

