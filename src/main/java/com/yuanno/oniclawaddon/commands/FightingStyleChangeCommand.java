package com.yuanno.oniclawaddon.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.yuanno.oniclawaddon.abilities.dragonclaw.FlamingSlashAbility;
import com.yuanno.oniclawaddon.abilities.dragonclaw.GrabAbility;
import com.yuanno.oniclawaddon.abilities.dragonclaw.RisingSunAbility;
import com.yuanno.oniclawaddon.abilities.dragonclaw.TalonsAbility;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.ISuggestionProvider;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.TranslationTextComponent;
import xyz.pixelatedw.mineminenomi.abilities.artofweather.*;
import xyz.pixelatedw.mineminenomi.abilities.artofweather.tempos.*;
import xyz.pixelatedw.mineminenomi.abilities.blackleg.*;
import xyz.pixelatedw.mineminenomi.abilities.brawler.*;
import xyz.pixelatedw.mineminenomi.abilities.doctor.DopingAbility;
import xyz.pixelatedw.mineminenomi.abilities.doctor.FailedExperimentAbility;
import xyz.pixelatedw.mineminenomi.abilities.doctor.FirstAidAbility;
import xyz.pixelatedw.mineminenomi.abilities.doctor.MedicBagExplosionAbility;
import xyz.pixelatedw.mineminenomi.abilities.sniper.*;
import xyz.pixelatedw.mineminenomi.abilities.swordsman.*;
import xyz.pixelatedw.mineminenomi.api.abilities.AbilityCategory;
import xyz.pixelatedw.mineminenomi.data.entity.ability.AbilityDataCapability;
import xyz.pixelatedw.mineminenomi.data.entity.ability.IAbilityData;
import xyz.pixelatedw.mineminenomi.data.entity.entitystats.EntityStatsCapability;
import xyz.pixelatedw.mineminenomi.data.entity.entitystats.IEntityStats;
import xyz.pixelatedw.mineminenomi.events.abilities.AbilityProgressionEvents;
import xyz.pixelatedw.mineminenomi.init.ModAbilities;
import xyz.pixelatedw.mineminenomi.init.ModValues;
import xyz.pixelatedw.mineminenomi.packets.server.SSyncAbilityDataPacket;
import xyz.pixelatedw.mineminenomi.packets.server.SSyncEntityStatsPacket;
import xyz.pixelatedw.mineminenomi.wypi.WyNetwork;

import java.util.ArrayList;
import java.util.List;

public class FightingStyleChangeCommand {

    public static void register(CommandDispatcher<CommandSource> dispatcher)
    {
        dispatcher.register(Commands.literal("style").requires((commandSource) -> commandSource.hasPermission(2))
                .then(Commands.argument("target", EntityArgument.player())
                        .then(Commands.argument("style", StringArgumentType.string()).suggests(SUGGEST_SET)
                                .executes((command) ->
                                {
                                    String style = StringArgumentType.getString(command, "style");

                                    return setStyle(command.getSource(), EntityArgument.getPlayer(command, "target"), style);
                                }))));
    }

    private static final SuggestionProvider<CommandSource> SUGGEST_SET = (source, builder) -> {
        List<String> suggestions = new ArrayList<>();

        suggestions.add(ModValues.SWORDSMAN);
        suggestions.add(ModValues.SNIPER);
        suggestions.add(ModValues.DOCTOR);
        suggestions.add(ModValues.ART_OF_WEATHER);
        suggestions.add(ModValues.BLACK_LEG);
        suggestions.add(ModValues.BRAWLER);
        suggestions.add("dragonclaw");

        return ISuggestionProvider.suggest(suggestions.stream(), builder);
    };

    private static int setStyle(CommandSource commandSource, PlayerEntity player, String style)
    {
        IEntityStats entityStats = EntityStatsCapability.get(player);
        entityStats.setFightingStyle(style);
        IAbilityData abilityData = AbilityDataCapability.get(player);
        abilityData.clearUnlockedAbilities(AbilityCategory.STYLE.isPartofCategory());
        switch (style)
        {
            case (ModValues.SWORDSMAN):
                abilityData.addUnlockedAbility(ShiShishiSonsonAbility.INSTANCE);
                abilityData.addUnlockedAbility(YakkodoriAbility.INSTANCE);
                abilityData.addUnlockedAbility(SanbyakurokujuPoundHoAbility.INSTANCE);
                abilityData.addUnlockedAbility(OTatsumakiAbility.INSTANCE);
                abilityData.addUnlockedAbility(HiryuKaenAbility.INSTANCE);
                break;
            case (ModValues.SNIPER):
                abilityData.addUnlockedAbility(HiNoToriBoshiAbility.INSTANCE);
                abilityData.addUnlockedAbility(KemuriBoshiAbility.INSTANCE);
                abilityData.addUnlockedAbility(RenpatsuNamariBoshiAbility.INSTANCE);
                abilityData.addUnlockedAbility(SakuretsuSabotenBoshiAbility.INSTANCE);
                abilityData.addUnlockedAbility(TetsuBoshiAbility.INSTANCE);
                abilityData.addUnlockedAbility(TokuyoAburaBoshiAbility.INSTANCE);
                abilityData.addUnlockedAbility(NemuriBoshiAbility.INSTANCE);
                break;
            case (ModValues.DOCTOR):
                abilityData.addUnlockedAbility(FirstAidAbility.INSTANCE);
                abilityData.addUnlockedAbility(MedicBagExplosionAbility.INSTANCE);
                abilityData.addUnlockedAbility(FailedExperimentAbility.INSTANCE);
                abilityData.addUnlockedAbility(DopingAbility.INSTANCE);
                break;
            case (ModValues.ART_OF_WEATHER):
                abilityData.addUnlockedAbility(HeatBallAbility.INSTANCE);
                abilityData.addUnlockedAbility(CoolBallAbility.INSTANCE);
                abilityData.addUnlockedAbility(WeatherCloudTempo.INSTANCE);
                abilityData.addUnlockedAbility(ThunderBallAbility.INSTANCE);
                abilityData.addUnlockedAbility(ThunderboltTempo.INSTANCE);
                abilityData.addUnlockedAbility(ThunderstormTempo.INSTANCE);
                abilityData.addUnlockedAbility(ThunderLanceTempo.INSTANCE);
                abilityData.addUnlockedAbility(FogTempo.INSTANCE);
                abilityData.addUnlockedAbility(MirageTempo.INSTANCE);
                abilityData.addUnlockedAbility(RainTempo.INSTANCE);
                abilityData.addUnlockedAbility(WeatherEggAbility.INSTANCE);
                abilityData.addUnlockedAbility(GustSwordAbility.INSTANCE);
                break;
            case (ModValues.BLACK_LEG):
                abilityData.addUnlockedAbility(DiableJambeAbility.INSTANCE);
                abilityData.addUnlockedAbility(SkywalkAbility.INSTANCE);
                abilityData.addUnlockedAbility(AntiMannerKickCourseAbility.INSTANCE);
                abilityData.addUnlockedAbility(ExtraHachisAbility.INSTANCE);
                abilityData.addUnlockedAbility(PartyTableKickCourseAbility.INSTANCE);
                abilityData.addUnlockedAbility(ConcasseAbility.INSTANCE);
                abilityData.addUnlockedAbility(BienCuitGrillShotAbility.INSTANCE);
                break;
            case (ModValues.BRAWLER):
                abilityData.addUnlockedAbility(GenkotsuMeteorAbility.INSTANCE);
                abilityData.addUnlockedAbility(KingPunchAbility.INSTANCE);
                abilityData.addUnlockedAbility(HakaiHoAbility.INSTANCE);
                abilityData.addUnlockedAbility(JishinHoAbility.INSTANCE);
                abilityData.addUnlockedAbility(SpinningBrawlAbility.INSTANCE);
                abilityData.addUnlockedAbility(SuplexAbility.INSTANCE);
                break;
            case ("dragonclaw"):
                abilityData.addUnlockedAbility(TalonsAbility.INSTANCE);
                abilityData.addUnlockedAbility(RisingSunAbility.INSTANCE);
                abilityData.addUnlockedAbility(GrabAbility.INSTANCE);
                abilityData.addUnlockedAbility(FlamingSlashAbility.INSTANCE);
                break;
        }
        WyNetwork.sendTo(new SSyncEntityStatsPacket(player.getId(), entityStats), player);
        WyNetwork.sendTo(new SSyncAbilityDataPacket(player.getId(), abilityData), player);

        commandSource.sendSuccess(new TranslationTextComponent("set fighting style of " + player.getDisplayName().getString() + " to " + style), true);
        return 1;
    }
}
