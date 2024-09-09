package committee.nova.throwableslimeball.common.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public class CommonConfig {
    public static final ModConfigSpec CFG;
    public static final ModConfigSpec.IntValue slimeBallMaxBounceTimes;
    public static final ModConfigSpec.IntValue magmaCreamMaxBounceTimes;
    public static final ModConfigSpec.DoubleValue slimeBallSpeedDecay;
    public static final ModConfigSpec.DoubleValue magmaCreamSpeedDecay;

    static {
        final ModConfigSpec.Builder builder = new ModConfigSpec.Builder();
        builder.push("Throwable Settings");
        slimeBallMaxBounceTimes = builder.comment("The max times that a slime ball can bounce")
                .defineInRange("slimeBallMaxBounceTimes", 3, 0, 12);
        magmaCreamMaxBounceTimes = builder.comment("The max times that a magma cream can bounce")
                .defineInRange("magmaCreamMaxBounceTimes", 3, 0, 12);
        slimeBallSpeedDecay = builder.comment("The decay of a slime ball's speed after bouncing")
                .defineInRange("slimeBallSpeedDecay", .3, .0, 1.0);
        magmaCreamSpeedDecay = builder.comment("The decay of a slime ball's speed after bouncing")
                .defineInRange("magmaCreamSpeedDecay", .4, .0, 1.0);
        builder.pop();
        CFG = builder.build();
    }
}
