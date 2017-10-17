package com.noxag.newnox.textlogic;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.color.PDColor;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotation;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotationTextMarkup;

import com.noxag.newnox.textanalyzer.data.TextFinding;
import com.noxag.newnox.textanalyzer.data.TextFinding.TextFindingType;
import com.noxag.newnox.textanalyzer.data.pdf.TextPositionSequence;
import com.noxag.newnox.textanalyzer.util.PDFColors;

/**
 * This class transforms {@link TextFinding}s to TextMarkups and adds those to a
 * {@link PDDocument}
 * 
 * @author Tobias.Schmidt@de.ibm.com
 *
 */
public class PDFTextMarker {

    private PDFTextMarker() {
        // hide constructor, because this is a completely static class
    }

    private static final Logger LOGGER = Logger.getLogger(PDFTextMarker.class.getName());
    private static final PDColor DEFAULT_COLOR = PDFColors.YELLOW;
    private static final String DEFAULT_SUB_TYPE = PDAnnotationTextMarkup.SUB_TYPE_HIGHLIGHT;

    /**
     * Transforms each {@link TextFinding} into a Textmarkup and adds it to the
     * {@link PDDocument}
     * 
     * @param pdfDoc
     *            the document that contains the text that should be marked
     * @param textFindings
     *            the textFindings to be transformed
     * @throws IOException
     *             if the annotations of the document can't be accessed
     */
    public static void addTextMarkups(PDDocument pdfDoc, List<TextFinding> textFindings) throws IOException {
        for (TextFinding finding : textFindings) {
            addTextMarkup(pdfDoc, finding.getTextPositionSequence(), toColor(finding.getType()),
                    toTextMarkupSubType(finding.getType()));
        }
    }

    private static void addTextMarkup(PDDocument doc, TextPositionSequence textPosition, PDColor color, String subType)
            throws IOException {
        doc.getPage(textPosition.getPageIndex() - 1).getAnnotations()
                .add(generateTextMarkupAnnotation(textPosition, color, subType));
    }

    private static PDAnnotation generateTextMarkupAnnotation(TextPositionSequence textPosition, PDColor color,
            String subType) {
        return generateTextMarkupAnnotation(textPosition, color, subType, 1f);
    }

    private static PDAnnotation generateTextMarkupAnnotation(TextPositionSequence annotationPosition, PDColor color,
            String subType, float opacity) {
        PDAnnotationTextMarkup txtMark = new PDAnnotationTextMarkup(subType);
        txtMark.setColor(color);
        txtMark.setConstantOpacity(opacity);
        txtMark.setRectangle(new PDRectangle(annotationPosition.getX(), annotationPosition.getY(),
                annotationPosition.getWidth(), annotationPosition.getHeight()));
        return txtMark;
    }

    public static void clearDocumentFromTextMarkups(PDDocument doc) {
        try {
            for (int pageNum = 1; pageNum <= doc.getNumberOfPages(); pageNum++) {
                doc.getPage(pageNum - 1).getAnnotations().clear();
            }
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "PDF could not acces annotations", e);
        }
    }

    private static PDColor toColor(TextFindingType type) {
        if (type == null) {
            return PDFColors.RED;
        }
        switch (type) {
        case POOR_WORDING:
            return PDFColors.RED;
        case SENTENCE_COMPLEXITY:
            return PDFColors.MAGENTA;
        case REPETITIV_WORDING:
            return PDFColors.ORANGE;
        case PAGINATION:
            return PDFColors.VIOLET;
        case TABLE_OF_CONTENT:
            return PDFColors.ORANGE;
        case LIST_OF_ABBREVIATIONS:
            return PDFColors.GREY;
        case TABLE_OF_FIGURES:
            return PDFColors.DEEP_PINK;
        default:
            return DEFAULT_COLOR;
        }
    }

    private static String toTextMarkupSubType(TextFindingType type) {
        if (type == null) {
            return PDAnnotationTextMarkup.SUB_TYPE_HIGHLIGHT;
        }
        switch (type) {
        case POOR_WORDING:
            return PDAnnotationTextMarkup.SUB_TYPE_STRIKEOUT;
        case SENTENCE_COMPLEXITY:
            return PDAnnotationTextMarkup.SUB_TYPE_UNDERLINE;
        case REPETITIV_WORDING:
            return PDAnnotationTextMarkup.SUB_TYPE_HIGHLIGHT;
        case PAGINATION:
            return PDAnnotationTextMarkup.SUB_TYPE_HIGHLIGHT;
        case TABLE_OF_CONTENT:
            return PDAnnotationTextMarkup.SUB_TYPE_HIGHLIGHT;
        case LIST_OF_ABBREVIATIONS:
            return PDAnnotationTextMarkup.SUB_TYPE_UNDERLINE;
        case TABLE_OF_FIGURES:
            return PDAnnotationTextMarkup.SUB_TYPE_UNDERLINE;
        default:
            return DEFAULT_SUB_TYPE;
        }
    }
}
