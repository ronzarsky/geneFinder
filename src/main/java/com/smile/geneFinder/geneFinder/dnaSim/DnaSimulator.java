package com.smile.geneFinder.geneFinder.dnaSim;
import com.smile.geneFinder.geneFinder.business.GeneFinderService;
import com.smile.geneFinder.geneFinder.business.Configuration;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import  java.util.Random;

public class DnaSimulator {
    static int numPrefixes = 10000;
    static int maxGeneLen = 30;
    static int minGeneLen = 10;
    static char[] geneChars = {'G', 'C', 'V', 'T'};
    static String simulatedDnaFileName = "dna.txt";

    public static char randomizeGeneChar() {
        Random rand = new Random();
        int charIndex = rand.nextInt(geneChars.length);
        return geneChars[charIndex];
    }

    public static String randomizeGene(int len) {
        String gene = Configuration.genePrefix;
        for (int i = 0; i < len; i++) {
            gene += randomizeGeneChar();
        }

        return gene;
    }

    public static String buildDnaData(String[] simulatedGenes){
        String dna = "";
        Random rand = new Random();
        for (int i = 0; i < numPrefixes; i++) {
            int geneLen = rand.nextInt((maxGeneLen - minGeneLen) + 1) + minGeneLen;
            String randGene = randomizeGene(geneLen);
            dna += randGene;
            simulatedGenes[i] = randGene;
        }

        return dna;
    }

    public static String[] buildDnaFile() throws IOException {
        File file = new File(simulatedDnaFileName);
        file.createNewFile();
        FileWriter writer = new FileWriter(simulatedDnaFileName);
        String[] simulatedGenes = new String[numPrefixes];
        String dna = buildDnaData(simulatedGenes);
        writer.write(dna);
        writer.close();
        return simulatedGenes;
    }

    public static void main(String[] args) throws  IOException {
        String[] simulatedGenes = buildDnaFile();
        GeneFinderService geneFinderService = new GeneFinderService();
        geneFinderService.initService();
        for (int i = 0; i < numPrefixes; i++) {
            boolean isMatch = geneFinderService.isMatch(simulatedGenes[8]);
            if (isMatch) {
                System.out.println("dna simulator main, is match:" + isMatch + ", i = " + i);
            } else {
                System.out.println("dna simulator main, NOT match:" + isMatch + ", i = " + i);
                return;
            }
        }
        System.out.println("all matched");
        boolean isMatch = geneFinderService.isMatch(Configuration.genePrefix + "GCCCAAATCGGG");
        System.out.println("phony gene test:" + isMatch);
    }
}
