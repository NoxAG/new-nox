package com.noxag.newnox.textanalyzer;

import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;

import com.noxag.newnox.textanalyzer.data.Finding;

public interface TextanalyzerAlgorithm {
    public List<? extends Finding> run(PDDocument doc);

    public String getUIName();
}
