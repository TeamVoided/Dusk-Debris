package org.teamvoided.dusk_debris.world

object WaterColors {
    private var colorMap = IntArray(65536)

    fun setColorMap(map: IntArray) {
        colorMap = map
    }

    @JvmStatic
    fun getColor(temperature: Double, humidity: Double): Int {
        var humidity2 = humidity
        humidity2 *= temperature
        val i = ((1.0 - temperature) * 255.0).toInt()
        val j = ((1.0 - humidity2) * 255.0).toInt()
        val k = j shl 8 or i
        return if (k >= colorMap.size) -65281 else colorMap[k]
    }

    val default: Int
        get() = getColor(0.5, 1.0)
}
