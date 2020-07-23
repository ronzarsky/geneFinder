package com.smile.geneFinder.geneFinder.business.genePrefixTree;
import com.smile.geneFinder.geneFinder.business.Configuration;
import com.smile.geneFinder.geneFinder.business.geneOffsetsIndexer.OffsetsStore;
import com.smile.geneFinder.geneFinder.business.infra.TextBulkFetcher;
import java.io.File;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicLong;

public class PrefixTreeBuilderTask implements Runnable {

    CountDownLatch latch;

    TextBulkFetcher textBulkFetcher;

    long fileLen;

    PrefixTree prefixTree;

    final AtomicLong offsetCounter;

    OffsetsStore offsetsStore;

    public PrefixTreeBuilderTask(CountDownLatch latch, OffsetsStore offsetsStore, PrefixTree prefixTree) {
        this.latch = latch;
        String dnaFileName = Configuration.dnaFileName;
        this.textBulkFetcher = new TextBulkFetcher(Configuration.dnaFileName);
        File file = new File(dnaFileName);
        this.fileLen = file.length();
        this.offsetCounter = new AtomicLong(0);
        this.prefixTree = prefixTree;
        this.offsetsStore = offsetsStore;
    }

    private String getLastGene(long lastOffset) {
        return this.textBulkFetcher.fetchBulk(lastOffset, (int)(this.fileLen - lastOffset + 1));
    }

    private String getGene(long geneOffset, long nextGeneOffset) {
        return this.textBulkFetcher.fetchBulk(geneOffset, (int)(nextGeneOffset - geneOffset));
    }

    public void run() {
        while(true) {
            long currentOffsetCounter = offsetCounter.getAndIncrement();
            System.out.println(currentOffsetCounter);
            if (currentOffsetCounter >= offsetsStore.size()) {
                this.latch.countDown();
                break;
            }

            long geneOffset = offsetsStore.getOffsetByIndex(currentOffsetCounter);
            String gene;
            if (currentOffsetCounter == offsetsStore.size() - 1) {
                gene = getLastGene(geneOffset);
            } else {
                long nextGeneOffset = offsetsStore.getOffsetByIndex(currentOffsetCounter + 1);
                gene = getGene(geneOffset, nextGeneOffset);
            }
            prefixTree.insertGene(gene);
        }
    }
}
