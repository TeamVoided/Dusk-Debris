package org.teamvoided.dusk_debris.init

import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry
import net.fabricmc.fabric.api.attachment.v1.AttachmentSyncPredicate
import net.fabricmc.fabric.api.attachment.v1.AttachmentType
import net.minecraft.registry.RegistryKey
import org.teamvoided.dusk_debris.DuskDebris
import org.teamvoided.dusk_debris.data.gen.providers.variants.SnifferVariants
import org.teamvoided.dusk_debris.entity.sniffer.SnifferVariant

object DuskAttachmentTypes {
    fun init() {}


//    @JvmField
//    val SNIFFER_VARIANT: AttachmentType<RegistryKey<SnifferVariant>> =
//        AttachmentRegistry.create(DuskDebris.id("sniffer_variant")) { builder: AttachmentRegistry.Builder<RegistryKey<SnifferVariant>> ->
//            builder
//                .initializer { SnifferVariants.DEFAULT }
//                .persistent(RegistryKey.codec(DuskRegistries.SNIFFER_VARIANT))
//                .syncWith(RegistryKey.packetCodec(DuskRegistries.SNIFFER_VARIANT), AttachmentSyncPredicate.all())
//        }
}