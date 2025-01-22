package org.teamvoided.voidlib.helpers

import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider

fun <T> FabricTagProvider<T>.FabricTagBuilder.add(list: Collection<T>): FabricTagProvider<T>.FabricTagBuilder {
    list.forEach { this.add(it) }
    return this
}
