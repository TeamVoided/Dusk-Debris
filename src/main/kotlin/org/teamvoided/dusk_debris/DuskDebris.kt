package org.teamvoided.dusk_debris


import net.fabricmc.loader.api.FabricLoader
import net.minecraft.resources.ResourceLocation
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.teamvoided.dusk_debris.block.DuskBlockFamilies
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

        DuskItems.init()
        DuskBlocks.init()
        DuskFluids.init()
        DuskBlockFamilies.init()
        DuskBlockEntities.init()
        DuskEffects.init()
        DuskEntities.init()
        DuskWorldgen.init()
        DuskBiomeModifications.init()
        DuskParticles.init()
        DuskSoundEvents.init()
        DuskGameRules.init()
        DuskTabs.init()

        DuskAttachmentTypes.init()
        DuskRegistryKeys.init()
        DuskRegistries.init()

        DuskSpellTypes.init()

        DuskCommands.init()
        DuskComponents.init()
        DuskNet.init()

        InitializeFabricEvents()
    }

    fun id(path: String) = ResourceLocation.fromNamespaceAndPath(MODID, path)
    fun mc(path: String) = ResourceLocation.withDefaultNamespace(path)
    fun id(modId: String, path: String) = ResourceLocation.fromNamespaceAndPath(modId, path)

    @JvmStatic
    fun isDev() = FabricLoader.getInstance().isDevelopmentEnvironment
}
