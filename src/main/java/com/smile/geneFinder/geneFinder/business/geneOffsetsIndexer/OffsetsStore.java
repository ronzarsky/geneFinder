package com.smile.geneFinder.geneFinder.business.geneOffsetsIndexer;
import java.util.Collections;
import java.util.concurrent.CopyOnWriteArrayList;

//TODO: implement the offset store using redis, or some other external store
public class OffsetsStore {

    private CopyOnWriteArrayList<Long> elements;

    public OffsetsStore() {
        this.elements = new CopyOnWriteArrayList<>();
    }

    public void storeOffset(long geneOffset) {
        this.elements.add(geneOffset);
    }

    public long getOffsetByIndex(long index) {
        return this.elements.get((int)index);
    }

    public long size() {
        return elements.size();
    }

    public void sort() {
        Collections.sort(this.elements);
    }
}
