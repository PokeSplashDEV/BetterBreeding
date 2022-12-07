package com.bencrow11.betterbreeding;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.daycare.event.DayCareEvent;
import com.pixelmonmod.pixelmon.api.events.PokemonReceivedEvent;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Random;
import java.util.UUID;

@Mod("betterbreeding")
public class BetterBreeding {
	private static final Logger LOGGER = LogManager.getLogger();

	public BetterBreeding() {
		MinecraftForge.EVENT_BUS.register(this);
		Pixelmon.EVENT_BUS.register(this);
	}

	@SubscribeEvent
	public void onServerStarting(FMLServerStartingEvent event) {
		LOGGER.info("Loaded.");
	}

	@SubscribeEvent
	public void checkEgg(PokemonReceivedEvent event) {
		// If the pokemon is an egg, cancel the receive.
		if (event.getPokemon().isEgg()) event.setCanceled(true);
	}

	@SubscribeEvent
	public void checkOT(DayCareEvent.PrePokemonAdd event) {
		UUID playerUUID = event.getPlayer().getUniqueID();
		UUID pokemonOTUUID = event.getChildCreated().getOriginalTrainerUUID();

		// If the player UUID and the pokemon OT UUID don't match, cancel the event
		if (!(playerUUID.equals(pokemonOTUUID))) {
			event.setCanceled(true);
		}
	}

	@SubscribeEvent
	public void setShinyRate(DayCareEvent.PreEggCalculate event) {
		Pokemon child = event.getCalculatedChild();
		Random random = new Random();

		// If the player has the shiny charm, set the odds to 1/512
		// TODO fill if statement
//		if () {
//			child.setShiny(random.nextInt(512) < 1);
//		// Else set shiny odds to 1/683
//		} else {
//			child.setShiny(random.nextInt(683) < 1);
//		}

		// Set the child with the new calculations
		event.setCalculatedChild(child);
	}
}
