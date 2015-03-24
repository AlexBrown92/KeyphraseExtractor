
import document.Document;
import document.Paragraph;
import document.Term;
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
        HashMap<String, TestDocument> trainingData = new HashMap<>();

        File stopWordsFile = new File("D:\\Dropbox\\Work\\Year 3\\Project\\Resources\\mysql_stopwords.txt");
        Scanner stopWordsScan = new Scanner(stopWordsFile);
        ArrayList<String> stopWords = new ArrayList<>();
        while (stopWordsScan.hasNextLine()) {
            stopWords.add(stopWordsScan.nextLine());
        }
        stopWords.add("-"); // Needed to stop problems with spaced hyphons
        stopWords.add("a"); // Not sure why this was ever missing
/*
         File in = new File("D:\\Dropbox\\Work\\Year 3\\Project\\Resources\\input_test.txt");
         //File in = new File("D:\\Dropbox\\Work\\Year 3\\Project\\Datasets\\MAUI\\MAUI\\fao30\\all\\a0011e00.txt");
         Scanner scan = new Scanner(in);
         scan.useDelimiter("\\Z");
         String text = scan.next();
         */
        File folder = new File("D:\\Dropbox\\Work\\Year 3\\Project\\Datasets\\MAUI\\MAUI\\citeulike180\\all\\");
        //File folder = new File("D:\\Dropbox\\Work\\Year 3\\Project\\Datasets\\MAUI\\MAUI\\fao780");
        //File folder = new File("D:\\Project_Temp\\Combined_Data\\");

        for (final File fileEntry : folder.listFiles()) {
            if (!fileEntry.isDirectory()) {
                String fName = fileEntry.getName().substring(0, fileEntry.getName().lastIndexOf('.'));
                System.out.println(fileEntry.getName());
                if (fileEntry.getName().endsWith(".txt")) {
                    FileInputStream fis = new FileInputStream(fileEntry);
                    byte[] data = new byte[(int) fileEntry.length()];
                    fis.read(data);
                    fis.close();
                    String text = new String(data, "UTF-8");
                    trainingData.put(fName, new TestDocument(text, stopWords));
                    File keys = new File(folder.getAbsolutePath() + "\\" + fName + ".key");
                    if (keys.exists()) {
                        Scanner keyScanner = new Scanner(keys);
                        while (keyScanner.hasNextLine()) {
                            trainingData.get(fName).addKnownKeyword(keyScanner.nextLine().trim());
                        }
                    } else {
                        // If we don't have any known keywords the file is no good to us for training! 
                        trainingData.remove(fName);
                    }
                }
            }
        }
        System.out.println("hit");

        /*
         Document d = new Document(text, stopWords);

         for (Paragraph p : d.getParagraphs()) {
         System.out.println(p.toString());
         }

         for (Term t : d.getTerms().values()) {
         System.out.println(t.getTermCount() + " | " + t.getFrequency() + " | " + t.getFreqInFirst1P() + " " + t.getFreqInFirst2P() + " " + t.getFreqInLast1P() + " " + t.getFreqInLast2P() + " " + t.getMostFrequentOccurrence());
         }
         */
    }

}
