package org.teamvoided.dusk_debris.init

import net.minecraft.block.Block
import net.minecraft.block.dispenser.DispenserBlock
import net.minecraft.block.dispenser.ItemDispenserBehavior
import net.minecraft.component.DataComponentTypes
import net.minecraft.component.type.DyedColorComponent
import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.*
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.util.math.BlockPointer
import net.minecraft.world.World
import net.minecraft.world.event.GameEvent
import org.teamvoided.dusk_debris.DuskDebris.id
import org.teamvoided.dusk_debris.block.DuskBlockLists
import org.teamvoided.dusk_debris.block.GunpowderBarrelBlock
import org.teamvoided.dusk_debris.entity.GunpowderBarrelEntity
import org.teamvoided.dusk_debris.item.BonecallerBandanaItem
import org.teamvoided.dusk_debris.item.DuskItemLists
import org.teamvoided.dusk_debris.item.EquipableBlockItemItem
import org.teamvoided.dusk_debris.item.throwable_bomb.BlunderbombItem
import org.teamvoided.dusk_debris.item.throwable_bomb.BonecallerItem
import org.teamvoided.dusk_debris.item.throwable_bomb.FirebombItem
import org.teamvoided.dusk_debris.item.throwable_bomb.bonecaller.BoneboggerItem
import org.teamvoided.dusk_debris.item.throwable_bomb.bonecaller.BonechillerItem
import org.teamvoided.dusk_debris.item.throwable_bomb.bonecaller.BonewitherItem
import org.teamvoided.dusk_debris.item.throwable_bomb.bonecaller.ShadecallerItem
import org.teamvoided.dusk_debris.item.throwable_bomb.nethershroom_throwable_item.BlindbombItem
import org.teamvoided.dusk_debris.item.throwable_bomb.nethershroom_throwable_item.PocketpoisonItem
import org.teamvoided.dusk_debris.item.throwable_bomb.nethershroom_throwable_item.SmokebombItem


@Suppress("unused", "MemberVisibilityCanBePrivate")
object DuskItems {

    val BLUE_NETHERSHROOM = register("blue_nethershroom", BlockItem(DuskBlocks.BLUE_NETHERSHROOM))
    val BLUE_NETHERSHROOM_BLOCK = register("blue_nethershroom_block", BlockItem(DuskBlocks.BLUE_NETHERSHROOM_BLOCK))
    val PURPLE_NETHERSHROOM = register("purple_nethershroom", BlockItem(DuskBlocks.PURPLE_NETHERSHROOM))
    val PURPLE_NETHERSHROOM_BLOCK =
        register("purple_nethershroom_block", BlockItem(DuskBlocks.PURPLE_NETHERSHROOM_BLOCK))
    val NETHERSHROOM_STEM = register("nethershroom_stem", BlockItem(DuskBlocks.NETHERSHROOM_STEM))

    val DEVELOPER_GUNPOWDER_ITEM = register("developer_gunpowder_item", BlockItem(DuskBlocks.GUNPOWDER))
    val GUNPOWDER_BARREL = register("gunpowder_barrel", BlockItem(DuskBlocks.GUNPOWDER_BARREL))
    val STRONGHOLD_GUNPOWDER_BARREL = register(
        "stronghold_gunpowder_barrel",
        BlockItem(DuskBlocks.STRONGHOLD_GUNPOWDER_BARREL, Item.Settings().maxCount(16))
    )
    val ANCIENT_BLACK_POWDER_BARREL = register(
        "ancient_black_powder_barrel",
        BlockItem(DuskBlocks.ANCIENT_BLACK_POWDER_BARREL, Item.Settings().maxCount(1))
    )
    val BLUNDERBOMB_ITEM =
        register("blunderbomb", BlunderbombItem(DuskBlocks.BLUNDERBOMB_BLOCK, Item.Settings().maxCount(16)))
    val FIREBOMB_ITEM = register("firebomb", FirebombItem(DuskBlocks.FIREBOMB_BLOCK, Item.Settings().maxCount(16)))
    val BONECALLER_ITEM =
        register("bonecaller", BonecallerItem(DuskBlocks.BONECALLER_BLOCK, Item.Settings().maxCount(16)))
    val BONECHILLER_ITEM =
        register("bonechiller", BonechillerItem(DuskBlocks.BONECHILLER_BLOCK, Item.Settings().maxCount(16)))
    val BOGCALLER_ITEM =
        register("bogcaller", BoneboggerItem(DuskBlocks.BOGCALLER_BLOCK, Item.Settings().maxCount(16)))
    val BONEWITHER_ITEM =
        register("bonewither", BonewitherItem(DuskBlocks.BONEWITHER_BLOCK, Item.Settings().maxCount(16)))
    val SHADECALLER_ITEM =
        register("shadecaller", ShadecallerItem(DuskBlocks.SHADECALLER_BLOCK, Item.Settings().maxCount(16)))
    val BONECALLER_BANDANA =
        register(
            "bonecaller_bandana",
            BonecallerBandanaItem(
                Item.Settings().maxCount(1).component(DataComponentTypes.DYED_COLOR, DyedColorComponent(0x7F7F7F, true))
            )
        )

    val SMOKEBOMB_ITEM =
        register(
            "smokebomb",
            SmokebombItem(
                DuskBlocks.SMOKEBOMB_BLOCK,
                Item.Settings().maxCount(1).component(DataComponentTypes.DYED_COLOR, DyedColorComponent(0x7F7F7F, true))
            )
        )
    val BLINDBOMB_ITEM = register("blindbomb", BlindbombItem(DuskBlocks.BLINDBOMB_BLOCK, Item.Settings().maxCount(1)))
    val POCKETPOISON_ITEM =
        register("pocketpoison", PocketpoisonItem(DuskBlocks.POCKETPOISON_BLOCK, Item.Settings().maxCount(1)))
    val GLOOM_SPAWN_EGG =
        register("gloomed_spawn_egg", (SpawnEggItem(DuskEntities.GLOOM, 0x222222, 0x222222, Item.Settings())))

    val LIGHT_BLUE_RIBBON = register("light_blue_ribbon", BlockItem(DuskBlocks.LIGHT_BLUE_RIBBON))

    //    val STRAY_SKULL = register("stray_skull", BlockItem(DuskBlocks.STRAY_SKULL))
//    val BOGGED_SKULL = register("bogged_skull", BlockItem(DuskBlocks.BOGGED_SKULL))
//    val GLOOM_SKULL = register("gloom_skull", BlockItem(DuskBlocks.GLOOM_SKULL))
    val TREACHEROUS_GOLD_BLOCK = register(
        "treacherous_gold_block", BlockItem(DuskBlocks.TREACHEROUS_GOLD_BLOCK, Item.Settings())
    )
    val TARNISHED_GOLD_BLOCK = register(
        "tarnished_gold_block", BlockItem(DuskBlocks.TARNISHED_GOLD_BLOCK, Item.Settings())
    )
    val LOST_SILVER_BLOCK = register(
        "lost_silver_block", BlockItem(DuskBlocks.LOST_SILVER_BLOCK, Item.Settings())
    )
    val SUNKEN_BRONZE_BLOCK = register(
        "sunken_bronze_block", BlockItem(DuskBlocks.SUNKEN_BRONZE_BLOCK, Item.Settings())
    )
    val TREACHEROUS_GOLD_COINS = register(
        "gold_coins", BlockItem(DuskBlocks.TREACHEROUS_GOLD_COIN_STACK, Item.Settings().maxCount(64))
    )
    val TREACHEROUS_ASSORTED_GOLD_COINS = register(
        "assorted_gold_coins", BlockItem(DuskBlocks.TREACHEROUS_GOLD_COIN_PILE, Item.Settings().maxCount(64))
    )
    val GOLDEN_VESSEL =
        register("golden_vessel", BlockItem(DuskBlocks.GOLDEN_VESSEL, Item.Settings().maxCount(16)))
    val LAPIS_RELIC =
        register("lapis_relic", BlockItem(DuskBlocks.LAPIS_RELIC, Item.Settings().maxCount(16)))
    val GILDED_CHALICE = register(
        "gilded_chalice", BlockItem(DuskBlocks.GILDED_CHALICE, Item.Settings().maxCount(16))
    )
    val SILVERED_CHALICE = register(
        "silvered_chalice", BlockItem(DuskBlocks.SILVERED_CHALICE, Item.Settings().maxCount(16))
    )
    val GOLDEN_RUBY_CROWN =
        register(
            "golden_ruby_crown",
            EquipableBlockItemItem(DuskBlocks.GOLDEN_RUBY_CROWN, Item.Settings().maxCount(16))
        )
    val GOLDEN_SAPPHIRE_CROWN =
        register(
            "golden_sapphire_crown",
            EquipableBlockItemItem(DuskBlocks.GOLDEN_SAPPHIRE_CROWN, Item.Settings().maxCount(16))
        )
    val GOLDEN_QUARTZ_CROWN =
        register(
            "golden_quartz_crown",
            EquipableBlockItemItem(DuskBlocks.GOLDEN_QUARTZ_CROWN, Item.Settings().maxCount(16))
        )
    val LEGENDARY_CRYSTAL_CROWN =
        register(
            "legendary_crystal_crown",
            EquipableBlockItemItem(DuskBlocks.LEGENDARY_CRYSTAL_CROWN, Item.Settings().maxCount(16))
        )

    val PAPER_BLOCK = register("paper_block", BlockItem(DuskBlocks.PAPER_BLOCK))

    val BOG_MUD_BUCKET = register(
        "bog_mud_bucket",
        PowderSnowBucketItem(DuskBlocks.BOG_MUD, SoundEvents.ITEM_BUCKET_EMPTY, Item.Settings())
    )
    val CYPRESS_LOG = register("cypress_log", BlockItem(DuskBlocks.CYPRESS_LOG))
    val CYPRESS_WOOD = register("cypress_wood", BlockItem(DuskBlocks.CYPRESS_WOOD))
    val STRIPPED_CYPRESS_LOG = register("stripped_cypress_log", BlockItem(DuskBlocks.STRIPPED_CYPRESS_LOG))
    val STRIPPED_CYPRESS_WOOD = register("stripped_cypress_wood", BlockItem(DuskBlocks.STRIPPED_CYPRESS_WOOD))
    val CYPRESS_PLANKS = register("cypress_planks", BlockItem(DuskBlocks.CYPRESS_PLANKS))
    val CYPRESS_STAIRS = register("cypress_stairs", BlockItem(DuskBlocks.CYPRESS_STAIRS))
    val CYPRESS_SLAB = register("cypress_slab", BlockItem(DuskBlocks.CYPRESS_SLAB))
    val CYPRESS_DOOR = register("cypress_door", TallBlockItem(DuskBlocks.CYPRESS_DOOR, Item.Settings()))
    val CYPRESS_TRAPDOOR = register("cypress_trapdoor", BlockItem(DuskBlocks.CYPRESS_TRAPDOOR))
    val CYPRESS_FENCE = register("cypress_fence", BlockItem(DuskBlocks.CYPRESS_FENCE))
    val CYPRESS_FENCE_GATE = register("cypress_fence_gate", BlockItem(DuskBlocks.CYPRESS_FENCE_GATE))
    val CYPRESS_BUTTON = register("cypress_button", BlockItem(DuskBlocks.CYPRESS_BUTTON))
    val CYPRESS_PRESSURE_PLATE = register("cypress_pressure_plate", BlockItem(DuskBlocks.CYPRESS_PRESSURE_PLATE))
    val CYPRESS_SIGN = register(
        "cypress_sign",
        SignItem((Item.Settings()).maxCount(16), DuskBlocks.CYPRESS_SIGN, DuskBlocks.CYPRESS_WALL_SIGN)
    )
    val CYPRESS_HANGING_SIGN = register(
        "cypress_hanging_sign",
        HangingSignItem(
            DuskBlocks.CYPRESS_HANGING_SIGN,
            DuskBlocks.CYPRESS_WALL_HANGING_SIGN,
            Item.Settings().maxCount(16)
        )
    )

    val VOLCANIC_SAND = register("volcanic_sand", BlockItem(DuskBlocks.VOLCANIC_SAND))
    val SUSPICIOUS_VOLCANIC_SAND = register("suspicious_volcanic_sand", BlockItem(DuskBlocks.SUSPICIOUS_VOLCANIC_SAND))
    val ROARING_GEYSER = register("roaring_geyser", BlockItem(DuskBlocks.ROARING_GEYSER))
    val VOLCANIC_SANDSTONE = register("volcanic_sandstone", BlockItem(DuskBlocks.VOLCANIC_SANDSTONE))
    val VOLCANIC_SANDSTONE_STAIRS =
        register("volcanic_sandstone_stairs", BlockItem(DuskBlocks.VOLCANIC_SANDSTONE_STAIRS))
    val VOLCANIC_SANDSTONE_SLAB = register("volcanic_sandstone_slab", BlockItem(DuskBlocks.VOLCANIC_SANDSTONE_SLAB))
    val VOLCANIC_SANDSTONE_WALL = register("volcanic_sandstone_wall", BlockItem(DuskBlocks.VOLCANIC_SANDSTONE_WALL))
    val CUT_VOLCANIC_SANDSTONE = register("cut_volcanic_sandstone", BlockItem(DuskBlocks.CUT_VOLCANIC_SANDSTONE))
    val CUT_VOLCANIC_SANDSTONE_SLAB =
        register("cut_volcanic_sandstone_slab", BlockItem(DuskBlocks.CUT_VOLCANIC_SANDSTONE_SLAB))
    val CHISELED_VOLCANIC_SANDSTONE =
        register("chiseled_volcanic_sandstone", BlockItem(DuskBlocks.CHISELED_VOLCANIC_SANDSTONE))
    val SMOOTH_VOLCANIC_SANDSTONE =
        register("smooth_volcanic_sandstone", BlockItem(DuskBlocks.SMOOTH_VOLCANIC_SANDSTONE))
    val SMOOTH_VOLCANIC_SANDSTONE_STAIRS =
        register("smooth_volcanic_sandstone_stairs", BlockItem(DuskBlocks.SMOOTH_VOLCANIC_SANDSTONE_STAIRS))
    val SMOOTH_VOLCANIC_SANDSTONE_SLAB =
        register("smooth_volcanic_sandstone_slab", BlockItem(DuskBlocks.SMOOTH_VOLCANIC_SANDSTONE_SLAB))

    @JvmField
    val BLACKSTONE_SWORD = register(
        "blackstone_sword", (SwordItem(
            ToolMaterials.STONE,
            (Item.Settings()).attributeModifiersComponent(
                SwordItem.createAttributes(
                    ToolMaterials.STONE,
                    3,
                    -2.4f
                )
            )
        ))
    )
    val BLACKSTONE_PICKAXE = register(
        "blackstone_pickaxe", (PickaxeItem(
            ToolMaterials.STONE,
            (Item.Settings()).attributeModifiersComponent(
                PickaxeItem.createAttributes(
                    ToolMaterials.STONE,
                    1.0f,
                    -2.8f
                )
            )
        ))
    )

    @JvmField
    val BLACKSTONE_AXE = register(
        "blackstone_axe", (AxeItem(
            ToolMaterials.STONE,
            (Item.Settings()).attributeModifiersComponent(
                AxeItem.createAttributes(
                    ToolMaterials.STONE,
                    7.0f,
                    -3.2f
                )
            )
        ))
    )
    val BLACKSTONE_SHOVEL = register(
        "blackstone_shovel", (ShovelItem(
            ToolMaterials.STONE,
            (Item.Settings()).attributeModifiersComponent(
                ShovelItem.createAttributes(
                    ToolMaterials.STONE,
                    1.5f,
                    -3.0f
                )
            )
        ))
    )
    val BLACKSTONE_HOE = register(
        "blackstone_hoe", (HoeItem(
            ToolMaterials.STONE,
            (Item.Settings()).attributeModifiersComponent(
                HoeItem.createAttributes(
                    ToolMaterials.STONE,
                    -1.0f,
                    -2.0f
                )
            )
        ))
    )


    val CHARRED_LOG = register("charred_log", BlockItem(DuskBlocks.CHARRED_LOG))
    val CHARRED_WOOD = register("charred_wood", BlockItem(DuskBlocks.CHARRED_WOOD))
    val STRIPPED_CHARRED_LOG = register("stripped_charred_log", BlockItem(DuskBlocks.STRIPPED_CHARRED_LOG))
    val STRIPPED_CHARRED_WOOD = register("stripped_charred_wood", BlockItem(DuskBlocks.STRIPPED_CHARRED_WOOD))
    val CHARRED_PLANKS = register("charred_planks", BlockItem(DuskBlocks.CHARRED_PLANKS))
    val CHARRED_STAIRS = register("charred_stairs", BlockItem(DuskBlocks.CHARRED_STAIRS))
    val CHARRED_SLAB = register("charred_slab", BlockItem(DuskBlocks.CHARRED_SLAB))
    val CHARRED_DOOR = register("charred_door", TallBlockItem(DuskBlocks.CHARRED_DOOR, Item.Settings()))
    val CHARRED_TRAPDOOR = register("charred_trapdoor", BlockItem(DuskBlocks.CHARRED_TRAPDOOR))
    val CHARRED_FENCE = register("charred_fence", BlockItem(DuskBlocks.CHARRED_FENCE))
    val CHARRED_FENCE_GATE = register("charred_fence_gate", BlockItem(DuskBlocks.CHARRED_FENCE_GATE))
    val CHARRED_BUTTON = register("charred_button", BlockItem(DuskBlocks.CHARRED_BUTTON))
    val CHARRED_PRESSURE_PLATE = register("charred_pressure_plate", BlockItem(DuskBlocks.CHARRED_PRESSURE_PLATE))
    val CHARRED_SIGN = register(
        "charred_sign",
        SignItem((Item.Settings()).maxCount(16), DuskBlocks.CHARRED_SIGN, DuskBlocks.CHARRED_WALL_SIGN)
    )
    val CHARRED_HANGING_SIGN = register(
        "charred_hanging_sign",
        HangingSignItem(
            DuskBlocks.CHARRED_HANGING_SIGN,
            DuskBlocks.CHARRED_WALL_HANGING_SIGN,
            Item.Settings().maxCount(16)
        )
    )

    fun init() {
        DuskItemLists.THROWABLE_BOMB_ITEM_LIST.forEach {
            DispenserBlock.registerBehavior(it)
        }
        DuskBlockLists.GUNPOWDER_BARREL_BLOCK_LIST.forEach {
            registerGunpowderDispensedBehavior(it)
        }
    }

    fun registerGunpowderDispensedBehavior(block: Block) =
        DispenserBlock.registerBehavior(block, object : ItemDispenserBehavior() {
            override fun dispenseSilently(pointer: BlockPointer, stack: ItemStack): ItemStack {
                val world: World = pointer.world()
                val blockPos = pointer.pos().offset(pointer.state().get(DispenserBlock.FACING))
                val explosiveEntity = GunpowderBarrelEntity(
                    world,
                    blockPos.x.toDouble() + 0.5,
                    blockPos.y.toDouble(),
                    blockPos.z.toDouble() + 0.5,
                    null as LivingEntity?
                )
                explosiveEntity.setProperties(
                    (block as GunpowderBarrelBlock).power,
                    (block).range,
                    block.defaultState,
//                world.getBlockState(blockPos) LMAO
                    block.color
                )
                world.spawnEntity(explosiveEntity)
                world.playSound(
                    null as PlayerEntity?,
                    explosiveEntity.x,
                    explosiveEntity.y,
                    explosiveEntity.z,
                    SoundEvents.ENTITY_TNT_PRIMED,
                    SoundCategory.BLOCKS,
                    1.0f,
                    1.0f
                )
                world.emitGameEvent(null as Entity?, GameEvent.ENTITY_PLACE, blockPos)
                stack.decrement(1)
                return stack
            }
        })

    fun register(id: String, item: Item): Item = Registry.register(Registries.ITEM, id(id), item)

    fun BlockItem(block: Block) = BlockItem(block, Item.Settings())
}