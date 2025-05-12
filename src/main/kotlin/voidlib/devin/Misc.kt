package voidlib.devin

import net.minecraft.registry.BootstrapContext
import net.minecraft.registry.Holder
import net.minecraft.registry.RegistryKey

fun <T> BootstrapContext<T>.register(key: RegistryKey<T>, obj: () -> T): Holder.Reference<T> = this.register(key, obj())