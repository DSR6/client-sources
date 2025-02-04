/*
 * Decompiled with CFR 0_118.
 */
package net.minecraft.command.server;

import java.util.List;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.command.server.CommandTestForBlock;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;

public class CommandTestFor
extends CommandBase {
    private static final String __OBFID = "CL_00001182";

    @Override
    public String getCommandName() {
        return "testfor";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "commands.testfor.usage";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if (args.length < 1) {
            throw new WrongUsageException("commands.testfor.usage", new Object[0]);
        }
        Entity var3 = CommandTestFor.func_175768_b(sender, args[0]);
        NBTTagCompound var4 = null;
        if (args.length >= 2) {
            try {
                var4 = JsonToNBT.func_180713_a(CommandTestFor.func_180529_a(args, 1));
            }
            catch (NBTException var6) {
                throw new CommandException("commands.testfor.tagError", var6.getMessage());
            }
        }
        if (var4 != null) {
            NBTTagCompound var5 = new NBTTagCompound();
            var3.writeToNBT(var5);
            if (!CommandTestForBlock.func_175775_a(var4, var5, true)) {
                throw new CommandException("commands.testfor.failure", var3.getName());
            }
        }
        CommandTestFor.notifyOperators(sender, (ICommand)this, "commands.testfor.success", var3.getName());
    }

    @Override
    public boolean isUsernameIndex(String[] args, int index) {
        if (index == 0) {
            return true;
        }
        return false;
    }

    @Override
    public List addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        return args.length == 1 ? CommandTestFor.getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getAllUsernames()) : null;
    }
}

