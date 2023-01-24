package alexthw.ars_elemental.common.glyphs;

import alexthw.ars_elemental.util.GlyphEffectUtil;
import com.hollingsworth.arsnouveau.api.spell.*;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentAOE;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentAmplify;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentPierce;
import com.hollingsworth.arsnouveau.common.spell.effect.EffectConjureWater;
import com.hollingsworth.arsnouveau.common.spell.effect.EffectCrush;
import com.hollingsworth.arsnouveau.common.spell.effect.EffectSmelt;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import com.hollingsworth.arsnouveau.common.lib.GlyphLib;


import javax.annotation.Nonnull;
import java.util.Map;
import java.util.Set;
import java.util.HashMap;

public class EffectConjureTerrain extends ElementalAbstractEffect {

    public static EffectConjureTerrain INSTANCE = new EffectConjureTerrain();

    private EffectConjureTerrain() {
        super("conjure_terrain", "Conjure Terrain");
    }

    @Override
    public void onResolveBlock(BlockHitResult rayTraceResult, Level world, LivingEntity shooter, SpellStats spellStats, SpellContext spellContext, SpellResolver resolver) {
        int amps = spellStats.getBuffCount(AugmentAmplify.INSTANCE);
        BlockState toPlace = switch (amps){
            default -> Blocks.DIRT.defaultBlockState();
            case 1 -> Blocks.COBBLESTONE.defaultBlockState();
            case 2 -> Blocks.COBBLED_DEEPSLATE.defaultBlockState();
        };
        if (spellContext.hasNextPart()) {
            while (spellContext.hasNextPart()) {
                AbstractSpellPart next = spellContext.nextPart();
                if (next instanceof AbstractEffect) {
                    if (next == EffectConjureWater.INSTANCE) {
                        toPlace = Blocks.BROWN_CONCRETE.defaultBlockState();
                    } else if (next == EffectCrush.INSTANCE) {
                        toPlace = amps > 0 ? Blocks.SANDSTONE.defaultBlockState() : Blocks.SAND.defaultBlockState();
                    } else if (next == EffectSmelt.INSTANCE && amps > 0) {
                        toPlace = amps > 1 ? Blocks.DEEPSLATE.defaultBlockState() : Blocks.STONE.defaultBlockState();
                    } else {
                        spellContext.setCurrentIndex(spellContext.getCurrentIndex() - 1);
                    }
                    break;
                }
            }
        }
        GlyphEffectUtil.placeBlocks(rayTraceResult, world, shooter, spellStats, spellContext, resolver, toPlace);
    }

    @Override
    public int getDefaultManaCost() {
        return 20;
    }

    @Nonnull
    @Override
    public Set<AbstractAugment> getCompatibleAugments() {
        return augmentSetOf(AugmentAOE.INSTANCE, AugmentPierce.INSTANCE, AugmentAmplify.INSTANCE);
    }

    @Override
    protected Map<String, Integer> getDefaultAugmentLimits() {
        Map<String, Integer> map = new HashMap<>();
        map.put(GlyphLib.AugmentSensitiveID, 2);
        return map;
    }

    @NotNull
    @Override
    public Set<SpellSchool> getSchools() {
        return setOf(SpellSchools.CONJURATION, SpellSchools.ELEMENTAL_EARTH);
    }

    @Override
    public String getBookDescription() {
        return "Places terrain block at a location. Can place more blocks if augmented with AoE or Pierce";
    }

    @Override
    public SpellTier getTier() {
        return SpellTier.ONE;
    }

}
