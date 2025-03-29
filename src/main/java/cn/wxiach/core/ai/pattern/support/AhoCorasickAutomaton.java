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
        TrieNode current = root;
        for (char c : pattern.pattern().toCharArray()) {
            current = current.children.computeIfAbsent(c, k -> new TrieNode());
        }
        // Store the full pattern at the end of the node
        current.patterns.add(pattern);
    }

    public void buildFailPointers() {
        Queue<TrieNode> queue = new LinkedList<>();

        // The first-tier node failure pointer points to root
        for (TrieNode child : root.children.values()) {
            child.fail = root;
            queue.add(child);
        }

        // Use BFS to traverse the Trie tree
        while (!queue.isEmpty()) {
            TrieNode current = queue.poll();

            for (Map.Entry<Character, TrieNode> characterTrieNodeEntry : current.children.entrySet()) {
                char c = characterTrieNodeEntry.getKey();
                TrieNode child = characterTrieNodeEntry.getValue();

                // Look for the longest valid prefix
                TrieNode fail = current.fail;

                // Let failNode jump up along the fail pointer until it finds a node that can match c
                while (fail != null && !fail.children.containsKey(c)) {
                    fail = fail.fail;
                }
                // If there is a c in the failure node, a suitable failure pointer has been found
                // Otherwise, fall back to the root node
                child.fail = (fail == null) ? root : fail.children.get(c);

                // Inherit the matching pattern of the fail pointer
                if (child.fail != null) {
                    child.patterns.addAll(child.fail.patterns);
                }

                queue.add(child);
            }
        }
    }

    public Set<Pattern<String>> search(String text) {
        Set<Pattern<String>> matches = new HashSet<>();
        TrieNode current = root;
        for (char c : text.toCharArray()) {
            while (current != null && !current.children.containsKey(c)) {
                current = current.fail;
            }
            current = (current == null) ? root : current.children.get(c);
            matches.addAll(current.patterns);
        }
        return matches;
    }

    static class TrieNode {
        Map<Character, TrieNode> children = new HashMap<>();
        TrieNode fail;
        List<Pattern<String>> patterns = new ArrayList<>();
    }
}
