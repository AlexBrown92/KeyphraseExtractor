package document;

/**
 *
 * @author Alexander Brown - 10019841
 */
public class Term {
    private String text;
    private String stemmedText;
    private int frequency;
    private float firstPos;
    private float lastPos;
    private float avgPos;
    private int firstSentence;
    private int lastSentence;
    private int numberOfWords;
    private boolean inFirstSentence;
    private boolean inLastSentence;
    private boolean hasProperNoun;
    private int x2Score;
    private int firstParagraph;
    private int lastParagraph;
    private float averageSentencePos;
    private float averageParagraphPos;
    private int freqInFirst10;
    private int freqInFirst20;
    private int freqInLast10;
    private int freqInLast20;
    private int lexicalChain;
    private int directLexicalChain;
    private int lexicalChainSpan;
    private int directLexicalChainSpan;

    public Term(String text, String stemmedText) {
        this.text = text;
        this.stemmedText = stemmedText;
    }
    
    
    
}
