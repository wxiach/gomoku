package cn.wxiach.core.ai.pattern.support;

import cn.wxiach.core.ai.pattern.Pattern;

import java.util.*;

public class AhoCorasickAutomaton {

    private final TrieNode root;

    public AhoCorasickAutomaton() {
        this.root = new TrieNode();
    }

    public AhoCorasickAutomaton(List<Pattern<String>> patterns) {
        this();
        patterns.forEach(this::insert);
        buildFailPointers();
    }

    public void insert(Pattern<String> pattern) {
        // Each time, a new node is added starting from the root node
        TrieNode node = root;
        for (char c: pattern.pattern().toCharArray()) {
            node = node.children.computeIfAbsent(c, k -> new TrieNode());
        }
        // Store the full pattern at the end of the node
        node.patterns.add(pattern);
    }

    public void buildFailPointers() {
        Queue<TrieNode> queue = new LinkedList<>();
        // The failure pointer of the root node points to itself
        root.fail = root;
        queue.add(root);

        // Use BFS to traverse the Trie tree
        while (!queue.isEmpty()) {
            TrieNode node = queue.poll();
            for (Map.Entry<Character, TrieNode> characterTrieNodeEntry : node.children.entrySet()) {
                char c = characterTrieNodeEntry.getKey();
                TrieNode child = characterTrieNodeEntry.getValue();
                // The failure pointer of the parent node
                TrieNode failNode = node.fail;

                // Let failNode jump up along the fail pointer until it finds a node that can match c
                while (failNode != root && !failNode.children.containsKey(c)) {
                    failNode = failNode.fail;
                }
                // If there is a c in the failNode, a suitable failure pointer has been found
                if (failNode.children.containsKey(c) && failNode.children.get(c) != child) {
                    child.fail = failNode.children.get(c);
                } else {
                    // Otherwise, fall back to the root node
                    child.fail = root;
                }

                // Inherit the matching pattern of the fail pointer
                child.patterns.addAll(child.fail.patterns);

                queue.add(child);
            }
        }
    }

    public Set<Pattern<String>> search(String text) {
        Set<Pattern<String>> matches = new HashSet<>();
        TrieNode node = root;
        for (char c : text.toCharArray()) {
            while (node != root && !node.children.containsKey(c)) {
                node = node.fail;
            }
            if (node.children.containsKey(c)) {
                node = node.children.get(c);
            }
            matches.addAll(node.patterns);
        }
        return matches;
    }

    static class TrieNode {
        Map<Character, TrieNode> children = new HashMap<>();
        TrieNode fail;
        List<Pattern<String>> patterns = new ArrayList<>();
    }
}
