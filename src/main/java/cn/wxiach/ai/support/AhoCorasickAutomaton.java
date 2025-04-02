package cn.wxiach.ai.support;

import java.util.*;

public class AhoCorasickAutomaton<T> {

    private final TrieNode<T> root;
    private final CharSequenceConverter<T> converter;

    public AhoCorasickAutomaton(CharSequenceConverter<T> converter) {
        this.root = new TrieNode<>();
        this.converter = converter;
    }

    public AhoCorasickAutomaton(Collection<T> patterns, CharSequenceConverter<T> converter) {
        this(converter);
        patterns.forEach(this::insert);
        buildFailPointers();
    }

    public void insert(T pattern) {
        TrieNode<T> current = root;
        CharSequence charSequence = converter.toCharSequence(pattern);
        for (int i = 0; i < charSequence.length(); i++) {
            current = current.children.computeIfAbsent(charSequence.charAt(i), k -> new TrieNode<>());
        }
        current.patterns.add(pattern);
    }

    public void buildFailPointers() {
        Queue<TrieNode<T>> queue = new LinkedList<>();

        for (TrieNode<T> child : root.children.values()) {
            child.fail = root;
            queue.add(child);
        }

        while (!queue.isEmpty()) {
            TrieNode<T> current = queue.poll();

            for (Map.Entry<Character, TrieNode<T>> entry : current.children.entrySet()) {
                char c = entry.getKey();
                TrieNode<T> child = entry.getValue();

                TrieNode<T> fail = current.fail;
                while (fail != null && !fail.children.containsKey(c)) {
                    fail = fail.fail;
                }

                child.fail = (fail == null) ? root : fail.children.get(c);
                if (child.fail != null) {
                    child.patterns.addAll(child.fail.patterns);
                }

                queue.add(child);
            }
        }
    }

    public Set<T> search(String text) {
        Set<T> matches = new HashSet<>();
        TrieNode<T> current = root;
        for (char c : text.toCharArray()) {
            while (current != null && !current.children.containsKey(c)) {
                current = current.fail;
            }
            current = (current == null) ? root : current.children.get(c);
            matches.addAll(current.patterns);
        }
        return matches;
    }

    static class TrieNode<T> {
        Map<Character, TrieNode<T>> children = new HashMap<>();
        TrieNode<T> fail;
        List<T> patterns = new ArrayList<>();
    }
}
