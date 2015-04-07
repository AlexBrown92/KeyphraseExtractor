package keyphraseextractor;


import keyphraseextractor.document.Document;
import keyphraseextractor.documentanalyser.DocumentAnalyser;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Scanner;

/**
 *
 * @author Alex
 */
public class Extractor {
    public void extract(String fileLoc) {
        DocumentAnalyser da = new DocumentAnalyser();
        try {
            File stopWordsFile = new File("mysql_stopwords.txt");
            Scanner stopWordsScan = new Scanner(stopWordsFile);
            ArrayList<String> stopWords = new ArrayList<>();
            while (stopWordsScan.hasNextLine()) {
                stopWords.add(stopWordsScan.nextLine());
            }
            stopWords.add("-"); // Needed to stop problems with spaced hyphons
            stopWords.add("a"); // Not sure why this was ever missing

            File file = new File(fileLoc);

            FileInputStream fis = new FileInputStream(file);
            byte[] data = new byte[(int) file.length()];
            fis.read(data);
            fis.close();
            String text = new String(data, "UTF-8");
            InputStream input = null;
            Properties prop = new Properties();
            input = new FileInputStream("config.properties");
            // load a properties file
            prop.load(input);
            Document doc = new Document(text, stopWords);
            String[] geneStrings = prop.getProperty("gene").substring(1).split("\\s+");
            double[] rules = new double[geneStrings.length];
            for (int i = 0; i < geneStrings.length; i++) {
                rules[i] = Double.parseDouble(geneStrings[i]);
            }
            ArrayList<String> selectedKeywords = da.analyse(doc, rules, Integer.parseInt(prop.getProperty("subrules")));
            System.out.println("Selected Keyphrases:");
            for (String selectedKeyword : selectedKeywords) {
                System.out.print(selectedKeyword + ", ");
            }
        } catch (IOException ioE) {
            System.out.println(ioE.toString());
        }
    }

}
