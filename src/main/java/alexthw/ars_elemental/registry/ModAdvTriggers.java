package alexthw.ars_elemental.registry;

import alexthw.ars_elemental.datagen.advancement.ANCriteriaTriggers;
import net.minecraft.advancements.critereon.PlayerInteractTrigger;

import static alexthw.ars_elemental.ArsElemental.prefix;

public class ModAdvTriggers extends ANCriteriaTriggers {

    public static void init() {}

    public static final PlayerInteractTrigger MIRROR = register(new PlayerInteractTrigger(prefix("mirror_shield")));

    public static final PlayerInteractTrigger BLOSSOM = register(new PlayerInteractTrigger(prefix("blossoming")));

    public static final PlayerInteractTrigger LEVITATE = register(new PlayerInteractTrigger(prefix("levitating")));


}
