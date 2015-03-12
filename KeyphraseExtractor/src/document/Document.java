package document;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import stemmer.*;

/**
 *
 * @author Alexander Brown - 10019841
 */
public class Document {

    private String text;
    private int length;
    private HashMap<String, Term> terms;
    private ArrayList<Paragraph> paragraphs;
    private final int STEM_LENGTH = 3;
    private ArrayList<String> stopWords;

    public Document(String text, ArrayList<String> stopWords) {
        this.stopWords = stopWords;
        this.text = text;
        this.paragraphs = new ArrayList<>();
        this.terms = new HashMap<>();
        String[] p_temp = text.split("\n");
        IteratedLovinsStemmer stemmer = new IteratedLovinsStemmer();

        // Identify Terms
        for (String paragraph : p_temp) {
            // Check to see if paragraph is empty
            if (!paragraph.equals("\r") && !paragraph.trim().isEmpty()) {
                Paragraph p = new Paragraph(paragraph);
                paragraphs.add(p);
                for (String s : p.getSentences()) {
                    s = s.replaceAll("[^A-Za-z0-9 '-]", "");
                    String[] words = s.split(" ");
                    for (int i = 0; i < words.length; i++) {
                        String word = words[i].trim();
                        String stemmed = stemmer.stem(word); // ideally stem here...
                        // Make sure term isn't on the stop words list
                        if (!stopWords.contains(word) && !stopWords.contains(stemmed)) {
                            addTerm(word, stemmed);
                            // If there are still more words
                            if (words.length > i+1){
                                String word2 = words[i+1].trim();
                                String stem2 = stemmer.stem(word2);
                                String combinedStem = stemmed.concat(" " + stem2); // Actually do some stemming on words[i+1]
                                String combinedTerm = word.concat(" " + word2);
                                // Don't end a term with a stop word
                                if (!stopWords.contains(word2.toLowerCase())){
                                    addTerm(combinedTerm, combinedStem);
                                }
                                if (words.length > i+2){
                                    String word3 = words[i+2].trim();
                                    String stem3 = stemmer.stem(word3);
                                    if (!stopWords.contains(word3.toLowerCase())){
                                        combinedStem = combinedStem.concat(" " + stem3); // Actually do some stemming on words[i+2]
                                        combinedTerm = combinedTerm.concat(" " + word3);
                                        addTerm(combinedTerm, combinedStem);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    
    private void addTerm(String term, String stemmed){
        if (!terms.containsKey(stemmed)){
            terms.put(stemmed, new Term(term, stemmed));
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

    public HashMap<String, Term> getTerms() {
        return terms;
    }

    public void setTerms(HashMap<String, Term> terms) {
        this.terms = terms;
    }

    public ArrayList<Paragraph> getParagraphs() {
        return paragraphs;
    }

    public void setParagraphs(ArrayList<Paragraph> paragraphs) {
        this.paragraphs = paragraphs;
    }

    public ArrayList<String> getStopWords() {
        return stopWords;
    }

    public void setStopWords(ArrayList<String> stopWords) {
        this.stopWords = stopWords;
    }
    
    

}
