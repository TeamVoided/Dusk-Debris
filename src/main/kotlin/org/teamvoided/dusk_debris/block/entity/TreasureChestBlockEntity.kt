//package org.teamvoided.dusk_debris.block.entity
//
//import net.minecraft.block.BlockState
//import net.minecraft.block.entity.BlockEntity
//import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket
//import net.minecraft.util.math.BlockPos
//import org.teamvoided.dusk_debris.block.TreasureChestBlock
//import org.teamvoided.dusk_debris.init.DuskBlockEntities.TREASURE_CHEST
//
//class TreasureChestBlockEntity : BlockEntity {
//    var type: String
//
//    constructor(pos: BlockPos, state: BlockState) : super(TREASURE_CHEST, pos, state) {
//        this.type = (state.block as TreasureChestBlock).getType()
//    }
//
//    constructor(pos: BlockPos, state: BlockState, type: String) : super(TREASURE_CHEST, pos, state) {
//        this.type = type
//    }
//
//    override fun toUpdatePacket(): BlockEntityUpdateS2CPacket {
//        return BlockEntityUpdateS2CPacket.of(this)
//    }
//}