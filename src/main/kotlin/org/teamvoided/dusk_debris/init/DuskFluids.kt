package org.teamvoided.dusk_debris.init

import net.minecraft.block.AbstractBlock
import net.minecraft.block.Block
import net.minecraft.block.FluidBlock
import net.minecraft.block.MapColor
import net.minecraft.block.piston.PistonBehavior
import net.minecraft.fluid.FlowableFluid
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.sound.BlockSoundGroup
import org.teamvoided.dusk_debris.DuskDebris
import org.teamvoided.dusk_debris.fluid.AcidFluid

object DuskFluids {
    val FLUIDS = mutableSetOf<FlowableFluid>()

    val ACID: FlowableFluid = register("acid", AcidFluid.Still())
    val FLOWING_ACID: FlowableFluid = register("flowing_acid", AcidFluid.Flowing())

    val ACID_BLOCK = DuskBlocks.registerNoItem(
        "acid", FluidBlock(
            ACID,
            AbstractBlock.Settings.create().mapColor(MapColor.WATER).replaceable().noCollision().strength(100.0f)
                .pistonBehavior(PistonBehavior.DESTROY).dropsNothing().liquid().sounds(BlockSoundGroup.EMPTY)
        )
    )

    fun init() {}


    private fun register(id: String, fluid: FlowableFluid): FlowableFluid {
        val regFluid = Registry.register(Registries.FLUID, DuskDebris.id(id), fluid)
        FLUIDS.add(regFluid)
        return regFluid
    }
}