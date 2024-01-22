package com.yuanno.oniclawaddon.packets;

import com.google.common.base.Strings;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;
import xyz.pixelatedw.mineminenomi.api.events.SetPlayerDetailsEvent;
import xyz.pixelatedw.mineminenomi.api.helpers.AbilityHelper;
import xyz.pixelatedw.mineminenomi.config.CommonConfig;
import xyz.pixelatedw.mineminenomi.data.entity.ability.AbilityDataCapability;
import xyz.pixelatedw.mineminenomi.data.entity.ability.IAbilityData;
import xyz.pixelatedw.mineminenomi.data.entity.entitystats.EntityStatsCapability;
import xyz.pixelatedw.mineminenomi.data.entity.entitystats.IEntityStats;
import xyz.pixelatedw.mineminenomi.init.ModItems;
import xyz.pixelatedw.mineminenomi.init.ModValues;
import xyz.pixelatedw.mineminenomi.items.CharacterCreatorItem;
import xyz.pixelatedw.mineminenomi.packets.server.SSyncAbilityDataPacket;
import xyz.pixelatedw.mineminenomi.packets.server.SSyncEntityStatsPacket;
import xyz.pixelatedw.mineminenomi.wypi.WyNetwork;

import java.util.Random;
import java.util.function.Supplier;

public class CFinishCCPacketOverwriteMixin
{
	private int factionId, raceId, styleId;

	private static final String[] FACTIONS = new String[] { "Random", ModValues.PIRATE, ModValues.MARINE, ModValues.BOUNTY_HUNTER, ModValues.REVOLUTIONARY };
	private static final String[] RACES = new String[] { "Random", ModValues.HUMAN, ModValues.FISHMAN, ModValues.CYBORG, ModValues.MINK };
	private static final String[] STYLES = new String[] { "Random", ModValues.SWORDSMAN, ModValues.SNIPER, ModValues.DOCTOR, ModValues.ART_OF_WEATHER, ModValues.BRAWLER, ModValues.BLACK_LEG };
	private static final String[] MINK_SUB_RACES = new String[] { ModValues.MINK_BUNNY, ModValues.MINK_DOG, ModValues.MINK_LION };

	public CFinishCCPacketOverwriteMixin() {}

	public CFinishCCPacketOverwriteMixin(int factionId, int raceId, int styleId)
	{
		this.factionId = factionId;
		this.raceId = raceId;
		this.styleId = styleId;
	}

	public void encode(PacketBuffer buffer)
	{
		buffer.writeInt(this.factionId);
		buffer.writeInt(this.raceId);
		buffer.writeInt(this.styleId);
	}

	public static CFinishCCPacketOverwriteMixin decode(PacketBuffer buffer)
	{
		CFinishCCPacketOverwriteMixin msg = new CFinishCCPacketOverwriteMixin();
		msg.factionId = buffer.readInt();
		msg.raceId = buffer.readInt();
		msg.styleId = buffer.readInt();
		return msg;
	}

	public static void handle(CFinishCCPacketOverwriteMixin message, final Supplier<NetworkEvent.Context> ctx)
	{
		if (ctx.get().getDirection() == NetworkDirection.PLAY_TO_SERVER)
		{
			ctx.get().enqueueWork(() ->
			{
				PlayerEntity player = ctx.get().getSender();
				IEntityStats entityProps = EntityStatsCapability.get(player);
				IAbilityData abilityProps = AbilityDataCapability.get(player);
				Random rand = new Random();

				boolean hasCharBook = !player.getMainHandItem().isEmpty() && player.getMainHandItem().getItem().equals(ModItems.CHARACTER_CREATOR.get());
				boolean hasEmptyStats = Strings.isNullOrEmpty(entityProps.getFaction()) || Strings.isNullOrEmpty(entityProps.getRace()) || Strings.isNullOrEmpty(entityProps.getFightingStyle());
				
				if(!hasCharBook && !hasEmptyStats)
					return;
				
				message.factionId = message.factionId % FACTIONS.length;
				message.raceId = message.raceId % RACES.length;
				message.styleId = message.styleId % STYLES.length;
				
				String faction;
				if (message.factionId == 0)
					faction = FACTIONS[1 + rand.nextInt(FACTIONS.length - 1)];
				else
					faction = FACTIONS[message.factionId];
				entityProps.setFaction(faction);

				String race;

				if (message.raceId == 0 || CommonConfig.INSTANCE.getRaceRandomizer()) {

					race = randomChoice();
				}
				else
					race = RACES[message.raceId];
				entityProps.setRace(race);

				if (entityProps.isMink())
				{
					String subRace = MINK_SUB_RACES[rand.nextInt(MINK_SUB_RACES.length)];
					entityProps.setSubRace(subRace);
				}

				String style;
				if (message.styleId == 0)
					style = STYLES[1 + rand.nextInt(STYLES.length - 1)];
				else
					style = STYLES[message.styleId];
				entityProps.setFightingStyle(style);

				  AbilityHelper.validateRacialAndHakiAbilities(player);

				if (entityProps.isCyborg())
				{
					if(entityProps.getMaxCola() < 100)
						entityProps.setMaxCola(100);
					entityProps.setCola(entityProps.getMaxCola());
				}

				for (ItemStack is : player.inventory.items)
					if (is != null && is.getItem() instanceof CharacterCreatorItem)
						player.inventory.removeItem(is);
				
				SetPlayerDetailsEvent event = new SetPlayerDetailsEvent(player);
				MinecraftForge.EVENT_BUS.post(event);
				
				WyNetwork.sendToAllTrackingAndSelf(new SSyncEntityStatsPacket(player.getId(), entityProps), player);
				WyNetwork.sendTo(new SSyncAbilityDataPacket(player.getId(), abilityProps), (ServerPlayerEntity) player);


			});
		}

		ctx.get().setPacketHandled(true);
	}


	public static String randomChoice() {
		// Strings to choose from
		String[] strings = {ModValues.HUMAN, ModValues.FISHMAN, ModValues.DOCTOR, ModValues.MINK, "Oni"};

		// Probabilities for each string
		double[] probabilities = {0.245, 0.245, 0.245, 0.245, 0.02};  // Adjusted probabilities

		// Validate that the probabilities add up to 1
		double sum = 0;
		for (double prob : probabilities) {
			sum += prob;
		}

		if (sum != 1.0) {
			throw new IllegalArgumentException("Probabilities must add up to 1");
		}

		// Choose a random number between 0 and 1
		double randomNumber = Math.random();

		// Use cumulative probabilities to determine the chosen string
		double cumulativeProbability = 0;
		for (int i = 0; i < probabilities.length; i++) {
			cumulativeProbability += probabilities[i];
			if (randomNumber <= cumulativeProbability) {
				return strings[i];
			}
		}

		// Default to the last string if for some reason the loop didn't return a value
		return strings[strings.length - 1];
	}
}
