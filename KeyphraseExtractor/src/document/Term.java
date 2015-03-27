package document;

import java.util.Map;
import java.util.HashMap;

/**
 *
 * @author Alexander Brown - 10019841
 */
public class Term {

    private String stemmedText;
    private Map occurrences;

    private double frequency; // 0
    private double firstPos;  // 1
    private double lastPos; // 2
    private double avgPos; // 3
    private double firstSentence; // 4
    private double lastSentence; // 5
    private short numberOfWords;
    private boolean inFirstSentence; // 6
    private boolean inLastSentence; // TODO
    private boolean hasProperNoun; // TODO?
    private int x2Score; // TODO?
    private double firstParagraph; // 7
    private double lastParagraph; // 8
    private double averageSentencePos; // 9
    private double averageParagraphPos; // 10
    private double freqInFirst10; // 11
    private double freqInFirst20; // 12
    private double freqInLast10; // 13
    private double freqInLast20; // 14
    private double freqInFirst1P; // 15
    private double freqInFirst2P; // 16
    private double freqInLast1P; // 17
    private double freqInLast2P; // 18

    public Term(String text, String stemmedText, double position, double paragraph, double sentencePos, double paragraphPos, int docLength) {
        this.occurrences = new HashMap<>();
        this.addOccurrence(text, docLength);
        this.stemmedText = stemmedText;
        this.firstPos = position;
        this.avgPos = position;
        this.lastPos = position;
        this.firstParagraph = paragraph;
        this.lastParagraph = paragraph;
        this.averageParagraphPos = paragraphPos;
        this.averageSentencePos = sentencePos;
        this.numberOfWords = (short) text.split(" ").length;
        this.freqInFirst10 = 0;
        this.freqInFirst20 = 0;
        this.freqInLast10 = 0;
        this.freqInLast20 = 0;
    }

    public String getMostFrequentOccurrence() {
        String mostFrequent = "";
        int highestFrequency = 0;
        for (Object term : this.occurrences.keySet()) {
            if ((int) this.occurrences.get(term) > highestFrequency) {
                highestFrequency = (int) this.occurrences.get(term);
                mostFrequent = (String) term;
            }
        }
        return mostFrequent;
    }

    public String getStemmedText() {
        return stemmedText;
    }

    public void setStemmedText(String stemmedText) {
        this.stemmedText = stemmedText;
    }

    public void addOccurrence(String term, int docLength) {
        this.frequency = ((double) this.getTermCount() + 1) / (double) docLength;
        if (this.occurrences.containsKey(term)) {
            this.occurrences.put(term, ((int) this.occurrences.get(term) + 1));
        } else {
            this.occurrences.put(term, 1);
        }
    }

    public void updateAvgPos(double newValue) {
        this.avgPos = recalcAvg(this.avgPos, newValue, this.getTermCount());
    }

    public void updateAverageSentencePos(double newValue) {
        this.averageSentencePos = recalcAvg(this.averageSentencePos, newValue, this.getTermCount());
    }

    public void updateAverageParagraphPos(double newValue) {
        this.averageParagraphPos = recalcAvg(this.averageParagraphPos, newValue, this.getTermCount());
    }

    // Used to recalculate a mean with a new value added
    private double recalcAvg(double currentAvg, double newValue, int newTotal) {
        double newAvg = currentAvg * (newTotal - 1);
        newAvg = (newAvg + newValue) / newTotal;
        return newAvg;
    }

    public int getTermCount() {
        int count = 0;
        for (Object occCount : this.occurrences.values()) {
            count += (int) occCount;
        }
        return count;
    }

    /*
     *
     * The following functions are to make life easier for simply incrementing 
     * values
     *
     */
    public void incrementFreqInFirst20(int docLength) {
        this.freqInFirst20 = +1 / (0.2 * ((double) docLength));
    }

    public void incrementFreqInFirst10(int docLength) {
        this.freqInFirst10 += 1 / (0.1 * ((double) docLength));
    }

    public void incrementFreqInLast20(int docLength) {
        this.freqInLast20 += 1 / (0.2 * ((double) docLength));
    }

    public void incrementFreqInLast10(int docLength) {
        this.freqInLast10 += 1 / (0.1 * ((double) docLength));
    }

    public void incrementFreqInFirst1P(int paraLength) {
        this.freqInFirst1P += 1 / (double) paraLength;
    }

    public void incrementFreqInFirst2P(int paraLength) {
        this.freqInFirst2P += 1 / (double) paraLength;
    }

    public void incrementFreqInLast1P(int paraLength) {
        this.freqInLast1P += 1 / (double) paraLength;
    }

    public void incrementFreqInLast2P(int paraLength) {
        this.freqInLast2P += 1 / (double) paraLength;
    }

    /*
     *
     * Only Getters and Setters Beyond here
     *
     */
    public Map getOccurrences() {
        return occurrences;
    }

    public void setOccurrences(Map occurrences) {
        this.occurrences = occurrences;
    }

    public double getFrequency() {
        return frequency;
    }

    public void setFrequency(double frequency) {
        this.frequency = frequency;
    }

    public double getFirstPos() {
        return firstPos;
    }

    public void setFirstPos(double firstPos) {
        this.firstPos = firstPos;
    }

    public double getLastPos() {
        return lastPos;
    }

    public void setLastPos(double lastPos) {
        this.lastPos = lastPos;
    }

    public double getAvgPos() {
        return avgPos;
    }

    public void setAvgPos(double avgPos) {
        this.avgPos = avgPos;
    }

    public double getFirstSentence() {
        return firstSentence;
    }

    public void setFirstSentence(double firstSentence) {
        this.firstSentence = firstSentence;
    }

    public double getLastSentence() {
        return lastSentence;
    }

    public void setLastSentence(double lastSentence) {
        this.lastSentence = lastSentence;
    }

    public int getNumberOfWords() {
        return numberOfWords;
    }

    public void setNumberOfWords(short numberOfWords) {
        this.numberOfWords = numberOfWords;
    }

    public boolean isInFirstSentence() {
        return inFirstSentence;
    }

    public void setInFirstSentence(boolean inFirstSentence) {
        this.inFirstSentence = inFirstSentence;
    }

    public boolean isInLastSentence() {
        return inLastSentence;
    }

    public void setInLastSentence(boolean inLastSentence) {
        this.inLastSentence = inLastSentence;
    }

    public boolean isHasProperNoun() {
        return hasProperNoun;
    }

    public void setHasProperNoun(boolean hasProperNoun) {
        this.hasProperNoun = hasProperNoun;
    }

    public int getX2Score() {
        return x2Score;
    }

    public void setX2Score(int x2Score) {
        this.x2Score = x2Score;
    }

    public double getFirstParagraph() {
        return firstParagraph;
    }

    public void setFirstParagraph(double firstParagraph) {
        this.firstParagraph = firstParagraph;
    }

    public double getLastParagraph() {
        return lastParagraph;
    }

    public void setLastParagraph(double lastParagraph) {
        this.lastParagraph = lastParagraph;
    }

    public double getAverageSentencePos() {
        return averageSentencePos;
    }

    public void setAverageSentencePos(double averageSentencePos) {
        this.averageSentencePos = averageSentencePos;
    }

    public double getAverageParagraphPos() {
        return averageParagraphPos;
    }

    public void setAverageParagraphPos(double averageParagraphPos) {
        this.averageParagraphPos = averageParagraphPos;
    }

    public double getFreqInFirst10() {
        return freqInFirst10;
    }

    public void setFreqInFirst10(double freqInFirst10) {
        this.freqInFirst10 = freqInFirst10;
    }

    public double getFreqInFirst20() {
        return freqInFirst20;
    }

    public void setFreqInFirst20(double freqInFirst20) {
        this.freqInFirst20 = freqInFirst20;
    }

    public double getFreqInLast10() {
        return freqInLast10;
    }

    public void setFreqInLast10(double freqInLast10) {
        this.freqInLast10 = freqInLast10;
    }

    public double getFreqInLast20() {
        return freqInLast20;
    }

    public void setFreqInLast20(double freqInLast20) {
        this.freqInLast20 = freqInLast20;
    }

    public double getFreqInFirst1P() {
        return freqInFirst1P;
    }

    public void setFreqInFirst1P(double freqInFirst1P) {
        this.freqInFirst1P = freqInFirst1P;
    }

    public double getFreqInFirst2P() {
        return freqInFirst2P;
    }

    public void setFreqInFirst2P(double freqInFirst2P) {
        this.freqInFirst2P = freqInFirst2P;
    }

    public double getFreqInLast1P() {
        return freqInLast1P;
    }

    public void setFreqInLast1P(double freqInLast1P) {
        this.freqInLast1P = freqInLast1P;
    }

    public double getFreqInLast2P() {
        return freqInLast2P;
    }

    public void setFreqInLast2P(double freqInLast2P) {
        this.freqInLast2P = freqInLast2P;
    }

}
