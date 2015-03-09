package Document;

import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author Alexander Brown - 10019841
 */
public class Document {

    private String text;
    private int length;
    private ArrayList<Term> terms;
    private ArrayList<Paragraph> paragraphs;

    public Document(String text) {
        this.text = text;
        this.paragraphs = new ArrayList<>();
        this.terms = new ArrayList<>();
        String[] p_temp = text.split("\n");
        for (String paragraph : p_temp) {
            // Check to see if paragraph is empty
            if (!paragraph.equals("\r") && !paragraph.equals("")){
                paragraphs.add(new Paragraph(paragraph));
            }
        }

    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public ArrayList<Term> getTerms() {
        return terms;
    }

    public void setTerms(ArrayList<Term> terms) {
        this.terms = terms;
    }

    public ArrayList<Paragraph> getParagraphs() {
        return paragraphs;
    }

    public void setParagraphs(ArrayList<Paragraph> paragraphs) {
        this.paragraphs = paragraphs;
    }

    
}
