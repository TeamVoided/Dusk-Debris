package org.teamvoided.dusk_debris.util

import net.minecraft.registry.BootstrapContext
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey
import org.teamvoided.dusk_debris.DuskDebris.id

abstract class Bootstrapper<V>(val registryKey: RegistryKey<Registry<V>>) {
    protected val toRegister: MutableMap<RegistryKey<V>, Bootstrapper<V>> = mutableMapOf()
    protected fun register(name: String, bootstrapper: Bootstrapper<V>): RegistryKey<V> {
        val key = RegistryKey.of(registryKey, id(name))
        toRegister[key] = bootstrapper
        return key
    }

    open fun bootstrap(ctx: BootstrapContext<V>) {
        toRegister.forEach { (key, value) ->
            ctx.register(key, value.bootstrap(ctx))
        }
        toRegister.clear()
    }

    fun interface Bootstrapper<V> {
        fun bootstrap(ctx: BootstrapContext<V>): V
    }
}