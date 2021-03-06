package com.noxag.newnox.textanalyzer.algorithms;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.apache.pdfbox.pdmodel.PDDocument;

import com.noxag.newnox.textanalyzer.TextanalyzerAlgorithm;
import com.noxag.newnox.textanalyzer.data.CommentaryFinding;
import com.noxag.newnox.textanalyzer.data.Finding;
import com.noxag.newnox.textanalyzer.data.pdf.PDFPage;
import com.noxag.newnox.textanalyzer.data.pdf.PDFParagraph;
import com.noxag.newnox.textanalyzer.data.pdf.TextPositionSequence;
import com.noxag.newnox.textanalyzer.util.PDFTextAnalyzerUtil;
import com.noxag.newnox.textanalyzer.util.PDFTextExtractionUtil;
import com.opencsv.CSVReader;

/**
 * This class produces a commentary finding to show if the "Akademische
 * Aufrichtigkeitserklaerung" exists.
 * 
 * @author Lars.Dittert@de.ibm.com
 *
 */

public class AkademischeAufrichtigkeitserklaerung implements TextanalyzerAlgorithm {
    private static final Logger LOGGER = Logger.getLogger(AkademischeAufrichtigkeitserklaerung.class.getName());
    private static final String AUFRICHTIGKEITSERKLAERUNG_IDENTIFICATION_LIST_PATH = "src/main/resources/analyzer-conf/aufrichtigkeitserklaerung-hints.csv";
    private List<String> aufrichtigkeitserklaerungHints;

    public AkademischeAufrichtigkeitserklaerung() {
        aufrichtigkeitserklaerungHints = readAufrichtigkeitserklaerungIdentificationListFile(
                AUFRICHTIGKEITSERKLAERUNG_IDENTIFICATION_LIST_PATH);
    }

    @Override
    public List<Finding> run(PDDocument doc) {
        List<Finding> findings = new ArrayList<>();
        CommentaryFinding commentaryFinding;
        int foundPage = compareString(splitPagesIntoParagraphs(getNotContentPages(doc)));
        if (foundPage != 0) {
            commentaryFinding = new CommentaryFinding("Declaration of sincerity found", "DeclarationOfSincerity",
                    foundPage, 0);
            findings.add(commentaryFinding);
            return findings;
        }
        commentaryFinding = new CommentaryFinding("Declaration of sincerity not found", "DeclarationOfSincerity", 0, 0);
        findings.add(commentaryFinding);
        return findings;
    }

    public static List<PDFPage> getNotContentPages(PDDocument doc) {
        List<PDFPage> pages = new ArrayList<>();
        try {
            pages = PDFTextExtractionUtil.extractText(doc);
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Could not get pages from doc.", e);
            e.printStackTrace();
        }
        return pages.stream().filter(page -> !page.isContentPage()).collect(Collectors.toList());
    }

    public List<PDFParagraph> splitPagesIntoParagraphs(List<PDFPage> pages) {
        List<PDFParagraph> paragraphs = new ArrayList<>();
        pages.stream().forEach(page -> {
            page.getArticles().stream().forEach(article -> {
                article.getParagraphs().stream().forEach(paragraph -> {
                    paragraphs.add(paragraph);
                });
            });
        });
        return paragraphs;
    }

    public List<String> generateLowerCaseString(List<PDFPage> pages) {
        List<TextPositionSequence> words = new ArrayList<>();
        words = PDFTextExtractionUtil.extractWords(pages);

        return words.stream().filter(word -> !PDFTextAnalyzerUtil.isPunctuationMark(word))
                .map(TextPositionSequence::toString).map(String::toLowerCase).collect(Collectors.toList());
    }

    public int compareString(List<PDFParagraph> paragraphs) {
        for (PDFParagraph paragraph : paragraphs) {
            List<String> wordListOfParagraph = new ArrayList<>();
            paragraph.getLines().stream().forEach(line -> {
                line.getWords().stream().forEach(word -> {
                    wordListOfParagraph.add(word.toString().toLowerCase());
                });
            });
            if (aufrichtigkeitserklaerungHints.stream().allMatch(wordListOfParagraph::contains)) {
                return paragraph.getFirstLine().getFirstWord().getPageIndex();
            }
        }
        return 0;
    }

    private List<String> readAufrichtigkeitserklaerungIdentificationListFile(
            String aufrichtigkeitserklaerungIdentificationListPath) {
        List<String> aufrichtigkeitserklaerungIdentificationList = new ArrayList<>();
        try {
            CSVReader reader = new CSVReader(new FileReader(aufrichtigkeitserklaerungIdentificationListPath));
            String[] line;
            while ((line = reader.readNext()) != null) {
                Arrays.stream(line).forEach(identification -> {
                    aufrichtigkeitserklaerungIdentificationList.add(identification.trim().toLowerCase());
                });
            }
            reader.close();
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Configuration file could not be read", e);
        }
        return aufrichtigkeitserklaerungIdentificationList;
    }

    @Override
    public String getUIName() {
        return "Check for declaration of sincerity";
    }

}