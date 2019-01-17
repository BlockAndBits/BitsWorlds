package bab.bitsworlds.gui;

import bab.bitsworlds.BitsWorlds;
import bab.bitsworlds.cmd.ConfigCmd;
import bab.bitsworlds.extensions.BWPlayer;
import bab.bitsworlds.multilanguage.LangCore;
import bab.bitsworlds.multilanguage.PrefixMessage;
import bab.bitsworlds.utils.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class MainGUI implements ImplGUI {
    @Override
    public BWGUI getGUI(String code, BWPlayer player) {
        switch (code) {
            case "main":
                return new BWGUI(
                        "main",
                        4*9,
                        ChatColor.DARK_AQUA  + "BitsWorlds",
                        this) {
                    @Override
                    public void setupItem(int item) {
                        switch (item) {
                            case 4:
                                //HEAD ITEM
                                ItemStack headItem = new ItemStack(Material.GRASS);
                                ItemMeta headItemMeta = headItem.getItemMeta();

                                headItemMeta.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + "BitsWorlds");

                                List<String> headItemLore = new ArrayList<>();

                                headItemLore.add(ChatColor.WHITE + LangCore.getClassMessage(MainGUI.class, "version-word").setKey("%%v", BitsWorlds.plugin.getDescription().getVersion()).getTranslatedMessage().message);
                                headItemLore.add("");
                                headItemLore.add(ChatColor.WHITE + "" + ChatColor.ITALIC + LangCore.getClassMessage(MainGUI.class, "by-word").setKey("%%n", ChatColor.BLUE + "MrPiva").getTranslatedMessage().message);

                                GUICore.addGuideLore(LangCore.getClassMessage(MainGUI.class, "head-item-guide-mode"), player, headItemLore);

                                headItemMeta.setLore(headItemLore);

                                headItem.setItemMeta(headItemMeta);

                                this.setItem(4, headItem);
                                break;
                            case 8:
                                //GUIDE ITEM
                                ItemStack guideItem = new ItemStack(Material.THIN_GLASS);

                                ItemMeta guideItemMeta = guideItem.getItemMeta();

                                guideItemMeta.setDisplayName(ChatColor.GOLD + LangCore.getClassMessage(MainGUI.class, "guide-mode-title").getTranslatedMessage().message);

                                List<String> guideItemLore = new ArrayList<>();

                                guideItemLore.add(ChatColor.WHITE + LangCore.getClassMessage(MainGUI.class, "actually-word").setKey("%%s",
                                        GUICore.guideMode(player) ? LangCore.getUtilMessage("enabled-word") : LangCore.getUtilMessage("disabled-word")).getTranslatedMessage().message);

                                guideItemLore.addAll(StringUtils.getDescriptionFromMessage(LangCore.getClassMessage(MainGUI.class, "guide-mode-lore").getTranslatedMessage().message, ChatColor.WHITE.toString(), ""));

                                guideItemMeta.setLore(guideItemLore);

                                guideItem.setItemMeta(guideItemMeta);

                                this.setItem(8, guideItem);
                            case 19:
                                //CONFIG ITEM
                                ItemStack configItem = new ItemStack(Material.BOOK_AND_QUILL);

                                ItemMeta configItemMeta = configItem.getItemMeta();

                                configItemMeta.setDisplayName(ChatColor.GOLD + LangCore.getClassMessage(MainGUI.class, "config-item-title").getTranslatedMessage().message);

                                List<String> configItemLore = new ArrayList<>();

                                GUICore.addGuideLore(LangCore.getClassMessage(MainGUI.class, "config-item-guide-mode").setKey("%%file", ChatColor.ITALIC + "config.yml"), player, configItemLore);

                                configItemMeta.setLore(configItemLore);

                                configItem.setItemMeta(configItemMeta);

                                this.setItem(19, configItem);

                                break;
                        }
                    }

                    @Override
                    public BWGUI init() {
                        genItems(4, 8, 19);

                        return this;
                    }
                }.init();
        }

        throw new NullPointerException("No GUI with id " + code + " found");
    }

    @Override
    public void clickEvent(InventoryClickEvent event, BWPlayer player, BWGUI gui) {
        switch (event.getSlot()) {
            case 8:
                GUICore.alternateGuideMode(player);

                gui.genItems(4, 8);
                break;
            case 19:
                if (!player.getBukkitPlayer().hasPermission("bitsworlds.maincmd.configcmd")) {
                    player.sendMessage(PrefixMessage.permission_message);

                    player.getBukkitPlayer().closeInventory();

                    return;
                }
                BWGUI configCmdGUI = new ConfigCmd().getGUI("config_main",  player);

                player.openGUI(configCmdGUI);

                configCmdGUI.genItems(27);

                break;
        }
    }
}
