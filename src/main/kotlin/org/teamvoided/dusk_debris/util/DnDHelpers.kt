package org.teamvoided.dusk_debris.util

import net.minecraft.sounds.SoundEvents
import net.minecraft.world.level.block.*
import net.minecraft.world.level.block.state.BlockBehaviour
import net.minecraft.world.level.block.state.BlockBehaviour.Properties
import net.minecraft.world.level.block.state.BlockBehaviour.Properties.ofFullCopy
import net.minecraft.world.level.block.state.properties.BlockSetType
import net.minecraft.world.level.block.state.properties.WoodType
import net.minecraft.world.level.material.PushReaction
import net.minecraft.world.phys.shapes.Shapes
import net.minecraft.world.phys.shapes.VoxelShape
import org.teamvoided.dusk_debris.DuskDebris.id
import org.teamvoided.dusk_debris.block.HauntedGravestoneBlock
import org.teamvoided.dusk_debris.block.voided.sign.VoidCeilingHangingSignBlock
import org.teamvoided.dusk_debris.block.voided.sign.VoidSignBlock
import org.teamvoided.dusk_debris.block.voided.sign.VoidWallHangingSignBlock
import org.teamvoided.dusk_debris.block.voided.sign.VoidWallSignBlock
import org.teamvoided.dusk_debris.init.DuskBlocks.register

val bonewoodSound = SoundType(
    1f,
    0.8f,
    SoundEvents.BONE_BLOCK_BREAK,
    SoundEvents.BONE_BLOCK_STEP,
    SoundEvents.BONE_BLOCK_PLACE,
    SoundEvents.BONE_BLOCK_HIT,
    SoundEvents.BONE_BLOCK_FALL
)
val witheringBonewoodSound = SoundType(
    1f,
    0f,
    SoundEvents.BONE_BLOCK_BREAK,
    SoundEvents.BONE_BLOCK_STEP,
    SoundEvents.BONE_BLOCK_PLACE,
    SoundEvents.BONE_BLOCK_HIT,
    SoundEvents.BONE_BLOCK_FALL
)

val gravestoneShape: VoxelShape = Shapes.or(
    Block.box(0.0, 0.0, 0.0, 2.0, 16.0, 6.0), //left
    Block.box(14.0, 0.0, 0.0, 16.0, 16.0, 6.0), //right
    Block.box(0.0, 13.0, 0.0, 16.0, 16.0, 6.0), //top
    Block.box(2.0, 0.0, 1.0, 14.0, 13.0, 5.0) //center
)
val centerGravestoneShape: VoxelShape = Shapes.or(
    Block.box(0.0, 0.0, 5.0, 2.0, 16.0, 11.0), //left
    Block.box(14.0, 0.0, 5.0, 16.0, 16.0, 11.0), //right
    Block.box(0.0, 13.0, 5.0, 16.0, 16.0, 11.0), //top
    Block.box(2.0, 0.0, 6.0, 14.0, 13.0, 10.0) //center
)
val smallGravestoneShape: VoxelShape = Block.box(3.0, 0.0, 0.0, 13.0, 12.0, 2.0)
val centerSmallGravestoneShape: VoxelShape = Block.box(3.0, 0.0, 7.0, 13.0, 12.0, 9.0)


internal fun registerHGravestone(name: String, block: Block) = register(
    name, HauntedGravestoneBlock(gravestoneShape, centerGravestoneShape, Properties.ofFullCopy(block).forceSolidOn())
)

internal fun registerSmallHGravestone(name: String, block: Block) = register(
    name, HauntedGravestoneBlock(smallGravestoneShape, centerSmallGravestoneShape, Properties.ofFullCopy(block))
)


fun BlockBehaviour.Properties.luminance(lightLevel: Int): BlockBehaviour.Properties = this.lightLevel { lightLevel }


// basic
fun stairsOf(block: Block): Block = StairBlock(block.defaultBlockState(), ofFullCopy(block))

fun slabOf(block: Block): Block = SlabBlock(ofFullCopy(block))

fun fenceOf(block: Block): Block = FenceBlock(ofFullCopy(block).forceSolidOn())

fun fenceGateOf(woodType: WoodType, block: Block): Block =
    FenceGateBlock(woodType, ofFullCopy(block).forceSolidOn())

fun doorOf(blockSetType: BlockSetType, block: Block): Block =
    DoorBlock(blockSetType, ofFullCopy(block).strength(3.0f).noOcclusion())

fun trapdoorOf(blockSetType: BlockSetType, block: Block): Block =
    TrapDoorBlock(blockSetType, ofFullCopy(block).isValidSpawn(Blocks::never))

fun pressurePlateOf(blockSetType: BlockSetType, block: Block): Block =
    PressurePlateBlock(
        blockSetType,
        ofFullCopy(block).noCollission().strength(0.5f).forceSolidOn().pushReaction(PushReaction.DESTROY)
    )

fun signOf(woodType: WoodType, block: Block): Block =
    VoidSignBlock(id(""), woodType, ofFullCopy(block).forceSolidOn().noCollission().strength(1.0f))

fun wallSignOf(woodType: WoodType, block: Block, sign: Block): Block =
    VoidWallSignBlock(
        id(""),
        woodType, ofFullCopy(block).forceSolidOn().noCollission().strength(1.0f)
            .dropsLike(sign)
    )

fun hangingSignOf(woodType: WoodType, block: Block): Block =
    VoidCeilingHangingSignBlock(id(""), woodType, ofFullCopy(block).forceSolidOn().noCollission().strength(1.0f))

fun wallHangingSignOf(woodType: WoodType, block: Block, hangingSign: Block): Block =
    VoidWallHangingSignBlock(
        id(""),
        woodType, ofFullCopy(block).forceSolidOn().noCollission().strength(1.0f)
            .dropsLike(hangingSign)
    )