package net.techtastic.tat;

import com.jozufozu.flywheel.core.PartialModel;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;

public class TATPartials {
    public static final PartialModel
            OVEN_DOOR = block("oven/door"),
            DISTILLERY_JAR = block("distilley/jar"),
            CANDLE = new PartialModel(new ResourceLocation("minecraft", "block/candle_one")),
            CANDLE_LIT = new PartialModel(new ResourceLocation("minecraft", "block/candle_one_lit"))
    ;

    private static PartialModel block(String path) {
        return new PartialModel(new ResourceLocation(ToilAndTrouble.MOD_ID, "block/" + path));
    }

    private static PartialModel entity(String path) {
        return new PartialModel(new ResourceLocation(ToilAndTrouble.MOD_ID, "entity/" + path));
    }

    private static PartialModel item(String path) {
        return new PartialModel(new ResourceLocation(ToilAndTrouble.MOD_ID, "item/" + path));
    }

    public static void init() {
    }
}
