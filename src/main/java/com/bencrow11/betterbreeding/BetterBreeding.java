package com.bencrow11.betterbreeding;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.events.PokemonReceivedEvent;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.comm.CommandChatHandler;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
	public void registerCommand(RegisterCommandsEvent event) {
		new CommandPokebuilder(event.getDispatcher());
	}


	@SubscribeEvent
	public void checkEgg(PokemonReceivedEvent event) {

		Pokemon pokemon = event.getPokemon();
		UUID targetPlayerUUID = event.getPlayer().getUniqueID();

		// If the pokemon is an egg, cancel the receive.
		if (pokemon.isEgg()) {
			event.setCanceled(true);
			return;
		};

		// If the pokemon is given with /pokegive
		if (pokemon.getOriginalTrainerUUID() == null) {
			return;
		}

		if (targetPlayerUUID.equals(pokemon.getOriginalTrainerUUID()) && !pokemon.hasFlag("Pokebuilder")) {
			pokemon.removeFlag("unbreedable");
			return;
		}

		if (targetPlayerUUID.toString().equals(pokemon.getPersistentData().getString("currentOwner"))) {
			pokemon.removeFlag("unbreedable");
			return;
		}

		pokemon.addFlag("unbreedable");

		// If the pokemon isn't given with /pokegive
//		if (pokemon.getOriginalTrainerUUID() != null) {
//			// if the player UUID is the same as the pokemon OT UUID and the pokemon hasn't been pokebuilt
//
//			// If OT and UUID match, and no pb flag, its breedable
//			if (targetPlayerUUID.equals(pokemon.getOriginalTrainerUUID()) && !pokemon.hasFlag("Pokebuilder")) {
//				pokemon.removeFlag("unbreedable");
//			// If the pokemon has been sold on gts as breedable
//			} else if (targetPlayerUUID.toString().equals(pokemon.getPersistentData().getString("currentOwner"))) {
//				pokemon.removeFlag("unbreedable");
//			} else  {
//				pokemon.addFlag("unbreedable");
//			}
//
//
//
//
//			if (pokemon.getOriginalTrainerUUID().equals(event.getPlayer().getUniqueID()) && !pokemon.hasFlag("Pokebuilder")) {
//				// Set the pokemon to breedable
//				pokemon.removeFlag("unbreedable");
//			// If the pokemon isn't already set as unbreedable
//			} else if (!pokemon.isUnbreedable()) {
//				// Set the pokemon to unbreedable
//				pokemon.addFlag("unbreedable");
////			}
//		}

	}

}

