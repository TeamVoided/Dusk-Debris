package org.teamvoided.dusk_debris.init

import net.minecraft.block.Block
import net.minecraft.block.dispenser.DispenserBlock
import net.minecraft.block.dispenser.ItemDispenserBehavior
import net.minecraft.component.DataComponentTypes
import net.minecraft.component.type.AttributeModifiersComponent
import net.minecraft.component.type.DyedColorComponent
import net.minecraft.component.type.NbtComponent
import net.minecraft.entity.Entity
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.*
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.util.Hand
import net.minecraft.util.Rarity
import net.minecraft.util.TypedActionResult
import net.minecraft.util.math.BlockPointer
import net.minecraft.world.World
import net.minecraft.world.event.GameEvent
import org.teamvoided.dusk_debris.DuskDebris.id
import org.teamvoided.dusk_debris.block.DuskBlockLists
import org.teamvoided.dusk_debris.block.GunpowderBarrelBlock
import org.teamvoided.dusk_debris.entity.GunpowderBarrelEntity
import org.teamvoided.dusk_debris.item.*
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
    val ITEMS = mutableListOf<Item>()

    val TINY_JELLYFISH = register(
        "tiny_jellyfish", EntityItem(
            DuskEntities.TINY_ENEMY_JELLYFISH, SoundEvents.ITEM_BUCKET_EMPTY_TADPOLE,
            Item.Settings().maxCount(1).component(DataComponentTypes.BUCKET_ENTITY_DATA, NbtComponent.DEFAULT)
        )
    )
    val DEBUG_SPELL_ITEM = register("debug_spell_item", DebugSpellItem(Item.Settings().maxCount(1).rarity(Rarity.EPIC)))

    val MACE_BLAZE = register(
        "mace_blaze",
        MaceBlazeItem(
            Item.Settings().rarity(net.minecraft.util.Rarity.EPIC).maxDamage(500)
                .component(DataComponentTypes.TOOL, MaceBlazeItem.createToolComponent())
                .attributeModifiersComponent(MaceBlazeItem.createAttributes())
        )
    )


    val TWISTING_SOUL_CHARGE = register("twisting_soul_charge", ThrowableItem(Item.Settings()))

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

    val TREACHEROUS_GOLD_COINS = register(
        "treacherous_gold_coins", BlockItem(DuskBlocks.TREACHEROUS_GOLD_COIN_STACK, Item.Settings())
    )
    val TREACHEROUS_ASSORTED_GOLD_COINS = register(
        "treacherous_assorted_gold_coins", BlockItem(DuskBlocks.TREACHEROUS_GOLD_COIN_PILE, Item.Settings())
    )
    val TARNISHED_GOLD_COINS = register(
        "tarnished_gold_coins", BlockItem(DuskBlocks.TARNISHED_GOLD_COIN_STACK, Item.Settings())
    )
    val TARNISHED_ASSORTED_GOLD_COINS = register(
        "tarnished_assorted_gold_coins", BlockItem(DuskBlocks.TARNISHED_GOLD_COIN_PILE, Item.Settings())
    )
    val LOST_SILVER_COINS = register(
        "lost_silver_coins", BlockItem(DuskBlocks.LOST_SILVER_COIN_STACK, Item.Settings())
    )
    val LOST_ASSORTED_SILVER_COINS = register(
        "lost_assorted_silver_coins", BlockItem(DuskBlocks.LOST_SILVER_COIN_PILE, Item.Settings())
    )
    val SUNKEN_BRONZE_COINS = register(
        "sunken_bronze_coins", BlockItem(DuskBlocks.SUNKEN_BRONZE_COIN_STACK, Item.Settings())
    )
    val SUNKEN_ASSORTED_BRONZE_COINS = register(
        "sunken_assorted_bronze_coins", BlockItem(DuskBlocks.SUNKEN_BRONZE_COIN_PILE, Item.Settings())
    )

    val BOG_MUD_BUCKET = register(
        "bog_mud_bucket",
        PowderSnowBucketItem(DuskBlocks.BOG_MUD, SoundEvents.ITEM_BUCKET_EMPTY, Item.Settings())
    )

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

    val SEQUOIA_SIGN = register(
        "sequoia_sign",
        SignItem((Item.Settings()).maxCount(16), DuskBlocks.SEQUOIA_SIGN, DuskBlocks.SEQUOIA_WALL_SIGN)
    )
    val SEQUOIA_HANGING_SIGN = register(
        "sequoia_hanging_sign",
        HangingSignItem(
            DuskBlocks.SEQUOIA_HANGING_SIGN,
            DuskBlocks.SEQUOIA_WALL_HANGING_SIGN,
            Item.Settings().maxCount(16)
        )
    )

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

    // DnD Items
    val GALLERY_MAPLE_DOOR =
        register("gallery_maple_door", TallBlockItem(DuskBlocks.GALLERY_MAPLE_DOOR, Item.Settings()))

    val GALLERY_MAPLE_SIGN = register(
        "gallery_maple_sign",
        SignItem(CountSettings(16), DuskBlocks.GALLERY_MAPLE_SIGN, DuskBlocks.GALLERY_MAPLE_WALL_SIGN)
    )
    val GALLERY_MAPLE_HANGING_SIGN = register(
        "gallery_maple_hanging_sign", HangingSignItem(
            DuskBlocks.GALLERY_MAPLE_HANGING_SIGN, DuskBlocks.GALLERY_MAPLE_WALL_HANGING_SIGN, CountSettings(16)
        )
    )
    val BONEWOOD_DOOR = register("bonewood_door", TallBlockItem(DuskBlocks.BONEWOOD_DOOR, Item.Settings()))

    val WITHERING_BONEWOOD_DOOR =
        register("withering_bonewood_door", TallBlockItem(DuskBlocks.WITHERING_BONEWOOD_DOOR, Item.Settings()))


    val WITCH_HAT = register("witch_hat", EquipableItem(CountSettings(1)))

    @JvmField
    val VILE_WITCH_HAT = register("vile_witch_hat", EquipableItem(CountSettings(1)))
    val DIE_ITEM = register(
        "die", DiceItem(
            CountSettings(16).component(DataComponentTypes.DYED_COLOR, DyedColorComponent(0xFFFFFF, true))
        )
    )

    val WATER_FERN = register("water_fern", WaterPlaceableBlockItem(DuskBlocks.WATER_FERN, Item.Settings()))


    val FREEZE_ROD = register("freeze_rod", Item(Item.Settings()))
    val CHILL_CHARGE = register("chill_charge", ChillChargeItem(Item.Settings()))

    val WEB_WEAVER =
        register("web_weaver", BowItem(Item.Settings().maxDamage(404)))
    val HARVESTER_SCYTHE = register(
        "harvester_scythe", HarvesterScytheItem(AttributeSettings(HarvesterScytheItem.makeAttributes()))
    )
    val BROOM = register("broom", BroomItem(CountSettings(1)))

    fun init() {
        DuskBlockLists.THROWABLE_BOMB_BLOCK_LIST.forEach { DispenserBlock.registerBehavior(it.asItem()) }
        DuskBlockLists.GUNPOWDER_BARREL_BLOCK_LIST.forEach { registerGunpowderDispensedBehavior(it) }

        DispenserBlock.registerBehavior(CHILL_CHARGE)
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

    fun register(id: String, item: Item): Item {
        val regItem = Registry.register(Registries.ITEM, id(id), item)
        ITEMS.add(regItem)
        return regItem
    }

    fun BlockItem(block: Block) = BlockItem(block, Item.Settings())


    // TODO replace with voidlib
    class EquipableItem(settings: Settings, val slot: EquipmentSlot = EquipmentSlot.HEAD) : Item(settings), Equippable {
        override fun getPreferredSlot(): EquipmentSlot = slot
        override fun use(world: World, user: PlayerEntity, hand: Hand): TypedActionResult<ItemStack> =
            this.use(this, world, user, hand)
    }

    @Suppress("FunctionName")
    fun AttributeSettings(comp: AttributeModifiersComponent): Item.Settings =
        Item.Settings().attributeModifiersComponent(comp)

    @Suppress("FunctionName")
    fun CountSettings(count: Int): Item.Settings = Item.Settings().maxCount(count)
}