package me.teri;

import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Set;

public class KB extends JavaPlugin {

    private static KB instance;

    private String craftBukkitVersion;
    private double horMultiplier = 1D;
    private double verMultiplier = 1D;

    @Override
    public void onEnable() {
        instance = this;

        getConfig().options().copyDefaults(true);
        getConfig().addDefault("kb.horizontal", 1D);
        getConfig().addDefault("kb.vertical", 1D);
        getConfig().addDefault("kb.preset", "");
        saveConfig();

        // CraftBukkit version is used to access NMS classes using reflection
        this.craftBukkitVersion = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
        this.horMultiplier = getConfig().getDouble("kb.horizontal");
        this.verMultiplier = getConfig().getDouble("kb.vertical");

        Bukkit.getPluginManager().registerEvents(new DamageListener(), this);
    }
    public void onDisable() {
        saveConfig();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("kb.setknockback")) {
            sender.sendMessage(ChatColor.RED + "No permission.");
            return true;
        }

        if (args.length == 0){
            sender.sendMessage(ChatColor.GRAY + "" + ChatColor.STRIKETHROUGH + "--------------------------------");
            sender.sendMessage(ChatColor.RED + "/kb set <hor> <ver>");
            sender.sendMessage(ChatColor.RED + "/kb preset create <name> <hor> <ver>");
            sender.sendMessage(ChatColor.RED + "/kb preset remove <name>");
            sender.sendMessage(ChatColor.RED + "/kb preset info <name> ");
            sender.sendMessage(ChatColor.RED + "/kb preset set <name> <hor> <ver>");
            sender.sendMessage(ChatColor.RED + "/kb preset load <name>");
            sender.sendMessage(ChatColor.RED + "/kb preset list");
            sender.sendMessage(ChatColor.GRAY + "" + ChatColor.STRIKETHROUGH + "--------------------------------");
            return true;
        }
        if(args.length == 2) {
            if (args[0].equalsIgnoreCase("preset")) {
                if (args[1].equalsIgnoreCase("list")) {
                    Set<String> a = getConfig().getConfigurationSection("kb.preset").getKeys(false);

                    sender.sendMessage(ChatColor.GRAY + "" + ChatColor.STRIKETHROUGH + "--------------------------------");
                    sender.sendMessage(ChatColor.GOLD + " KnockBack Preset List");
                    if (a.size() != 0) {
                        for (String str : a) {
                            sender.sendMessage(ChatColor.GREEN + "   " + str);
                        }
                    }
                    sender.sendMessage(ChatColor.GRAY + "" + ChatColor.STRIKETHROUGH + "--------------------------------");
                    } else {
                    sender.sendMessage(ChatColor.RED + "Not exit preset");
                        return true;
                }
            }
        }
        if(args.length == 3) {
            if (args[0].equalsIgnoreCase("set")) {
                double horMultiplier = NumberUtils.toDouble(args[1], -1D);
                double verMultiplier = NumberUtils.toDouble(args[2], -1D);

                if (horMultiplier < 0D || verMultiplier < 0D) {
                    sender.sendMessage(ChatColor.RED + "Invalid horizontal/vertical multiplier!");
                    return true;
                }
                getConfig().set("kb.horizontal", horMultiplier);
                getConfig().set("kb.vertical", verMultiplier);
                saveConfig();

                this.horMultiplier = horMultiplier;
                this.horMultiplier = verMultiplier;

                sender.sendMessage(ChatColor.GRAY + "" + ChatColor.STRIKETHROUGH + "--------------------------------");
                sender.sendMessage(ChatColor.GREEN + " Succsesfully updated knockback!");
                sender.sendMessage(ChatColor.GRAY + "" + ChatColor.STRIKETHROUGH + "--------------------------------");
            }
            if(args[0].equalsIgnoreCase("preset")) {
                if(args[1].equalsIgnoreCase("load")) {
                    Set<String> a = getConfig().getConfigurationSection("kb.preset").getKeys(false);
                    if(a.contains(args[2])) {
                        getConfig().set("kb.horizontal", getConfig().getDouble("kb.preset." + args[2] + ".horizontal"));
                        getConfig().set("kb.vertical", getConfig().getDouble("kb.preset." + args[2] + ".vertical"));
                        this.horMultiplier = getConfig().getDouble("kb.preset." + args[2] + ".horizontal");
                        this.verMultiplier = getConfig().getDouble("kb.preset." + args[2] + ".vertical");
                        sender.sendMessage(ChatColor.GRAY + "" + ChatColor.STRIKETHROUGH + "--------------------------------");
                        sender.sendMessage(ChatColor.GREEN + " Succsesfully loaded the preset!");
                        sender.sendMessage(ChatColor.BLUE + " Loaded Preset:");
                        sender.sendMessage(ChatColor.GOLD + "  - Preset Name: " + ChatColor.AQUA + ChatColor.BOLD + args[2]);
                        sender.sendMessage(ChatColor.GOLD + "  - Preset Horizontal: " + ChatColor.GRAY + getConfig().getDouble("kb.preset." + args[2] + ".horizontal"));
                        sender.sendMessage(ChatColor.GOLD + "  - Preset Vertical: " + ChatColor.GRAY + getConfig().getDouble("kb.preset." + args[2] + ".vertical"));
                        sender.sendMessage(ChatColor.GRAY + "" + ChatColor.STRIKETHROUGH + "--------------------------------");
                    }
                    else {
                        sender.sendMessage(ChatColor.RED + "Not exit preset");
                        return true;
                    }
                }
                if (args[1].equalsIgnoreCase("remove")) {
                    Set<String> a = getConfig().getConfigurationSection("kb.preset").getKeys(false);

                    if(a.contains(args[2])) {
                        getConfig().set("kb.preset." + args[2], null);
                        saveConfig();
                        sender.sendMessage(ChatColor.GRAY + "" + ChatColor.STRIKETHROUGH + "--------------------------------");
                        sender.sendMessage(ChatColor.GREEN + " Succsesfully remove the preset!");
                        sender.sendMessage(ChatColor.DARK_RED + " Remove Preset:");
                        sender.sendMessage(ChatColor.GOLD + "  - Preset Name: " + ChatColor.AQUA + ChatColor.BOLD + args[2]);
                        sender.sendMessage(ChatColor.GRAY + "" + ChatColor.STRIKETHROUGH + "--------------------------------");
                    }
                    else {
                        sender.sendMessage(ChatColor.RED + "Not exit preset");
                        return true;
                    }
                }
                if (args[1].equalsIgnoreCase("info")) {
                    Set<String> a = getConfig().getConfigurationSection("kb.preset").getKeys(false);

                    if(a.contains(args[2])) {
                        sender.sendMessage(ChatColor.GRAY + "" + ChatColor.STRIKETHROUGH + "--------------------------------");
                        sender.sendMessage(ChatColor.GOLD + "  - Preset Name: " + ChatColor.AQUA + ChatColor.BOLD + args[2]);
                        sender.sendMessage(ChatColor.GOLD + "  - Preset Horizontal: " + ChatColor.GRAY + getConfig().getDouble("kb.preset." + args[2] + ".horizontal"));
                        sender.sendMessage(ChatColor.GOLD + "  - Preset Vertical: " + ChatColor.GRAY + getConfig().getDouble("kb.preset." + args[2] + ".vertical"));
                        sender.sendMessage(ChatColor.GRAY + "" + ChatColor.STRIKETHROUGH + "--------------------------------");
                    }
                    else {
                        sender.sendMessage(ChatColor.RED + "Not exit preset");
                        return true;
                    }
                }
                if (args[1].equalsIgnoreCase("list")) {
                    Set<String> a = getConfig().getConfigurationSection("kb.preset").getKeys(false);

                    if(a.contains(args[2])) {
                        sender.sendMessage(ChatColor.GRAY + "" + ChatColor.STRIKETHROUGH + "--------------------------------");
                        sender.sendMessage(ChatColor.GOLD + " KnockBack Preset List");
                        if(a.size() != 0) {
                            for(String str : a) {
                                sender.sendMessage(ChatColor.GREEN +"   " + str);
                            }
                        }
                        sender.sendMessage(ChatColor.GRAY + "" + ChatColor.STRIKETHROUGH + "--------------------------------");
                    }
                    else {
                        sender.sendMessage(ChatColor.RED + "Not exit preset");
                        return true;
                    }
                }
            }
        }
        if(args.length == 5) {
            if(args[0].equalsIgnoreCase("preset")) {
                if(args[1].equalsIgnoreCase("set")) {
                    double horMultiplier = NumberUtils.toDouble(args[3], -1D);
                    double verMultiplier = NumberUtils.toDouble(args[4], -1D);

                    if (horMultiplier < 0D || verMultiplier < 0D) {
                        sender.sendMessage(ChatColor.RED + "Invalid horizontal/vertical multiplier!");
                        return true;
                    }

                    Set<String> a = getConfig().getConfigurationSection("kb.preset").getKeys(false);
                    if(a.contains(args[2])) {
                        getConfig().set("kb.preset." + args[2] + ".horizontal", horMultiplier);
                        getConfig().set("kb.preset." + args[2] + ".vertical", verMultiplier);
                        saveConfig();
                        sender.sendMessage(ChatColor.GRAY + "" + ChatColor.STRIKETHROUGH + "--------------------------------");
                        sender.sendMessage(ChatColor.GREEN + " Succsesfully set the knockback preset!");
                        sender.sendMessage(ChatColor.GOLD + "  - Horizontal: " + ChatColor.GRAY + getConfig().getDouble("kb.preset." + args[2] + ".horizontal"));
                        sender.sendMessage(ChatColor.GOLD + "  - Vertical: " + ChatColor.GRAY + getConfig().getDouble("kb.preset." + args[2] + ".vertical"));
                        sender.sendMessage(ChatColor.GRAY + "" + ChatColor.STRIKETHROUGH + "--------------------------------");
                    }
                    else {
                        sender.sendMessage(ChatColor.RED + "Not exit preset");
                        return true;
                    }
                }
                if (args[1].equalsIgnoreCase("create")) {
                    double horMultiplier = NumberUtils.toDouble(args[3], -1D);
                    double verMultiplier = NumberUtils.toDouble(args[4], -1D);

                    if (horMultiplier < 0D || verMultiplier < 0D) {
                        sender.sendMessage(ChatColor.RED + "Invalid horizontal/vertical multiplier!");
                        return true;
                    }

                    Set<String> a = getConfig().getConfigurationSection("kb.preset").getKeys(false);
                    if(!a.contains(args[2])) {
                        getConfig().set("kb.preset." + args[2] + ".horizontal", horMultiplier);
                        getConfig().set("kb.preset." + args[2] + ".vertical", verMultiplier);
                        saveConfig();
                        sender.sendMessage(ChatColor.GRAY + "" + ChatColor.STRIKETHROUGH + "--------------------------------");
                        sender.sendMessage(ChatColor.GREEN + " Succsesfully created the preset!");
                        sender.sendMessage(ChatColor.BLUE + " Created Preset:");
                        sender.sendMessage(ChatColor.GOLD + "  - Preset Name: " + ChatColor.AQUA + ChatColor.BOLD + args[2]);
                        sender.sendMessage(ChatColor.GOLD + "  - Preset Horizontal: " + ChatColor.GRAY + getConfig().getDouble("kb.preset." + args[2] + ".horizontal"));
                        sender.sendMessage(ChatColor.GOLD + "  - Preset Vertical: " + ChatColor.GRAY + getConfig().getDouble("kb.preset." + args[2] + ".vertical"));
                        sender.sendMessage(ChatColor.GRAY + "" + ChatColor.STRIKETHROUGH + "--------------------------------");
                    }
                    else {
                        sender.sendMessage(ChatColor.RED + "Already created!");
                        return true;
                    }
                }
            }
        }
        return true;
    }

    public static KB getInstance() {
        return instance;
    }

    public String getCraftBukkitVersion() {
        return craftBukkitVersion;
    }

    public double getHorMultiplier() {
        return horMultiplier;
    }

    public void setHorMultiplier(double horMultiplier) {
        this.horMultiplier = horMultiplier;
    }

    public double getVerMultiplier() {
        return verMultiplier;
    }

    public void setVerMultiplier(double verMultiplier) {
        this.verMultiplier = verMultiplier;
    }

}