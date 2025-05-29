package org.teamvoided.dusk_debris.block

import com.mojang.serialization.MapCodec
import net.minecraft.block.BlockRenderType
import net.minecraft.block.BlockState
import net.minecraft.block.BlockWithEntity
import net.minecraft.block.entity.BlockEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.ActionResult
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import org.teamvoided.dusk_debris.block.entity.StatueBlockEntity
import org.teamvoided.dusk_debris.util.openStatuesScreen

class StatueBlock(settings: Settings) : BlockWithEntity(settings) {
    override fun getCodec(): MapCodec<StatueBlock> = createCodec(::StatueBlock)
    override fun createBlockEntity(pos: BlockPos, state: BlockState): BlockEntity = StatueBlockEntity(pos, state)
    override fun getRenderType(state: BlockState) = BlockRenderType.MODEL
    override fun onUse(
        state: BlockState, world: World, pos: BlockPos, entity: PlayerEntity, hitResult: BlockHitResult,
    ): ActionResult {
        val statue = world.getBlockEntity(pos)
        return if (statue is StatueBlockEntity) {
            entity.openStatuesScreen(statue)
            ActionResult.success(world.isClient)
        } else ActionResult.PASS
    }
}