# Gomoku Game with AI

[English](README.md) | 中文

[![JDK](https://img.shields.io/badge/JDK-21-brightgreen.svg)](https://jdk.java.net/21/)
[![License](https://img.shields.io/badge/license-MIT-blue.svg)](LICENSE)
[![Build](https://img.shields.io/badge/build-passing-brightgreen.svg)]()

一个基于Java实现的五子棋游戏，支持与AI对战。

> 目前的AI水平很一般，仅仅能搜索到6层。应该还有很多BUG（逃~~）。

![Game Screenshot](screenshot/gomoku.png)

## 功能与技术实现

### 核心功能
- 支持人机对战，提供三个AI难度等级(初学者4层、业余6层、职业8层深度)
- 游戏辅助功能：悔棋、计时器、棋子颜色选择
- 简洁优雅的操作界面

### 技术特性
1. **AI决策系统**
   - 采用迭代加深的Alpha-Beta剪枝搜索
   - 结合棋型评估与威胁度分析的局面评估
   - 基于Aho-Corasick自动机的快速棋型识别

2. **性能优化**
   - 启发式走法排序提升搜索效率
   - Zobrist哈希实现局面缓存
   - 一维数组(15x15)高效存储棋盘
   - 对象缓存机制减少内存分配

3. **系统设计**
   - 采用EventBus实现UI与游戏逻辑的解耦，确保模块间的松耦合设计

## 项目结构
```
src/main/java/cn/wxiach/
├── core/           # 核心游戏逻辑
│   ├── model/     # 基础数据模型(棋盘、棋子等)
│   ├── rule/      # 游戏规则实现
│   ├── state/     # 游戏状态管理
│   └── utils/     # 工具类
├── robot/         # AI实现
│   ├── search/    # 搜索算法
│   ├── pattern/   # 棋型识别
│   └── evaluate/  # 局面评估
├── ui/            # 用户界面
│   ├── components/# UI组件
│   ├── assets/    # 资源文件
│   └── support/   # UI支持类
└── event/         # 事件系统
```

## 环境要求
- JDK 21 或更高版本
- Gradle 8.5 或更高版本

## 下一步计划

- [ ] **启发式排序优化**: 改进走法排序算法，引入历史启发表和杀手启发表，提升剪枝效率
- [ ] **算杀模块**: 实现VCT(Victory by Continuous Threats)算法，提升AI在关键局面的表现
- [ ] **开局库系统**: 引入标准开局库，提升开局阶段的走法质量和计算效率

## 贡献指南

欢迎提交Issue和Pull Request来帮助改进项目。

## 开源协议

本项目采用 MIT 协议开源，详见 [LICENSE](LICENSE) 文件。

## 参考文献

### 论文
1. 程宇, 雷小锋. 五子棋中Alpha-Beta搜索算法的研究与改进[J]. 信息技术, 2008(1):148-150.
2. 董红安. 计算机五子棋博弈系统的研究与实现[D]. 西安电子科技大学, 2010.

