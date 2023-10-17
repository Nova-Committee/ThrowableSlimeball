package committee.nova.throwableslimeball.fabric.common.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class CommonConfig {
    public static final ForgeConfigSpec CFG;
    public static final ForgeConfigSpec.IntValue slimeBallMaxBounceTimes;
    public static final ForgeConfigSpec.IntValue magmaCreamMaxBounceTimes;
    public static final ForgeConfigSpec.DoubleValue slimeBallSpeedDecay;
    public static final ForgeConfigSpec.DoubleValue magmaCreamSpeedDecay;

    static {
        final ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
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
