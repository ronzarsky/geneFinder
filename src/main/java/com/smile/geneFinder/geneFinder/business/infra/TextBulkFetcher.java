package com.smile.geneFinder.geneFinder.business.infra;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class TextBulkFetcher {

    public String textFilename;

    public TextBulkFetcher(String textFilename) {
        this.textFilename = textFilename;
    }

    public String fetchBulk(long bulkOffset, int bulkSize) {
        byte[] bulk = new byte[bulkSize];
        String text = null;
        try {
            RandomAccessFile f = new RandomAccessFile(this.textFilename, "r");
            f.seek(bulkOffset);
            f.read(bulk);
            text = new String(bulk);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return text;
    }
}
