package keyphraseextractor;


import keyphraseextractor.documentanalyser.DocumentAnalyser;
import ga.algorithm.GA;
import ga.data.TestDocument;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author Alex
 */
public class ExtractorTrainer {

    public void train(String filePath) {
        ArrayList<TestDocument> trainingData = new ArrayList<>();
        try {
            File stopWordsFile = new File("mysql_stopwords.txt");
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
            //File folder = new File("D:\\Dropbox\\Work\\Year 3\\Project\\Datasets\\MAUI\\MAUI\\citeulike180\\all\\");
            //File folder = new File("D:\\Dropbox\\Work\\Year 3\\Project\\Datasets\\MAUI\\MAUI\\fao780");
            //File folder = new File("D:\\Project_Temp\\Combined_Data\\");
            File folder = new File(filePath);
            int i = 0;
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
            DocumentAnalyser da = new DocumentAnalyser();
            GA ga = new GA(da, trainingData, true);
            ga.run();
        } catch (IOException ioE) {
            System.out.println(ioE.toString());
        }
    }
}
