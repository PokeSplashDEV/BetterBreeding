package com.bencrow11.betterbreeding;

import com.google.common.collect.Lists;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.storage.PartyStorage;
import com.pixelmonmod.pixelmon.api.storage.StorageProxy;
import com.pixelmonmod.pixelmon.comm.CommandChatHandler;
import com.pixelmonmod.pixelmon.command.PixelCommand;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import net.minecraftforge.server.permission.PermissionAPI;
import org.w3c.dom.Text;

import java.util.List;
import java.util.Objects;

public class CommandDebug extends PixelCommand {


	public CommandDebug(CommandDispatcher<CommandSource> dispatcher) {
		super(dispatcher);
	}

	public String getName() {
		return "pbdebug";
	}

	@Override
	public List<String> getAliases() {
		return Lists.newArrayList("pbdebug");
	}

	@Override
	public void execute(CommandSource sender, String[] args) throws CommandException, CommandSyntaxException {
		// Checks for permission to use the command
		if (!PermissionAPI.hasPermission(sender.asPlayer(), "pokebuilder.admin")) {
			CommandChatHandler.sendChat(sender, TextFormatting.RED + "You do not have permission for this command!");
		} else {
			// checks arg amount is correct
			if (args.length != 1) {
				CommandChatHandler.sendChat(sender, TextFormatting.RED + "Usage: /pbdebug <slot>");
			} else {
				try {
					int slot = Integer.parseInt(args[0]);
					PartyStorage storage = StorageProxy.getParty(sender.asPlayer().getUniqueID());
					Pokemon pokemon = storage.get(slot - 1);

					if (pokemon == null) {
						CommandChatHandler.sendChat(sender, TextFormatting.RED + "No Pokemon in slot " + slot);
						return;
					}

					// Prints pokemon information to chat
					CommandChatHandler.sendChat(sender, TextFormatting.AQUA + "Pokebuilt: " + pokemon.hasFlag(
							"Pokebuilder"));
					CommandChatHandler.sendChat(sender, TextFormatting.AQUA + "Can breed: " + !pokemon.hasFlag(
							"unbreedable"));
					CommandChatHandler.sendChat(sender,
							TextFormatting.AQUA + "Current Owner: " + pokemon.getPersistentData().getString(
									"currentOwner"));

				} catch (Exception error) {
					CommandChatHandler.sendChat(sender, TextFormatting.RED + "Usage: /pbdebug <slot>");
				}
			}
		}
	}
}
