package keyphraseextractor;

import keyphraseextractor.document.Document;
import keyphraseextractor.document.Paragraph;
import keyphraseextractor.document.Term;
import keyphraseextractor.documentanalyser.DocumentAnalyser;
import ga.algorithm.GA;
import ga.data.TestDocument;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

/**
 *
 * @author Alex
 */
public class TestMain {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws FileNotFoundException, IOException {
        ArrayList<TestDocument> trainingData = new ArrayList<>();

        File stopWordsFile = new File("mysql_stopwords.txt");
        Scanner stopWordsScan = new Scanner(stopWordsFile);
        ArrayList<String> stopWords = new ArrayList<>();
        while (stopWordsScan.hasNextLine()) {
            stopWords.add(stopWordsScan.nextLine());
        }
        stopWords.add("-"); // Needed to stop problems with spaced hyphons
        stopWords.add("a"); // Not sure why this was ever missing

        File in = new File("D:\\Dropbox\\Work\\Year 3\\Project\\Resources\\test.txt");
        //File in = new File("D:\\Dropbox\\Work\\Year 3\\Project\\Datasets\\MAUI\\MAUI\\fao30\\all\\a0011e00.txt");
        Scanner scan = new Scanner(in);
        scan.useDelimiter("\\Z");
        String inText = scan.next();

        File folder = new File("D:\\Dropbox\\Work\\Year 3\\Project\\Datasets\\MAUI\\MAUI\\citeulike180\\all\\");
        //File folder = new File("D:\\Dropbox\\Work\\Year 3\\Project\\Datasets\\MAUI\\MAUI\\fao780");
        //File folder = new File("D:\\Project_Temp\\Combined_Data\\");
        DocumentAnalyser da = new DocumentAnalyser();

        //tc1(inText, stopWords);
        
        Document d = new Document(inText, stopWords);
        tc2(d);
        /*
        int i = 0;
        for (final File fileEntry : folder.listFiles()) {
            if (!fileEntry.isDirectory()) {
                String fName = fileEntry.getName().substring(0, fileEntry.getName().lastIndexOf('.'));
                if (fileEntry.getName().endsWith(".txt")) {
                    FileInputStream fis = new FileInputStream(fileEntry);
                    byte[] data = new byte[(int) fileEntry.length()];
                    fis.read(data);
                    fis.close();
                    String text = new String(data, "UTF-8");
                    trainingData.add(new TestDocument(text, stopWords));
                    trainingData.get(i).setDocName(fName);
                    File keys = new File(folder.getAbsolutePath() + "\\" + fName + ".key");
                    if (keys.exists()) {
                        Scanner keyScanner = new Scanner(keys);
                        while (keyScanner.hasNextLine()) {
                            //trainingData.get(fName).addKnownKeyword(keyScanner.nextLine().trim());
                            trainingData.get(i).addKnownKeyword(keyScanner.nextLine().trim());
                        }
                        i++;
                    } else {
                        // If we don't have any known keywords the file is no good to us for training! 
                        trainingData.remove(i);
                    }
                }
            }
        }
        GA ga = new GA(da, trainingData, true);
        tc3(ga);*/
    }

    private static void tc1(String inText, ArrayList<String> stopWords) {
        System.out.println("Running TC1: ");
        Document d = new Document(inText, stopWords);
        for (String key : d.getTerms().keySet()) {
            System.out.println("TERM: " + key);
            for (Object full : d.getTerms().get(key).getOccurrences().keySet()) {
                System.out.println((String) full);
            }
        }
        System.out.println("=============================");
    }

    private static void tc2(Document d) {
        System.out.println("Running TC2: ");
        for (Term t : d.getTerms().values()) {
            System.out.println(t.getStemmedText() + ": " + t.getFrequency() + " " + t.getFirstPos() + " " + t.getLastPos() + " " + t.getAvgPos() + " " + t.getFirstSentence() + " " + t.getLastSentence() + " " + t.isInFirstSentence() + " " + t.getFirstParagraph() + " " + t.getLastParagraph() + " " + t.getAverageSentencePos() + " " + t.getAverageSentencePos() + " " + t.getFreqInFirst10() + " " + t.getFreqInFirst20() + " " + t.getFreqInLast10() + " " + t.getFreqInLast20() + " " + t.getFreqInFirst1P() + " " + t.getFreqInFirst2P() + " " + t.getFreqInLast1P() + " " + t.getFreqInLast2P());
        }
        System.out.println("Length: " + d.getLength());
        System.out.println("=============================");
    }

    private static void tc3(GA ga) {
        System.out.println("Running TC3: ");
        for (int i = 0; i < ga.getPop().getSize(); i++) {
            System.out.println(ga.getPop().getIndividual(i).getFitness() + " " + ga.getPop().getIndividual(i).displayGene());
        }
        ga.run();
        for (int i = 0; i < ga.getPop().getSize(); i++) {
            System.out.println(ga.getPop().getIndividual(i).getFitness() + " " + ga.getPop().getIndividual(i).displayGene());
        }
        System.out.println("=============================");
    }

}
