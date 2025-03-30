package cn.wxiach.core.ai.pattern.support;

import cn.wxiach.core.ai.pattern.feature.FeaturePattern;
import cn.wxiach.model.Color;

public class AhoCorasickAutomatonFactory {
    private static final AhoCorasickAutomaton whitePieceAhoCorasickAutomaton;
    private static final AhoCorasickAutomaton blackPieceAhoCorasickAutomaton;

    static {
        whitePieceAhoCorasickAutomaton = new AhoCorasickAutomaton(FeaturePattern.getWhitePatterns());
        blackPieceAhoCorasickAutomaton = new AhoCorasickAutomaton(FeaturePattern.getBlackPatterns());
    }

    public static synchronized AhoCorasickAutomaton getInstance(Color color) {
        return color == Color.WHITE ? whitePieceAhoCorasickAutomaton : blackPieceAhoCorasickAutomaton;
    }
}

