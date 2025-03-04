package org.teamvoided.dusk_debris.mixin.directional_sculk.blockstates;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.SculkShriekerBlockEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.SpawnUtil;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.teamvoided.dusk_debris.block.mixin.SculkDirectionalStuff;

@Mixin(SculkShriekerBlockEntity.class)
public class SculkShriekerBlockEntityMixin extends BlockEntity {
    @Shadow
    private int warningLevel;

    public SculkShriekerBlockEntityMixin(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Inject(method = "trySpawnWarden", at = @At("HEAD"), cancellable = true)
    public void trySpawnWardenBelow(ServerWorld world, CallbackInfoReturnable<Boolean> cir) {
        if (SculkDirectionalStuff.isNotUp(this.getCachedState())) {
            if (this.warningLevel >= 4) {
                for (int down = 0; down < 30; down++) {
                    var posDown = this.getPos().down(down);
                    var stateDown = world.getBlockState(posDown.down());
                    if (!stateDown.isIn(BlockTags.REPLACEABLE)) {
                        cir.setReturnValue(SpawnUtil.method_42122(EntityType.WARDEN, SpawnReason.TRIGGERED, world, posDown, 20, 5, 6, SpawnUtil.Strategy.field_39401).isPresent());
                        break;
                    }
                }
            }
            cir.cancel();
        }
    }
}