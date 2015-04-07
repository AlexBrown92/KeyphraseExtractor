package keyphraseextractor.documentanalyser;

import keyphraseextractor.document.Document;
import keyphraseextractor.document.Term;
import java.util.ArrayList;

/**
 *
 * @author Alexander Brown - 10019841
 */
public class DocumentAnalyser {

    public ArrayList<String> analyse(Document doc, double[] rules, int numSubrules) {
        ArrayList<String> selectedTerms = new ArrayList<>();
        for (Term t : doc.getTerms().values()) {
            boolean isMatching = true;
            int i = 0;
            int subrule = 1;
            //for (int i = 0; i < rules.length; i += (3*numSubrules)) {
            // Iterate through rule chunks
            while (i < rules.length) {
                int selected = (int) rules[i];
                double ub, lb;
                if (rules[i + 1] > rules[i + 2]) {
                    ub = rules[i + 1];
                    lb = rules[i + 2];
                } else {
                    ub = rules[i + 2];
                    lb = rules[i + 1];
                }
                double stat;
                switch (selected) {
                    case 0:
                        stat = t.getFrequency();
                        break;
                    case 1:
                        stat = t.getFirstPos();
                        break;
                    case 2:
                        stat = t.getLastPos();
                        break;
                    case 3:
                        stat = t.getAvgPos();
                        break;
                    case 4:
                        stat = t.getFirstSentence();
                        break;
                    case 5:
                        stat = t.getLastSentence();
                        break;
                    case 6:
                        if (t.isInFirstSentence()) {
                            stat = 1;
                        } else {
                            stat = 0;
                        }
                        break;
                    case 7:
                        stat = t.getFirstParagraph();
                        break;
                    case 8:
                        stat = t.getLastParagraph();
                        break;
                    case 9:
                        stat = t.getAverageSentencePos();
                        break;
                    case 10:
                        stat = t.getAverageParagraphPos();
                        break;
                    case 11:
                        stat = t.getFreqInFirst10();
                        break;
                    case 12:
                        stat = t.getFreqInFirst20();
                        break;
                    case 13:
                        stat = t.getFreqInLast10();
                        break;
                    case 14:
                        stat = t.getFreqInLast20();
                        break;
                    case 15:
                        stat = t.getFreqInFirst1P();
                        break;
                    case 16:
                        stat = t.getFreqInFirst2P();
                        break;
                    case 17:
                        stat = t.getFreqInLast1P();
                        break;
                    case 18:
                        stat = t.getFreqInLast2P();
                        break;
                    default:
                        stat = -1;
                }
                if ((stat < lb) || (stat > ub)) {
                    isMatching = false;
                }
                i += 3;
                if (subrule == numSubrules) {
                    if (isMatching) {
                        selectedTerms.add(t.getMostFrequentOccurrence());
                        break;
                    } else {
                        isMatching = true;
                    }
                    subrule = 1;
                }
                subrule++;
            }

        }
        return selectedTerms;
    }
}
