package org.teamvoided.dusk_debris.init

import net.minecraft.core.Registry
import net.minecraft.core.component.DataComponents
import net.minecraft.core.dispenser.BlockSource
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.*
import net.minecraft.world.item.component.CustomData
import net.minecraft.world.item.component.DyedItemColor
import net.minecraft.world.item.component.ItemAttributeModifiers
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.DispenserBlock
import net.minecraft.world.level.gameevent.GameEvent
import org.teamvoided.dusk_debris.DuskDebris.id
import org.teamvoided.dusk_debris.block.DuskBlockLists
import org.teamvoided.dusk_debris.block.sot.GunpowderBarrelBlock
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
            DuskEntities.TINY_ENEMY_JELLYFISH, SoundEvents.BUCKET_EMPTY_TADPOLE,
            Item.Properties().stacksTo(1).component(DataComponents.BUCKET_ENTITY_DATA, CustomData.EMPTY)
        )
    )
    val DEBUG_SPELL_ITEM = register("debug_spell_item", DebugSpellItem(Item.Properties().stacksTo(1).rarity(Rarity.EPIC)))

    val MACE_BLAZE = register(
        "mace_blaze",
        MaceBlazeItem(
            Item.Properties().rarity(net.minecraft.world.item.Rarity.EPIC).durability(500)
                .component(DataComponents.TOOL, MaceBlazeItem.createToolComponent())
                .attributes(MaceBlazeItem.createAttributes())
        )
    )


    val TWISTING_SOUL_CHARGE = register("twisting_soul_charge", ThrowableItem(Item.Properties()))

    val BLUNDERBOMB_ITEM =
        register("blunderbomb", BlunderbombItem(DuskBlocks.BLUNDERBOMB_BLOCK, Item.Properties().stacksTo(16)))
    val FIREBOMB_ITEM = register("firebomb", FirebombItem(DuskBlocks.FIREBOMB_BLOCK, Item.Properties().stacksTo(16)))
    val BONECALLER_ITEM =
        register("bonecaller", BonecallerItem(DuskBlocks.BONECALLER_BLOCK, Item.Properties().stacksTo(16)))
    val BONECHILLER_ITEM =
        register("bonechiller", BonechillerItem(DuskBlocks.BONECHILLER_BLOCK, Item.Properties().stacksTo(16)))
    val BOGCALLER_ITEM =
        register("bogcaller", BoneboggerItem(DuskBlocks.BOGCALLER_BLOCK, Item.Properties().stacksTo(16)))
    val BONEWITHER_ITEM =
        register("bonewither", BonewitherItem(DuskBlocks.BONEWITHER_BLOCK, Item.Properties().stacksTo(16)))
    val SHADECALLER_ITEM =
        register("shadecaller", ShadecallerItem(DuskBlocks.SHADECALLER_BLOCK, Item.Properties().stacksTo(16)))
    val BONECALLER_BANDANA =
        register(
            "bonecaller_bandana",
            BonecallerBandanaItem(
                Item.Properties().stacksTo(1).component(DataComponents.DYED_COLOR, DyedItemColor(0x7F7F7F, true))
            )
        )

    val SMOKEBOMB_ITEM =
        register(
            "smokebomb",
            SmokebombItem(
                DuskBlocks.SMOKEBOMB_BLOCK,
                Item.Properties().stacksTo(1).component(DataComponents.DYED_COLOR, DyedItemColor(0x7F7F7F, true))
            )
        )
    val BLINDBOMB_ITEM = register("blindbomb", BlindbombItem(DuskBlocks.BLINDBOMB_BLOCK, Item.Properties().stacksTo(1)))
    val POCKETPOISON_ITEM =
        register("pocketpoison", PocketpoisonItem(DuskBlocks.POCKETPOISON_BLOCK, Item.Properties().stacksTo(1)))

    val TREACHEROUS_GOLD_COINS = register(
        "treacherous_gold_coins", BlockItem(DuskBlocks.TREACHEROUS_GOLD_COIN_STACK, Item.Properties())
    )
    val TREACHEROUS_ASSORTED_GOLD_COINS = register(
        "treacherous_assorted_gold_coins", BlockItem(DuskBlocks.TREACHEROUS_GOLD_COIN_PILE, Item.Properties())
    )
    val TARNISHED_GOLD_COINS = register(
        "tarnished_gold_coins", BlockItem(DuskBlocks.TARNISHED_GOLD_COIN_STACK, Item.Properties())
    )
    val TARNISHED_ASSORTED_GOLD_COINS = register(
        "tarnished_assorted_gold_coins", BlockItem(DuskBlocks.TARNISHED_GOLD_COIN_PILE, Item.Properties())
    )
    val LOST_SILVER_COINS = register(
        "lost_silver_coins", BlockItem(DuskBlocks.LOST_SILVER_COIN_STACK, Item.Properties())
    )
    val LOST_ASSORTED_SILVER_COINS = register(
        "lost_assorted_silver_coins", BlockItem(DuskBlocks.LOST_SILVER_COIN_PILE, Item.Properties())
    )
    val SUNKEN_BRONZE_COINS = register(
        "sunken_bronze_coins", BlockItem(DuskBlocks.SUNKEN_BRONZE_COIN_STACK, Item.Properties())
    )
    val SUNKEN_ASSORTED_BRONZE_COINS = register(
        "sunken_assorted_bronze_coins", BlockItem(DuskBlocks.SUNKEN_BRONZE_COIN_PILE, Item.Properties())
    )

    val BOG_MUD_BUCKET = register(
        "bog_mud_bucket",
        SolidBucketItem(DuskBlocks.BOG_MUD, SoundEvents.BUCKET_EMPTY, Item.Properties())
    )

    val CYPRESS_SIGN = register(
        "cypress_sign",
        SignItem((Item.Properties()).stacksTo(16), DuskBlocks.CYPRESS_SIGN, DuskBlocks.CYPRESS_WALL_SIGN)
    )
    val CYPRESS_HANGING_SIGN = register(
        "cypress_hanging_sign",
        HangingSignItem(
            DuskBlocks.CYPRESS_HANGING_SIGN,
            DuskBlocks.CYPRESS_WALL_HANGING_SIGN,
            Item.Properties().stacksTo(16)
        )
    )

    val SEQUOIA_SIGN = register(
        "sequoia_sign",
        SignItem((Item.Properties()).stacksTo(16), DuskBlocks.SEQUOIA_SIGN, DuskBlocks.SEQUOIA_WALL_SIGN)
    )
    val SEQUOIA_HANGING_SIGN = register(
        "sequoia_hanging_sign",
        HangingSignItem(
            DuskBlocks.SEQUOIA_HANGING_SIGN,
            DuskBlocks.SEQUOIA_WALL_HANGING_SIGN,
            Item.Properties().stacksTo(16)
        )
    )

    val CHARRED_SIGN = register(
        "charred_sign",
        SignItem((Item.Properties()).stacksTo(16), DuskBlocks.CHARRED_SIGN, DuskBlocks.CHARRED_WALL_SIGN)
    )
    val CHARRED_HANGING_SIGN = register(
        "charred_hanging_sign",
        HangingSignItem(
            DuskBlocks.CHARRED_HANGING_SIGN,
            DuskBlocks.CHARRED_WALL_HANGING_SIGN,
            Item.Properties().stacksTo(16)
        )
    )

    // DnD Items
    val GALLERY_MAPLE_DOOR =
        register("gallery_maple_door", DoubleHighBlockItem(DuskBlocks.GALLERY_MAPLE_DOOR, Item.Properties()))

    val GALLERY_MAPLE_SIGN = register(
        "gallery_maple_sign",
        SignItem(CountSettings(16), DuskBlocks.GALLERY_MAPLE_SIGN, DuskBlocks.GALLERY_MAPLE_WALL_SIGN)
    )
    val GALLERY_MAPLE_HANGING_SIGN = register(
        "gallery_maple_hanging_sign", HangingSignItem(
            DuskBlocks.GALLERY_MAPLE_HANGING_SIGN, DuskBlocks.GALLERY_MAPLE_WALL_HANGING_SIGN, CountSettings(16)
        )
    )
    val BONEWOOD_DOOR = register("bonewood_door", DoubleHighBlockItem(DuskBlocks.BONEWOOD_DOOR, Item.Properties()))

    val WITHERING_BONEWOOD_DOOR =
        register("withering_bonewood_door", DoubleHighBlockItem(DuskBlocks.WITHERING_BONEWOOD_DOOR, Item.Properties()))


    val WITCH_HAT = register("witch_hat", EquipableItem(CountSettings(1)))

    @JvmField
    val VILE_WITCH_HAT = register("vile_witch_hat", EquipableItem(CountSettings(1)))
    val DIE_ITEM = register(
        "die", DiceItem(
            CountSettings(16).component(DataComponents.DYED_COLOR, DyedItemColor(0xFFFFFF, true))
        )
    )

    val WATER_FERN = register("water_fern", PlaceOnWaterBlockItem(DuskBlocks.WATER_FERN, Item.Properties()))


    val FREEZE_ROD = register("freeze_rod", Item(Item.Properties()))
    val CHILL_CHARGE = register("chill_charge", ChillChargeItem(Item.Properties()))

    val WEB_WEAVER =
        register("web_weaver", BowItem(Item.Properties().durability(404)))
    val HARVESTER_SCYTHE = register(
        "harvester_scythe", HarvesterScytheItem(AttributeSettings(HarvesterScytheItem.makeAttributes()))
    )
    val BROOM = register("broom", BroomItem(CountSettings(1)))

    fun init() {
        DuskBlockLists.THROWABLE_BOMB_BLOCK_LIST.forEach { DispenserBlock.registerProjectileBehavior(it.asItem()) }
        DuskBlockLists.GUNPOWDER_BARREL_BLOCK_LIST.forEach { registerGunpowderDispensedBehavior(it) }

        DispenserBlock.registerProjectileBehavior(CHILL_CHARGE)
    }

    fun registerGunpowderDispensedBehavior(block: Block) =
        DispenserBlock.registerBehavior(block, object : DefaultDispenseItemBehavior() {
            override fun execute(pointer: BlockSource, stack: ItemStack): ItemStack {
                val world: Level = pointer.level()
                val blockPos = pointer.pos().relative(pointer.state().getValue(DispenserBlock.FACING))
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
                    block.defaultBlockState(),
//                world.getBlockState(blockPos) LMAO
                    block.color
                )
                world.addFreshEntity(explosiveEntity)
                world.playSound(
                    null as Player?,
                    explosiveEntity.x,
                    explosiveEntity.y,
                    explosiveEntity.z,
                    SoundEvents.TNT_PRIMED,
                    SoundSource.BLOCKS,
                    1.0f,
                    1.0f
                )
                world.gameEvent(null as Entity?, GameEvent.ENTITY_PLACE, blockPos)
                stack.shrink(1)
                return stack
            }
        })

    fun register(id: String, item: Item): Item {
        val regItem = Registry.register(BuiltInRegistries.ITEM, id(id), item)
        ITEMS.add(regItem)
        return regItem
    }

    fun BlockItem(block: Block) = BlockItem(block, Item.Properties())


    // TODO replace with voidlib
    class EquipableItem(settings: Properties, val slot: EquipmentSlot = EquipmentSlot.HEAD) : Item(settings),
        Equipable {
        override fun getEquipmentSlot(): EquipmentSlot = slot
        override fun use(world: Level, user: Player, hand: InteractionHand): InteractionResultHolder<ItemStack> =
            this.swapWithEquipmentSlot(this, world, user, hand)
    }

    @Suppress("FunctionName")
    fun AttributeSettings(comp: ItemAttributeModifiers): Item.Properties =
        Item.Properties().attributes(comp)

    @Suppress("FunctionName")
    fun CountSettings(count: Int): Item.Properties = Item.Properties().stacksTo(count)
}