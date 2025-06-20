package org.dTExpChange;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import java.util.List;
import java.util.Random;

public final class DTExpChange extends JavaPlugin {

    private FileConfiguration config;
    private final Random random = new Random();
    private Economy economy = null;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        reloadConfig();
        config = getConfig();

        // 检查配置
        if (!config.contains("exchange_tiers") || config.getConfigurationSection("exchange_tiers") == null) {
            getLogger().warning("配置文件中没有找到有效的exchange_tiers部分!");
            getLogger().warning("使用默认配置创建新的配置文件...");
            saveResource("config.yml", true); // 强制覆盖
            reloadConfig();
        }

        // 搜索Vault前置
        if (!setupEconomy()) {
            getLogger().severe("未找到Vault经济系统! 插件将禁用。");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        getLogger().info("金币兑换经验插件已启用!");
        getLogger().info("作者QQ：2802274853");
    }

    // 设置Vault经济系统
    private boolean setupEconomy() {
        if (Bukkit.getPluginManager().getPlugin("Vault") == null) {
            return false;
        }

        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }

        economy = rsp.getProvider();
        return economy != null;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§c只有玩家才能使用此命令!");
            return true;
        }

        Player player = (Player) sender;

        if (cmd.getName().equalsIgnoreCase("exchangeexp")) {
            if (args.length == 0) {
                // 显示可用的兑换档位
                showExchangeOptions(player);
                return true;
            }

            // 尝试兑换指定档位
            tryExchange(player, args[0]);
            return true;
        }

        return false;
    }

    private void showExchangeOptions(Player player) {
        List<String> tiers = config.getStringList("tiers");

        player.sendMessage("§a可用的金币兑换经验档位:");
        for (String tier : tiers) {
            ConfigurationSection section = config.getConfigurationSection("exchange_tiers." + tier);
            if (section != null) {
                int coins = section.getInt("coins");
                int minExp = section.getInt("min_exp");
                int maxExp = section.getInt("max_exp");
                player.sendMessage(String.format("§e%s: §b%d金币 §f兑换 §a%d-%d经验",
                        tier, coins, minExp, maxExp));
            }
        }
        player.sendMessage("§a使用/exchangeexp <档位名称> 进行兑换");
    }

    private void tryExchange(Player player, String tierName) {
        ConfigurationSection tier = config.getConfigurationSection("exchange_tiers." + tierName);

        if (tier == null) {
            player.sendMessage("§c无效的兑换档位!");
            return;
        }

        int coinsRequired = tier.getInt("coins");
        int minExp = tier.getInt("min_exp");
        int maxExp = tier.getInt("max_exp");

        // 检查玩家是否有足够的金币
        if (!economy.has(player, coinsRequired)) {
            player.sendMessage(String.format("§c金币不足! 需要%d金币，你只有%.2f金币",
                    coinsRequired, economy.getBalance(player)));
            return;
        }

        // 扣除金币
        economy.withdrawPlayer(player, coinsRequired);

        // 计算随机经验值
        int expGained = minExp + random.nextInt(maxExp - minExp + 1);

        // 给予经验
        player.giveExp(expGained);

        player.sendMessage(String.format("§a成功兑换! 花费%d金币获得%d经验",
                coinsRequired, expGained));
        player.sendMessage(String.format("§b剩余金币: %.2f", economy.getBalance(player)));
    }

    @Override
    public void onDisable() {
        getLogger().info("金币兑换经验插件已禁用!感谢使用！");
    }
}
