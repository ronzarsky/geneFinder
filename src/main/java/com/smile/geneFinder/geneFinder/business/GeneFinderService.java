package com.smile.geneFinder.geneFinder.business;
import com.smile.geneFinder.geneFinder.business.geneOffsetsIndexer.OffsetsIndexer;
import com.smile.geneFinder.geneFinder.business.geneOffsetsIndexer.OffsetsStore;
import com.smile.geneFinder.geneFinder.business.genePrefixTree.PrefixTree;
import com.smile.geneFinder.geneFinder.business.genePrefixTree.PrefixTreeBuilder;
import org.springframework.stereotype.Service;

@Service
public class GeneFinderService {

    PrefixTree prefixTree;

    public GeneFinderService() {
    }

    public boolean isValidGene(String gene) {
        String geneValidPrefix = Configuration.genePrefix;
        if (gene.length() <= geneValidPrefix.length()) {
            return false;
        }

        return gene.substring(0, geneValidPrefix.length()).equals(geneValidPrefix);
    }

    public Boolean isMatch(String gen) {
        return this.prefixTree.isMatch(gen);
    }

    public boolean isReady() {
        return this.prefixTree != null;
    }

    public OffsetsStore calcOffsets() {
        OffsetsIndexer geneOffsetsIndexer = new OffsetsIndexer();
        geneOffsetsIndexer.indexGeneOffsets();
        return geneOffsetsIndexer.getOffsetsStore();
    }

    public void initService() {
        //Step 1: find offsets of all genes in the dna file
        OffsetsStore offsets = calcOffsets();
        //Step 2: - insert strings at offsets into the a prefix tree
        //        - any matching gene is associated to a valid path in the tree
        PrefixTreeBuilder prefixTreeBuilder = new PrefixTreeBuilder(offsets);
        this.prefixTree = prefixTreeBuilder.createGenePrefixTree();
    }
}
