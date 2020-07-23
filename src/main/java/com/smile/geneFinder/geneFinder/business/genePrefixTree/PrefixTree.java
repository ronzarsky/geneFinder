package com.smile.geneFinder.geneFinder.business.genePrefixTree;

public class PrefixTree {
    TrieNode root;

    public PrefixTree() {
        this.root = new TrieNode("root");
    }

    public void insertGene(String gene) {
        this.root.insertString(gene);
    }

    public boolean isMatch(String s) {
        return this.root.isMatch(s);
    }

}
