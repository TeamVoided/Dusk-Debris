package org.teamvoided.dusk_debris.entity.data


import net.minecraft.entity.data.TrackedDataHandler
import net.minecraft.util.collection.Int2ObjectBiMap
import org.teamvoided.dusk_debris.entity.TuffGolemEntity

object DuskTrackedDataHandlerRegistry {
    private val DATA_HANDLERS: Int2ObjectBiMap<TrackedDataHandler<*>> = Int2ObjectBiMap.create(16)
    val TUFF_GOLEM_STATE: TrackedDataHandler<TuffGolemEntity.TuffGolemState> =
        TrackedDataHandler.create(TuffGolemEntity.TuffGolemState.PACKET_CODEC)

    fun register(handler: TrackedDataHandler<*>) {
        DATA_HANDLERS.add(handler)
    }

    fun get(id: Int): TrackedDataHandler<*>? {
        return DATA_HANDLERS[id]
    }

    fun getId(handler: TrackedDataHandler<*>): Int {
        return DATA_HANDLERS.getRawId(handler)
    }

    fun init() {
        register(TUFF_GOLEM_STATE)
    }
}