package com.bencrow11.betterbreeding;

import com.google.common.collect.Lists;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.pixelmonmod.pixelmon.api.storage.PartyStorage;
import com.pixelmonmod.pixelmon.api.storage.StorageProxy;
import com.pixelmonmod.pixelmon.comm.CommandChatHandler;
import com.pixelmonmod.pixelmon.command.PixelCommand;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import net.minecraftforge.server.permission.PermissionAPI;

import java.util.List;
import java.util.Objects;

public class CommandPokebuilder extends PixelCommand {


	public CommandPokebuilder(CommandDispatcher<CommandSource> dispatcher) {
		super(dispatcher);
	}

	public String getName() {
		return "pbtag";
	}

	@Override
	public List<String> getAliases() {
		return Lists.newArrayList("pbtag");
	}

	@Override
	public void execute(CommandSource sender, String[] args) throws CommandException, CommandSyntaxException {

		// checks for a player running the command
		if (sender.getEntity() != null) {
			// Checks the player has the permission
			if (!PermissionAPI.hasPermission(sender.asPlayer(), "pokebuilder.admin")) {
				CommandChatHandler.sendChat(sender, TextFormatting.RED + "You do not have permission for this command!");
				return;
			}

			// Checks the arg lenth is correct
			if (args.length != 2) {
				CommandChatHandler.sendChat(sender, TextFormatting.RED + "Usage: /pbtag <player> <slot>");
				return;
			}
		}

		try {
			// gets the player given
			ServerPlayerEntity player =
					ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayerByUsername(args[0]);

			// gets the slot from the args
			int slot = Integer.parseInt(args[1]);

			assert player != null;
			PartyStorage storage = StorageProxy.getParty(player.getUniqueID());

			// adds the tag pokebuilder tag to the pokemon in the specified slot
			Objects.requireNonNull(storage.get(slot - 1)).addFlag("Pokebuilder");
			Objects.requireNonNull(storage.get(slot - 1)).addFlag("unbreedable");
			Objects.requireNonNull(storage.get(slot - 1)).getPersistentData().remove("currentOwner");
		} catch (Exception error) {
			CommandChatHandler.sendChat(sender, TextFormatting.RED + "Usage: /pbtag <player> <slot>");
		}
	}
}
