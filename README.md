# DTExpChange - Minecraft 金币兑换经验插件

![Minecraft Version](https://img.shields.io/badge/Minecraft-1.12%2B-brightgreen)
![License](https://img.shields.io/badge/License-MIT-blue)
![Vault Dependency](https://img.shields.io/badge/Requires-Vault-yellow)

## 目录
- [🎯 功能概述](#-功能概述)
- [🚀 快速开始](#-快速开始)
  - [安装要求](#安装要求)
  - [安装步骤](#安装步骤)
- [⚙️ 配置详解](#️-配置详解)
  - [config.yml](#configyml)
  - [权限节点](#权限节点)
  - [命令列表](#命令列表)
- [👨💻 开发者API](#-开发者api)
- [🔧 构建插件](#-构建插件)
- [❓ 常见问题](#-常见问题)
- [📜 许可证](#-许可证)

## 🎯 功能概述

**DTExpChange** 是一个让玩家可以用游戏内金币兑换经验的 Spigot 插件，主要特性包括：

- ✔️ **多档位兑换系统** - 支持无限量自定义兑换档位  
- ✔️ **随机经验范围** - 每个档位可设置最小/最大经验值  
- ✔️ **Vault 经济集成** - 兼容所有主流经济插件  
- ✔️ **权限控制** - 不同档位可设置不同权限要求  
- ✔️ **视听特效** - 兑换成功时可触发粒子效果和音效  
- ✔️ **多语言支持** - 所有消息均可自定义

## 🚀 快速开始

### 安装要求

- Minecraft 服务器：Paper 1.21.4(其余版本尚未验证)
- 必须插件：[Vault](https://www.spigotmc.org/resources/vault.34315/)
- 经济插件：EssentialsX / CMI / 或其他 Vault 兼容经济系统

### 安装步骤

1. 将 `DTExpChange.jar` 放入服务器的 `plugins` 文件夹  
2. 重启服务器生成默认配置  
3. 编辑 `plugins/DTExpChange/config.yml` 调整设置  
4. 使用 `/dtexpchange reload` 重载配置  

## ⚙️ 配置详解

### config.yml

```yaml
# 兑换档位列表
tiers:
  - beginner
  - advanced
  - expert

# 档位详细设置
exchange_tiers:
  beginner:
    coins: 1000
    min_exp: 100
    max_exp: 500
    permission: ""

  advanced:
    coins: 5000
    min_exp: 500
    max_exp: 1500
    permission: ""

# 消息配置
messages:
  insufficient_funds: "&c❌ 需要 {coins}金币，你只有 {balance}金币"
  success: "&a✔ 兑换成功! 获得 {exp}经验"
  invalid_tier: "&c⚠ 无效的兑换档位"

# 特效设置
effects:
  sound:
    enabled: true
    type: "ENTITY_PLAYER_LEVELUP"
    volume: 1.0
    pitch: 1.5
  particles:
    enabled: true
    type: "HEART"
    count: 5
```

### 权限节点

| 权限节点              | 描述               | 默认      |
|-----------------------|--------------------|-----------|
| `dtexpchange.use`     | 基础兑换权限       | 所有玩家  |
| `dtexpchange.reload`  | 重载配置权限       | OP        |
| `dtexpchange.vip`     | VIP 档位权限       | 无        |

### 命令列表

| 命令                          | 描述             | 权限                 |
|-------------------------------|------------------|----------------------|
| `/exchangeexp`                | 查看可用档位     | `dtexpchange.use`    |
| `/exchangeexp <档位>`         | 兑换经验         | `dtexpchange.use`    |
| `/dtexpchange reload`         | 重载配置         | `dtexpchange.reload` |

## 👨💻 开发者 API

```java
// 获取插件实例
DTExpChange plugin = DTExpChange.getInstance();

// 检查能否兑换
if(plugin.canExchange(player, "vip")) {
    // 执行兑换
    ExchangeResult result = plugin.exchange(player, "vip");

    if(result.isSuccess()) {
        player.sendMessage("获得 " + result.getExp() + " 经验");
    }
}

// 注册自定义档位
plugin.registerTier("custom", 3000, 200, 800);
```

## 🔧 构建插件

1. 克隆仓库  
2. 确保已安装 JDK 21 
3. 执行以下命令构建：

```bash
gradle clean shadowJar
```

构建完成后，`build/libs/` 目录下会生成可部署的 `.jar` 文件。

## ❓ 常见问题

**Q:** 兑换时显示 "无效的档位" 错误  
**A:** 检查 `config.yml` 中是否正确定义了该档位

**Q:** 经济系统不工作  
**A:** 确保已安装 Vault 和兼容的经济插件

**Q:** 如何添加新档位  
**A:** 在 `config.yml` 的 `tiers` 列表和 `exchange_tiers` 部分添加新配置

## 📜 许可证

本项目采用 **MIT 许可证** 开源。

```
Copyright (c) 2023 DTExpChange

允许自由使用、修改和分发本软件及相关文档文件。
```
