package org.teamvoided.dusk_debris.data.gen.world.gen

import net.minecraft.registry.BootstrapContext
import net.minecraft.registry.RegistryKey
import net.minecraft.util.math.noise.DoublePerlinNoiseSampler.NoiseParameters
import org.teamvoided.dusk_debris.data.gen.world.gen.NoiseCreator.nether
import org.teamvoided.dusk_debris.data.gen.world.gen.NoiseCreator.register
import org.teamvoided.dusk_debris.data.worldgen.DuskNoiseParametersKeys

object NoiseCreator {
    fun bootstrap(c: BootstrapContext<NoiseParameters>) {
        c.overworld()
        c.nether()
        c.register(DuskNoiseParametersKeys.EXAMPLE, -5, 1)
    }

    private fun BootstrapContext<NoiseParameters>.overworld() {
        this.register(DuskNoiseParametersKeys.CONTINENTAL_WEIRDNESS, -8, 1, 1)

        this.register(DuskNoiseParametersKeys.PLATEAU_TYPE, -8, 1, 1, 0, 1, 1)
        this.register(DuskNoiseParametersKeys.GRAND_CANYON, -8, 1, 3, 1)

        this.register(DuskNoiseParametersKeys.FLATS_TYPE, -6, 1, 0, 1)
        this.register(DuskNoiseParametersKeys.FLATS_ELEV, -5, 1)

        this.register(DuskNoiseParametersKeys.UR_TYPE, -7, 1)
        this.register(DuskNoiseParametersKeys.UR_HEIGHT, -6, 1, 1, 1)

        this.register(DuskNoiseParametersKeys.STONE_TOWERS, -7, 1,1)
        this.register(DuskNoiseParametersKeys.STONE_TOWERS_HEIGHT, -6, 1, 1, 1)
        this.register(DuskNoiseParametersKeys.STONE_TOWERS_OFFSET, -7, 1)
    }

    private fun BootstrapContext<NoiseParameters>.nether() {
        //register(c, DuskNoiseParametersKeys.LAVA_TUBE, -8, 1, -2, 1, 0, 0, 0)
        this.register(DuskNoiseParametersKeys.LAVA_LEVEL, -10, 1)
        this.registerNetherBiomeNoises(
            0,
            DuskNoiseParametersKeys.TEMPERATURE_NETHER,
            DuskNoiseParametersKeys.VEGETATION_NETHER,
            DuskNoiseParametersKeys.CONTINENTALNESS_NETHER,
            DuskNoiseParametersKeys.EROSION_NETHER,
            DuskNoiseParametersKeys.DROP_CEILING
        )
        //this.registerNetherBiomeNoises(
        //    -2,
        //    DuskNoiseParametersKeys.TEMPERATURE_LARGE_NETHER,
        //    DuskNoiseParametersKeys.VEGETATION_LARGE_NETHER,
        //    DuskNoiseParametersKeys.CONTINENTALNESS_LARGE_NETHER,
        //    DuskNoiseParametersKeys.EROSION_LARGE_NETHER,
        //    DuskNoiseParametersKeys.DROP_CEILING_LARGE
        //)
        this.register(DuskNoiseParametersKeys.RIDGE_NETHER, -7, 1, 2, 1, 0, 0, 0)
    }


    private fun BootstrapContext<NoiseParameters>.registerNetherBiomeNoises(
        octaveOffset: Int,
        temperature: RegistryKey<NoiseParameters>,
        humidity: RegistryKey<NoiseParameters>,
        continentalness: RegistryKey<NoiseParameters>,
        erosion: RegistryKey<NoiseParameters>,
        dropCeiling: RegistryKey<NoiseParameters>
    ) {
        this.register(temperature, -10 + octaveOffset, 1.5, 0, 1, 0, 0, 0)
        this.register(humidity, -8 + octaveOffset, 1, 1, 0, 0, 0, 0)
        this.register(continentalness, -9 + octaveOffset, 1, 1, 2, 2, 2, 1, 1, 1, 1)
        this.register(erosion, -9 + octaveOffset, 1, 1, 0, 1, 1)
        this.register(dropCeiling, -6 + octaveOffset, 1, 1, 1, 1, 0, 1, 0, 1)
    }

    private fun BootstrapContext<NoiseParameters>.register(
        key: RegistryKey<NoiseParameters>,
        firstOctave: Int,
        firstAmplitude: Number,
        vararg amplitudes: Number
    ) {
        val array = DoubleArray(amplitudes.size)
        amplitudes.forEachIndexed { idx, it -> array[idx] = it.toDouble() }
        this.register(key, NoiseParameters(firstOctave, firstAmplitude.toDouble(), *array))
    }
}