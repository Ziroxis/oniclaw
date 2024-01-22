package com.yuanno.oniclawaddon.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.ISuggestionProvider;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.TranslationTextComponent;
import xyz.pixelatedw.mineminenomi.api.abilities.AbilityCategory;
import xyz.pixelatedw.mineminenomi.api.abilities.IAbility;
import xyz.pixelatedw.mineminenomi.data.entity.ability.AbilityDataCapability;
import xyz.pixelatedw.mineminenomi.data.entity.ability.IAbilityData;
import xyz.pixelatedw.mineminenomi.data.entity.entitystats.EntityStatsCapability;
import xyz.pixelatedw.mineminenomi.data.entity.entitystats.IEntityStats;
import xyz.pixelatedw.mineminenomi.events.abilities.AbilityProgressionEvents;
import xyz.pixelatedw.mineminenomi.init.ModValues;
import xyz.pixelatedw.mineminenomi.packets.server.SSyncAbilityDataPacket;
import xyz.pixelatedw.mineminenomi.packets.server.SSyncEntityStatsPacket;
import xyz.pixelatedw.mineminenomi.wypi.WyNetwork;

import java.util.ArrayList;
import java.util.List;

public class RaceChangeCommand {

    public static void register(CommandDispatcher<CommandSource> dispatcher)
    {
        dispatcher.register(Commands.literal("race").requires((commandSource) -> commandSource.hasPermission(2))
                .then(Commands.argument("target", EntityArgument.player())
                        .then(Commands.argument("race", StringArgumentType.string()).suggests(SUGGEST_SET)
                                .executes((command) ->
                                {
                                    String race = StringArgumentType.getString(command, "race");

                                    return setRace(command.getSource(), EntityArgument.getPlayer(command, "target"), race);
                                }))));
    }

    private static final SuggestionProvider<CommandSource> SUGGEST_SET = (source, builder) -> {
        List<String> suggestions = new ArrayList<>();

        suggestions.add(ModValues.HUMAN);
        suggestions.add(ModValues.FISHMAN);
        suggestions.add(ModValues.CYBORG);
        suggestions.add(ModValues.MINK);
        suggestions.add("oni");

        return ISuggestionProvider.suggest(suggestions.stream(), builder);
    };

    private static int setRace(CommandSource commandSource, PlayerEntity player, String race)
    {
        IEntityStats entityStats = EntityStatsCapability.get(player);
        entityStats.setRace(race);
        IAbilityData abilityData = AbilityDataCapability.get(player);
        abilityData.clearUnlockedAbilities(AbilityCategory.RACIAL.isPartofCategory());
        AbilityProgressionEvents.checkForRacialUnlocks(player);
        WyNetwork.sendTo(new SSyncEntityStatsPacket(player.getId(), entityStats), player);
        WyNetwork.sendTo(new SSyncAbilityDataPacket(player.getId(), abilityData), player);

        commandSource.sendSuccess(new TranslationTextComponent("set race of " + player.getDisplayName().getString() + " to " + race), true);
        return 1;
    }
}
