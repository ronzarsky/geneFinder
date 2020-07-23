package com.smile.geneFinder.geneFinder.business.geneOffsetsIndexer;
import com.smile.geneFinder.geneFinder.business.Configuration;
import com.smile.geneFinder.geneFinder.business.infra.TextBulkFetcher;
import java.io.File;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicLong;

public class OffsetIndexerTask implements  Runnable {

    OffsetsStore offsetsStore;

    final AtomicLong bulkCounter;

    String dnaFilename;

    long fileLen;

    String genePrefix;

    TextBulkFetcher textBulkFetcher ;

    int bulkSize;

    CountDownLatch latch;

    public OffsetIndexerTask(CountDownLatch latch, OffsetsStore offsetsStore) {
        this.offsetsStore = offsetsStore;
        this.bulkCounter = new AtomicLong(0);
        this.dnaFilename = Configuration.dnaFileName;
        File file = new File(dnaFilename);
        this.fileLen = file.length();
        this.genePrefix = Configuration.genePrefix;
        this.textBulkFetcher = new TextBulkFetcher(dnaFilename);
        this.bulkSize = Configuration.geneOffsetsIndexerBulkSize;
        this.latch = latch;
    }

    public void run() {
        while (true) {
            // atomically point the task to the bulk of data to be processed in this loop iteration
            // all threads are sharing the same atomic counter to ensure the bulks
            // will be distributed among threads
            long currentBulkCounter = bulkCounter.getAndIncrement();
            System.out.println("bulk count:" + currentBulkCounter);
            long bulkOffset = bulkSize * currentBulkCounter;

            // there are no more bulks to be processed
            if (bulkOffset  >= fileLen) {
                this.latch.countDown();
                break;
            }

            // if a prefix is split between two adjacent bulks
            // the prefix will be detected because we fetch the starting chars of the next bulk
            // [that's why we fetch (bulkSize + genePrefix.length()-1) instead of bulkSize ]
            String text = textBulkFetcher.fetchBulk(bulkOffset, bulkSize + genePrefix.length()-1);
            int fromIndex = 0;
            while (fromIndex < text.length()) {
                int offsetWithinBulk = text.indexOf(genePrefix, fromIndex);
                if (offsetWithinBulk == -1) {
                    break;
                }
                offsetsStore.storeOffset(bulkOffset + offsetWithinBulk);
                fromIndex = offsetWithinBulk + 1;
            }
        }
    }
}
