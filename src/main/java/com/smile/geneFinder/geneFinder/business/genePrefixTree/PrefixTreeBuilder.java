package com.smile.geneFinder.geneFinder.business.genePrefixTree;
import com.smile.geneFinder.geneFinder.business.Configuration;
import com.smile.geneFinder.geneFinder.business.geneOffsetsIndexer.OffsetsStore;
import com.smile.geneFinder.geneFinder.business.infra.TaskExecutor;

public class PrefixTreeBuilder extends TaskExecutor {

    PrefixTree prefixTree;

    OffsetsStore offsetsStore;

    public PrefixTreeBuilder(OffsetsStore offsetsStore) {
        super(Configuration.prefixTreeExecutorNumThreads);
        this.offsetsStore = offsetsStore;
        this.prefixTree = new PrefixTree();
    }

    public PrefixTree createGenePrefixTree() {
        PrefixTreeBuilderTask task = new PrefixTreeBuilderTask(this.latch, this.offsetsStore,this.prefixTree);
        executeTask(task);
        return this.prefixTree;
    }

}
