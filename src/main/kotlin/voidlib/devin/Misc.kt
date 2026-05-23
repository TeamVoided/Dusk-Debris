package voidlib.devin

import net.minecraft.core.Holder
import net.minecraft.data.worldgen.BootstrapContext
import net.minecraft.resources.ResourceKey

fun <T> BootstrapContext<T>.register(key: ResourceKey<T>, obj: () -> T): Holder.Reference<T> = this.register(key, obj())