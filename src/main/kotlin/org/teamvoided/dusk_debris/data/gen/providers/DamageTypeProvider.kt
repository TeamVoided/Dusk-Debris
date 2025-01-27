package org.teamvoided.dusk_debris.data.gen.providers

import net.minecraft.entity.damage.*
import net.minecraft.registry.BootstrapContext
import org.teamvoided.dusk_debris.data.DuskDamageTypes

object DamageTypeProvider {
    fun bootstrap(c: BootstrapContext<DamageType>) {
        c.register(DuskDamageTypes.ACID, DamageType("acid", 0.1f))
        c.register(DuskDamageTypes.ELECTRICITY, DamageType("electricity", 0.1f))
        c.register(DuskDamageTypes.INDIRECT_ELECTRICITY, DamageType("indirect_electricity", 0.1f))
    }

//    fun BootstrapContext<DamageType>.register(
//        registryKey: RegistryKey<DamageType>,
//        builder: DamageTypes.Builder
//    ) {
//        this.register(registryKey, builder.build(registryKey.value))
//    }

//    fun DamageType(messageId: String, scaling: DamageScalingType, exhaustion: Float): DamageType {
//        return DamageType(messageId, scaling, exhaustion, DamageEffects.HURT, DeathMessageType.DEFAULT)
//    }
//
//    fun DamageType(
//        messageId: String,
//        scaling: DamageScalingType,
//        exhaustion: Float,
//        effects: DamageEffects
//    ): DamageType {
//        return DamageType(messageId, scaling, exhaustion, effects, DeathMessageType.DEFAULT)
//    }
//
//    fun DamageType(messageId: String, exhaustion: Float, effects: DamageEffects): DamageType {
//        return DamageType(messageId, DamageScalingType.WHEN_CAUSED_BY_LIVING_NON_PLAYER, exhaustion, effects)
//    }

    private fun DamageType(messageId: String, exhaustion: Float): DamageType {
        return DamageType(messageId, DamageScalingType.WHEN_CAUSED_BY_LIVING_NON_PLAYER, exhaustion)
    }
}