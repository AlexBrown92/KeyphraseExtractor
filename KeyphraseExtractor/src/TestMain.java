
import Document.Document;
import Document.Paragraph;
import java.io.File;
import java.io.FileNotFoundException;
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
        Document d = new Document(text);
        
        for (Paragraph p : d.getParagraphs()) {
            System.out.println(p.toString());
        }
    }
    
}
