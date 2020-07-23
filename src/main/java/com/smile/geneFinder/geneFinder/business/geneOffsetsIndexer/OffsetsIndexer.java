package com.smile.geneFinder.geneFinder.business.geneOffsetsIndexer;
import com.smile.geneFinder.geneFinder.business.Configuration;
import com.smile.geneFinder.geneFinder.business.infra.TaskExecutor;

public class OffsetsIndexer extends TaskExecutor  {

    OffsetsStore offsetsStore;

    public OffsetsIndexer() {
        super(Configuration.offsetsExecutorNumThreads);
        this.offsetsStore = new OffsetsStore();
    }

    public void indexGeneOffsets() {
        OffsetIndexerTask task = new OffsetIndexerTask(this.latch, this.offsetsStore);
        executeTask(task);
        // TODO: instead of sorting, save offsets per bulk and then iterate over all bulks to assemble an order
        this.offsetsStore.sort();
    }

    public OffsetsStore getOffsetsStore() {
        return this.offsetsStore;
    }
}
