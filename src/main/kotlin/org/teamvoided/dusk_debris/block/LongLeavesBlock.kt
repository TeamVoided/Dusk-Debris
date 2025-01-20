package org.teamvoided.dusk_debris.block

import net.minecraft.block.BlockState
import net.minecraft.block.LeavesBlock
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.WorldAccess
import kotlin.math.min

class LongLeavesBlock(settings: Settings) : LeavesBlock(settings) {

//    override fun getRandomTicks(state: BlockState): Boolean {
//        return state.get(DISTANCE) >= MAX_DISTANCE && !state.get(PERSISTENT)
//    }
//
//
//    override fun canDecay(state: BlockState): Boolean {
//        return !state.get(PERSISTENT) && state.get(DISTANCE) >= MAX_DISTANCE
//    }
//
//
//    private fun updateDistanceFromLogs(state: BlockState, world: WorldAccess, pos: BlockPos): BlockState {
//        var distance = MAX_DISTANCE
//        val mutable = BlockPos.Mutable()
//        val var5 = Direction.entries.toTypedArray()
//
//        for (direction in var5) {
//            mutable[pos] = direction
//            distance = min(distance, (getDistanceFromLog(world.getBlockState(mutable)) + 1))
//            if (distance == 1) {
//                break
//            }
//        }
//
//        return state.with(DISTANCE, distance)
//    }
//    private fun getDistanceFromLog(state: BlockState): Int {
//        return getOptionalDistanceFromLog(state).orElse(MAX_DISTANCE)
//    }
//
//
//    companion object {
//        val MAX_DISTANCE = 15
//    }
}