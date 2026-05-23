package org.teamvoided.dusk_debris.init

import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.level.block.LiquidBlock
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.block.state.BlockBehaviour
import net.minecraft.world.level.material.FlowingFluid
import net.minecraft.world.level.material.MapColor
import net.minecraft.world.level.material.PushReaction
import org.teamvoided.dusk_debris.DuskDebris
import org.teamvoided.dusk_debris.fluid.AcidFluid

object DuskFluids {
    val FLUIDS = mutableSetOf<FlowingFluid>()

    val ACID: FlowingFluid = register("acid", AcidFluid.Still())
    val FLOWING_ACID: FlowingFluid = register("flowing_acid", AcidFluid.Flowing())

    val ACID_BLOCK = DuskBlocks.registerNoItem(
        "acid", LiquidBlock(
            ACID,
            BlockBehaviour.Properties.of().mapColor(MapColor.WATER).replaceable().noCollission().strength(100.0f)
                .pushReaction(PushReaction.DESTROY).noLootTable().liquid().sound(SoundType.EMPTY)
        )
    )

    fun init() {}


    private fun register(id: String, fluid: FlowingFluid): FlowingFluid {
        val regFluid = Registry.register(BuiltInRegistries.FLUID, DuskDebris.id(id), fluid)
        FLUIDS.add(regFluid)
        return regFluid
    }
}