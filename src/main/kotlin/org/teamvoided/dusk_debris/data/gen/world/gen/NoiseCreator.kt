package org.teamvoided.dusk_debris.data.gen.world.gen

import net.minecraft.registry.BootstrapContext
import net.minecraft.registry.RegistryKey
import net.minecraft.util.math.noise.DoublePerlinNoiseSampler.NoiseParameters
import org.teamvoided.dusk_debris.data.gen.world.gen.NoiseCreator.register
import org.teamvoided.dusk_debris.data.gen.world.gen.NoiseCreator.registerNetherBiomeNoises
import org.teamvoided.dusk_debris.data.worldgen.DuskNoiseParametersKeys

object NoiseCreator {
    fun bootstrap(c: BootstrapContext<NoiseParameters>) {
        c.overworld()
        c.nether()
    }

    private fun BootstrapContext<NoiseParameters>.overworld() {
        this.register(DuskNoiseParametersKeys.CONTINENTAL_WEIRDNESS, -8, 1.0, -1.0)
        this.register(DuskNoiseParametersKeys.GRAND_CANYON, -8, 1.0, 3.0, 1.0)
    }

    private fun BootstrapContext<NoiseParameters>.nether() {
        //register(c, DuskNoiseParametersKeys.LAVA_TUBE, -8, 1.0, -2.0, 1.0, 0.0, 0.0, 0.0)
        this.register(DuskNoiseParametersKeys.LAVA_LEVEL, -10, 1.0)
        this.register(DuskNoiseParametersKeys.EXAMPLE, -5, 1.0)
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
        this.register(DuskNoiseParametersKeys.RIDGE_NETHER, -7, 1.0, 2.0, 1.0, 0.0, 0.0, 0.0)
    }


    private fun BootstrapContext<NoiseParameters>.registerNetherBiomeNoises(
        octaveOffset: Int,
        temperature: RegistryKey<NoiseParameters>,
        humidity: RegistryKey<NoiseParameters>,
        continentalness: RegistryKey<NoiseParameters>,
        erosion: RegistryKey<NoiseParameters>,
        dropCeiling: RegistryKey<NoiseParameters>
    ) {
        this.register(temperature, -10 + octaveOffset, 1.5, 0.0, 1.0, 0.0, 0.0, 0.0)
        this.register(humidity, -8 + octaveOffset, 1.0, 1.0, 0.0, 0.0, 0.0, 0.0)
        this.register(continentalness, -9 + octaveOffset, 1.0, 1.0, 2.0, 2.0, 2.0, 1.0, 1.0, 1.0, 1.0)
        this.register(erosion, -9 + octaveOffset, 1.0, 1.0, 0.0, 1.0, 1.0)
        this.register(dropCeiling, -6 + octaveOffset, 1.0, 1.0, 1.0, 1.0, 0.0, 1.0, 0.0, 1.0)
    }

    private fun BootstrapContext<NoiseParameters>.register(
        key: RegistryKey<NoiseParameters>,
        firstOctave: Int,
        firstAmplitude: Double,
        vararg amplitudes: Double
    ) {
        this.register(key, NoiseParameters(firstOctave, firstAmplitude, *amplitudes))
    }
}