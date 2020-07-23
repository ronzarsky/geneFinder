package com.smile.geneFinder.geneFinder.business.genePrefixTree;
import java.util.HashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TrieNode {
    HashMap<Character, TrieNode> children;
    String name ;
    Lock lock;

    public TrieNode(String name) {
        this.name = name;
        this.children = new HashMap<>();
        this.lock = new ReentrantLock();
    }

    public TrieNode insertChar(char c) {
        // TODO: use redis or graph db to do this part
        //       - do not select Neo4J because it is not possible to distribute the tree on different servers
        //       - Neo4J works with a single master, meaning all writes go through one server
        //       - More approprate: Janus Graph or redis-like key-value stores to implement the trie dictionaries
        //       - the lock is used here to ensure atomicity  on the tree operations
        //          - we don't want two different threads to create thhe same node at the same time
        //                --> without the lock this scanario may cause data loss
        lock.lock();
        if (this.children.containsKey(c)) {
            lock.unlock();
            return this.children.get(c);
        }

        TrieNode newTrieNode = new TrieNode(String.valueOf(c));
        this.children.put(c, newTrieNode);
        lock.unlock();
        return newTrieNode;
    }

    public TrieNode insertString(String s) {
        int n = s.length();
        TrieNode next = this;
        for (int i = 0; i < n; i++) {
            char nextChar = s.charAt(i);
            next = next.insertChar(nextChar);
        }

        return next;
    }

    public void show(String spaces) {
        System.out.println(spaces + name);
        if (children.keySet().isEmpty()) {
            return;
        }

        for (Character character : this.children.keySet()) {
            TrieNode child = children.get(character);
            child.show(spaces + "    ");
        }
    }

    public boolean isMatch(String s) {
        int n = s.length();
        TrieNode next = this;
        for (int i = 0; i < n; i++) {
            char nextChar = s.charAt(i);
            next = next.children.get(nextChar);
            if (next == null) {
                return false;
            }
        }

        return true;
    }
}
