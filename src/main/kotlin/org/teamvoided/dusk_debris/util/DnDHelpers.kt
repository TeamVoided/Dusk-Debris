package org.teamvoided.dusk_debris.util

import net.minecraft.block.*
import net.minecraft.block.AbstractBlock.Settings
import net.minecraft.block.AbstractBlock.Settings.copy
import net.minecraft.block.piston.PistonBehavior
import net.minecraft.sound.BlockSoundGroup
import net.minecraft.sound.SoundEvents
import net.minecraft.util.shape.VoxelShape
import net.minecraft.util.shape.VoxelShapes
import org.teamvoided.dusk_debris.DuskDebris.id
import org.teamvoided.dusk_debris.block.HauntedGravestoneBlock
import org.teamvoided.dusk_debris.block.voided.sign.VoidCeilingHangingSignBlock
import org.teamvoided.dusk_debris.block.voided.sign.VoidSignBlock
import org.teamvoided.dusk_debris.block.voided.sign.VoidWallHangingSignBlock
import org.teamvoided.dusk_debris.block.voided.sign.VoidWallSignBlock
import org.teamvoided.dusk_debris.init.DuskBlocks.register

val bonewoodSound = BlockSoundGroup(
    1f,
    0.8f,
    SoundEvents.BLOCK_BONE_BLOCK_BREAK,
    SoundEvents.BLOCK_BONE_BLOCK_STEP,
    SoundEvents.BLOCK_BONE_BLOCK_PLACE,
    SoundEvents.BLOCK_BONE_BLOCK_HIT,
    SoundEvents.BLOCK_BONE_BLOCK_FALL
)
val witheringBonewoodSound = BlockSoundGroup(
    1f,
    0f,
    SoundEvents.BLOCK_BONE_BLOCK_BREAK,
    SoundEvents.BLOCK_BONE_BLOCK_STEP,
    SoundEvents.BLOCK_BONE_BLOCK_PLACE,
    SoundEvents.BLOCK_BONE_BLOCK_HIT,
    SoundEvents.BLOCK_BONE_BLOCK_FALL
)

val gravestoneShape: VoxelShape = VoxelShapes.union(
    Block.createCuboidShape(0.0, 0.0, 0.0, 2.0, 16.0, 6.0), //left
    Block.createCuboidShape(14.0, 0.0, 0.0, 16.0, 16.0, 6.0), //right
    Block.createCuboidShape(0.0, 13.0, 0.0, 16.0, 16.0, 6.0), //top
    Block.createCuboidShape(2.0, 0.0, 1.0, 14.0, 13.0, 5.0) //center
)
val centerGravestoneShape: VoxelShape = VoxelShapes.union(
    Block.createCuboidShape(0.0, 0.0, 5.0, 2.0, 16.0, 11.0), //left
    Block.createCuboidShape(14.0, 0.0, 5.0, 16.0, 16.0, 11.0), //right
    Block.createCuboidShape(0.0, 13.0, 5.0, 16.0, 16.0, 11.0), //top
    Block.createCuboidShape(2.0, 0.0, 6.0, 14.0, 13.0, 10.0) //center
)
val smallGravestoneShape: VoxelShape = Block.createCuboidShape(3.0, 0.0, 0.0, 13.0, 12.0, 2.0)
val centerSmallGravestoneShape: VoxelShape = Block.createCuboidShape(3.0, 0.0, 7.0, 13.0, 12.0, 9.0)


internal fun registerHGravestone(name: String, block: Block) = register(
    name, HauntedGravestoneBlock(gravestoneShape, centerGravestoneShape, Settings.copy(block).solid())
)

internal fun registerSmallHGravestone(name: String, block: Block) = register(
    name, HauntedGravestoneBlock(smallGravestoneShape, centerSmallGravestoneShape, Settings.copy(block))
)


fun AbstractBlock.Settings.luminance(lightLevel: Int): AbstractBlock.Settings = this.luminance { lightLevel }


// basic
fun stairsOf(block: Block): Block = StairsBlock(block.defaultState, copy(block))

fun slabOf(block: Block): Block = SlabBlock(copy(block))

fun fenceOf(block: Block): Block = FenceBlock(copy(block).solid())

fun fenceGateOf(woodType: WoodType, block: Block): Block =
    FenceGateBlock(woodType, copy(block).solid())

fun doorOf(blockSetType: BlockSetType, block: Block): Block =
    DoorBlock(blockSetType, copy(block).strength(3.0f).nonOpaque())

fun trapdoorOf(blockSetType: BlockSetType, block: Block): Block =
    TrapdoorBlock(blockSetType, copy(block).allowsSpawning(Blocks::nonSpawnable))

fun pressurePlateOf(blockSetType: BlockSetType, block: Block): Block =
    PressurePlateBlock(
        blockSetType,
        copy(block).noCollision().strength(0.5f).solid().pistonBehavior(PistonBehavior.DESTROY)
    )

fun signOf(woodType: WoodType, block: Block): Block =
    VoidSignBlock(id(""), woodType, copy(block).solid().noCollision().strength(1.0f))

fun wallSignOf(woodType: WoodType, block: Block, sign: Block): Block =
    VoidWallSignBlock(
        id(""),
        woodType, copy(block).solid().noCollision().strength(1.0f)
            .dropsLike(sign)
    )

fun hangingSignOf(woodType: WoodType, block: Block): Block =
    VoidCeilingHangingSignBlock(id(""), woodType, copy(block).solid().noCollision().strength(1.0f))

fun wallHangingSignOf(woodType: WoodType, block: Block, hangingSign: Block): Block =
    VoidWallHangingSignBlock(
        id(""),
        woodType, copy(block).solid().noCollision().strength(1.0f)
            .dropsLike(hangingSign)
    )