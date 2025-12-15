package org.teamvoided.dusk_debris.init

import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry
import net.fabricmc.fabric.api.attachment.v1.AttachmentSyncPredicate
import net.fabricmc.fabric.api.attachment.v1.AttachmentType
import net.minecraft.registry.RegistryKey
import net.minecraft.util.math.BlockPos
import org.teamvoided.dusk_debris.DuskDebris
import org.teamvoided.dusk_debris.block.attachments.ExhaustData
import org.teamvoided.dusk_debris.data.gen.providers.variants.SnifferVariants
import org.teamvoided.dusk_debris.entity.variant.SnifferVariant

@Suppress("UnstableApiUsage")
object DuskAttachmentTypes {
    fun init() {}


    @JvmField
    val SNIFFER_VARIANT: AttachmentType<RegistryKey<SnifferVariant>> =
        AttachmentRegistry.create(DuskDebris.id("sniffer_variant")) { builder: AttachmentRegistry.Builder<RegistryKey<SnifferVariant>> ->
            builder
                .initializer { SnifferVariants.DEFAULT }
                .persistent(RegistryKey.codec(DuskRegistryKeys.SNIFFER_VARIANT))
                .syncWith(RegistryKey.packetCodec(DuskRegistryKeys.SNIFFER_VARIANT), AttachmentSyncPredicate.all())
        }
    val EXHAUST_DATA: AttachmentType<Map<BlockPos, ExhaustData>> =
        AttachmentRegistry.create(DuskDebris.id("exhaust_data")) { builder ->
            builder.persistent(ExhaustData.MAP_CODEC)
        }
}