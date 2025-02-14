package org.teamvoided.dusk_debris

import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry
import net.fabricmc.fabric.api.attachment.v1.AttachmentSyncPredicate
import net.fabricmc.fabric.api.attachment.v1.AttachmentType
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.entity.EntityType
import net.minecraft.entity.decoration.DisplayEntity.TextDisplayEntity
import net.minecraft.entity.passive.SnifferEntity
import net.minecraft.item.ItemStack
import net.minecraft.registry.RegistryKey
import net.minecraft.server.command.CommandManager.literal
import net.minecraft.text.Text
import net.minecraft.util.Identifier
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.teamvoided.dusk_debris.block.DuskBlockFamilies
import org.teamvoided.dusk_debris.data.gen.providers.variants.SnifferVariants
import org.teamvoided.dusk_debris.entity.sniffer.SnifferVariant
import org.teamvoided.dusk_debris.init.*
import org.teamvoided.dusk_debris.init.worldgen.DuskBiomeModifications
import org.teamvoided.dusk_debris.module.DuskGameRules
import org.teamvoided.dusk_debris.util.variant


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
        DuskRegistries.init()

        DuskCommands.init()
    }

    fun id(path: String) = Identifier.of(MODID, path)
    fun mc(path: String) = Identifier.ofDefault(path)
    fun id(modId: String, path: String) = Identifier.of(modId, path)

    @JvmStatic
    fun isDev() = FabricLoader.getInstance().isDevelopmentEnvironment
}
