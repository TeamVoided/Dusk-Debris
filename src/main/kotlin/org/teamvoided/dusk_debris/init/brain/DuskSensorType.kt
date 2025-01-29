package org.teamvoided.dusk_debris.init.brain

import net.minecraft.entity.ai.brain.sensor.Sensor
import net.minecraft.entity.ai.brain.sensor.SensorType
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import org.teamvoided.dusk_debris.DuskDebris.id
import org.teamvoided.dusk_debris.entity.ai.brain.sensor.GiantEnemyJellyfishAttackEntiySensor
import java.util.function.Supplier

object DuskSensorType {
    val GEJ_ATTACK_ENTITY_SENSOR = register("gej_attack_entity_sensor") { GiantEnemyJellyfishAttackEntiySensor() }

    private fun <U : Sensor<*>> register(id: String, factory: Supplier<U>): SensorType<U> {
        return Registry.register(Registries.SENSOR_TYPE, id(id), SensorType(factory))
    }
}