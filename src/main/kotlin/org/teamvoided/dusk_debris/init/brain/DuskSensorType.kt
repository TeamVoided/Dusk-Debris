package org.teamvoided.dusk_debris.init.brain

import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.entity.ai.sensing.Sensor
import net.minecraft.world.entity.ai.sensing.SensorType
import org.teamvoided.dusk_debris.DuskDebris.id
import org.teamvoided.dusk_debris.entity.ai.brain.sensor.GiantEnemyJellyfishAttackEntiySensor
import java.util.function.Supplier

object DuskSensorType {
    val GEJ_ATTACK_ENTITY_SENSOR = register("gej_attack_entity_sensor") { GiantEnemyJellyfishAttackEntiySensor() }

    private fun <U : Sensor<*>> register(id: String, factory: Supplier<U>): SensorType<U> {
        return Registry.register(BuiltInRegistries.SENSOR_TYPE, id(id), SensorType(factory))
    }
}