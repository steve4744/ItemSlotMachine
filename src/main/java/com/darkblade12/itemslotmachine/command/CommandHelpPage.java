package com.darkblade12.itemslotmachine.command;

import com.darkblade12.itemslotmachine.ItemSlotMachine;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public final class CommandHelpPage {

    private final ItemSlotMachine plugin;
    private final CommandHandler handler;
    private final int commandsPerPage;

    CommandHelpPage(ItemSlotMachine plugin, CommandHandler handler, int commandsPerPage) {
        this.plugin = plugin;
        this.handler = handler;
        this.commandsPerPage = commandsPerPage;
    }

    private String insertIntoFormat(String label, ICommand i) {
        CommandDetails c = CommandList.getDetails(i);
        return plugin.messageManager.help_page_command_format(CommandHandler.getUsage(label, i), plugin.messageManager.getMessage("description_" + handler.getDefaultLabel() + "_" + c.name()), c.executableAsConsole(), c.permission());
    }

    public void showPage(CommandSender sender, String label, int page) {
        List<ICommand> visible = getVisibleCommands(sender);
        String header = plugin.messageManager.help_page_header(label);
        StringBuilder b = new StringBuilder(header);
        for (int i = (page - 1) * commandsPerPage; i <= page * commandsPerPage - 1; i++) {
            if (i > visible.size() - 1) {
                break;
            } else {
                b.append("\n§r").append(insertIntoFormat(label, visible.get(i)));
            }
        }
        b.append("\n§r").append(plugin.messageManager.help_page_footer(page, getPages(sender)));
        sender.sendMessage(b.toString());
    }

    public boolean hasPage(CommandSender sender, int page) {
        return page > 0 && page <= getPages(sender);
    }

    private int getPages(CommandSender sender) {
        double p = (double) getVisibleCommands(sender).size() / (double) commandsPerPage;
        int pr = (int) p;
        return p > (double) pr ? pr + 1 : pr;
    }

    private List<ICommand> getVisibleCommands(CommandSender sender) {
        List<ICommand> visible = new ArrayList<>();
        for (ICommand i : handler.getCommands()) {
            String permission = CommandList.getDetails(i).permission();
            if (permission.equals("None") || sender.hasPermission(permission)) {
                visible.add(i);
            } else {
                master:
                for (String p : handler.getMasterPermissions()) {
                    if (sender.hasPermission(p)) {
                        visible.add(i);
                        break;
                    }
                }
            }
        }
        return visible;
    }
}
