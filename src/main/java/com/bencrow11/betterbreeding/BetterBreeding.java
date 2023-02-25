package com.bencrow11.betterbreeding;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.events.PokedexEvent;
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
		new CommandDebug(event.getDispatcher());
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
	}

	// Prevents non OT pokemon being registered in the PokeDex
	@SubscribeEvent
	public void onDexFill(PokedexEvent.Pre event) {
			UUID player = event.getPlayer().getUniqueID();
			UUID pokemon = event.getPokemon().getOriginalTrainerUUID();

			if (!player.equals(pokemon)) {
				event.setCanceled(true);
			}
	}

}

