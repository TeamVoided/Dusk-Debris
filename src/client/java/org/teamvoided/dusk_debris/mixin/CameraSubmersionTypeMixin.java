package org.teamvoided.dusk_debris.mixin;

import net.minecraft.client.render.CameraSubmersionType;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.Arrays;

@Debug(export = true)
@Mixin(CameraSubmersionType.class)
public class CameraSubmersionTypeMixin {
//    @Mutable
//    @Shadow
//    @Final
//    private static CameraSubmersionType[] field_27889;
//
//    @Invoker("<init>")
//    private static CameraSubmersionType invokeInit(String name) {
//        throw new AssertionError();
//    }
//
//    @Inject(method = "<clinit>", at = @At("RETURN"))
//    private static void clInit(CallbackInfo ci) {
//        register("CUSTOM");
//    }
//
//    @SuppressWarnings({"UnusedReturnValue"})
//    @Unique
//    private static CameraSubmersionType register(String name) {
//        ArrayList<CameraSubmersionType> values = new ArrayList<>(Arrays.asList(field_27889));
//        CameraSubmersionType type = invokeInit(name);
//        values.add(type);
//        field_27889 = values.toArray(new CameraSubmersionType[]{});
//        return type;
//    }
}