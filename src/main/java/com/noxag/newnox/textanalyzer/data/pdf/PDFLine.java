package com.noxag.newnox.textanalyzer.data.pdf;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.pdfbox.text.TextPosition;

/**
 * 
 * @author Tobias.Schmidt@de.ibm.com
 *
 */
public class PDFLine implements PDFObject {
    List<TextPositionSequence> words;

    public PDFLine() {
        words = new ArrayList<>();
    }

    public PDFLine(TextPositionSequence... words) {
        this.setWords(Arrays.asList(words));
    }

    public PDFLine(List<TextPositionSequence> words) {
        this.setWords(words);
    }

    @Override
    public List<TextPositionSequence> getWords() {
        return words;
    }

    public void setWords(List<TextPositionSequence> words) {
        this.words = words;
    }

    public TextPositionSequence getFirstWord() {
        return words.get(0);
    }

    public TextPositionSequence getLastWord() {
        return words.get(words.size() - 1);
    }

    public TextPositionSequence getTextPositionSequence() {
        if (words.isEmpty()) {
            return null;
        }
        if (this.getFirstWord() == null) {
            return null;
        }
        List<TextPosition> charPositions = new ArrayList<>();
        charPositions.addAll(this.getFirstWord().getTextPositions());
        charPositions.addAll(this.getLastWord().getTextPositions());
        return new TextPositionSequence(charPositions, this.getFirstWord().getPageIndex());
    }

    public void addAll(List<TextPositionSequence> words) {
        if (!words.isEmpty()) {
            this.getWords().addAll(words);
        }
    }

    public void add(TextPositionSequence word) {
        if (word != null) {
            this.getWords().add(word);
        }
    }

    @Override
    public String toString() {
        return words.stream().map(TextPositionSequence::toString).map(str -> str + " ").reduce(String::concat)
                .orElseGet(() -> super.toString());
    }

}
