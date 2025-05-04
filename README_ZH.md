# 五子棋对弈系统

[English](./README.md) | 中文

[![JDK](https://img.shields.io/badge/JDK-21-brightgreen.svg)](https://jdk.java.net/21/)
[![License](https://img.shields.io/badge/license-MIT-blue.svg)](LICENSE)
[![Build](https://img.shields.io/badge/build-passing-brightgreen.svg)]()
[![GUI](https://img.shields.io/badge/GUI-Swing-orange.svg)]()

基于 Java 开发的五子棋对弈系统，支持 AI 对战。

> AI 当前搜索深度限制为 6 层，正在持续优化中。

<img src="screenshot/gomoku.png" width="480" alt="游戏截图">

## 功能特性

### 核心功能

- AI 对战支持三个难度等级（初级-4层、进阶-6层、专业-8层）
- 游戏工具：悔棋、计时器、棋子颜色选择
- 清爽直观的界面

### 技术特性

1. **AI 决策系统**
    - 迭代加深的 Alpha-Beta 剪枝搜索
    - 基于棋型识别和威胁分析的局面评估
    - 使用 AC 自动机进行快速棋型匹配

2. **性能优化**
    - 启发式走法排序提升搜索效率
    - 使用 Zobrist 哈希进行局面缓存
    - 高效的棋盘表示（一维数组，15x15）
    - 对象池优化内存使用

3. **架构设计**
    - 基于 EventBus 的事件驱动设计，实现 UI-逻辑解耦

## 项目结构

```
src/main/java/cn/wxiach/
├── core/           # 游戏核心逻辑
│   ├── model/     # 基础数据模型
│   ├── rule/      # 游戏规则
│   ├── state/     # 游戏状态管理
│   └── utils/     # 工具类
├── robot/         # AI 实现
│   ├── search/    # 搜索算法
│   ├── pattern/   # 棋型识别
│   └── evaluate/  # 局面评估
├── ui/            # 用户界面
│   ├── components/# UI 组件
│   ├── assets/    # 资源文件
│   └── support/   # UI 支持
└── event/         # 事件系统
```

## 环境要求

- JDK 21 或更高版本
- Gradle 8.5 或更高版本

## 开发计划

- [ ] **走法排序优化**: 实现历史启发和杀手启发以提升剪枝效率
- [ ] **VCT 模块**: 添加连续威胁胜利算法用于关键局面
- [ ] **开局库**: 引入标准开局数据库

## 参与贡献

欢迎提交 Issue 和 Pull Request。

## 开源协议

本项目基于 MIT 协议开源 - 详见 [LICENSE](LICENSE) 文件。

## 参考文献

### 论文

1. 程昱, 雷小峰. 五子棋Alpha-Beta搜索算法的研究与改进[J]. 信息技术, 2008(1):148-150.
2. 董洪安. 计算机五子棋博弈系统的研究与实现[D]. 西安电子科技大学, 2010.

