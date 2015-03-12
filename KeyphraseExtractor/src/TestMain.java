
import Document.Document;
import Document.Paragraph;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;


/**
 *
 * @author Alex
 */
public class TestMain {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws FileNotFoundException {
        File in = new File("D:\\Dropbox\\Work\\Year 3\\Project\\Resources\\input_test.txt");
        Scanner scan = new Scanner(in);
        scan.useDelimiter("\\Z");
        String text = scan.next();   
        
        File stopWordsFile = new File("D:\\Dropbox\\Work\\Year 3\\Project\\Resources\\mysql_stopwords.txt");
        Scanner stopWordsScan = new Scanner(stopWordsFile);
        ArrayList<String> stopWords = new ArrayList<>();
        while (stopWordsScan.hasNextLine()){
            stopWords.add(stopWordsScan.nextLine());
        }
        stopWords.add("-"); // Needed to stop problems with spaced hyphons
        stopWords.add("a"); // Not sure why this was ever missing...
        Document d = new Document(text, stopWords);
        
        for (Paragraph p : d.getParagraphs()) {
            System.out.println(p.toString());
        }
        for (String term : d.getTerms().keySet()) {
            System.out.println(term);
        }
    }
    
}
