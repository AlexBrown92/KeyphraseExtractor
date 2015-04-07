package ga.data;

import keyphraseextractor.document.Document;
import java.util.ArrayList;

/**
 *
 * @author Alex
 */
public class TestDocument extends Document {

    private ArrayList<String> knownKeywords;
    private String docName;

    public TestDocument(String text, ArrayList<String> stopWords) {
        super(text, stopWords);
        this.knownKeywords = new ArrayList<>();
    }

    public void addKnownKeyword(String keyword) {
        this.knownKeywords.add(keyword);
    }

    public String getDocName() {
        return docName;
    }

    public void setDocName(String docName) {
        this.docName = docName;
    }

    public ArrayList<String> getKnownKeywords() {
        return knownKeywords;
    }

    public void setKnownKeywords(ArrayList<String> knownKeywords) {
        this.knownKeywords = knownKeywords;
    }

}
