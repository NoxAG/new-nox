package com.noxag.newnox.textanalyzer.data;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents the result of a statistical analysis
 * 
 * @author Pascal.Schroeder@de.ibm.com
 *
 */
public class StatisticFinding extends Finding {

    private StatisticFindingType type;
    private List<StatisticFindingData> statisticData;
    private String chartName, xAxisLabel, yAxisLabel, dataLineLabel;
    private boolean Sort;

    public enum StatisticFindingType {
        FOREIGN_WORDS, VOCABULARY_DISTRIBUTION, PUNCTUATION_DISTRIBUTION, COMMON_ABBREVIATION, COMMON_FOREIGN_WORD, WORDING, SENTENCE_COMPLEXITY;
    }

    public StatisticFinding() {
        this(null);
    }

    public StatisticFinding(StatisticFindingType typ) {
        this(typ, new ArrayList<StatisticFindingData>());
    }

    public StatisticFinding(StatisticFindingType typ, List<StatisticFindingData> data) {
        this(typ, data, "Histogramm", true);
    }

    public StatisticFinding(StatisticFindingType typ, List<StatisticFindingData> data, boolean shouldSort) {
        this(typ, data, "Histogramm", shouldSort);
    }

    public StatisticFinding(StatisticFindingType typ, List<StatisticFindingData> data, String chartName,
            boolean shouldSort) {
        this.type = typ;
        this.statisticData = data;
        this.chartName = chartName;
        this.Sort = shouldSort;
        assignChartName(this.type);
        assignAxisLabel(this.type);
    }

    public StatisticFindingType getType() {
        return type;
    }

    public void setType(StatisticFindingType type) {
        this.type = type;
    }

    public String getChartName() {
        return chartName;
    }

    public void setChartName(String chartName) {
        this.chartName = chartName;
    }

    public String getxAxisLabel() {
        return xAxisLabel;
    }

    public void setxAxisLabel(String xAxisLabel) {
        this.xAxisLabel = xAxisLabel;
    }

    public String getyAxisLabel() {
        return yAxisLabel;
    }

    public void setyAxisLabel(String yAxisLabel) {
        this.yAxisLabel = yAxisLabel;
    }

    public String getDataLineLabel() {
        return dataLineLabel;
    }

    public void setDataLineLabel(String dataLineLabel) {
        this.dataLineLabel = dataLineLabel;
    }

    public List<StatisticFindingData> getStatisticData() {
        return statisticData;
    }

    public void setStatisticData(List<StatisticFindingData> statisticData) {
        this.statisticData = statisticData;
    }

    public void addStatisticData(StatisticFindingData statisticData) {
        this.statisticData.add(statisticData);
    }

    public void addStatisticData(List<StatisticFindingData> statisticData) {
        this.statisticData.addAll(statisticData);
    }

    private void assignChartName(StatisticFindingType typ) {
        switch (typ) {
        case VOCABULARY_DISTRIBUTION:
            this.chartName = StatisticFindingConstants.VOCABULARY_TITLE;
            break;
        case PUNCTUATION_DISTRIBUTION:
            this.chartName = StatisticFindingConstants.PUNCTUATION_TITLE;
            break;
        case COMMON_ABBREVIATION:
            this.chartName = StatisticFindingConstants.ABBREVIATION_TITLE;
            break;
        case COMMON_FOREIGN_WORD:
            this.chartName = StatisticFindingConstants.FOREIGN_TITLE;
            break;
        case WORDING:
            this.chartName = StatisticFindingConstants.WORDING_TITLE;
            break;
        case SENTENCE_COMPLEXITY:
            this.chartName = StatisticFindingConstants.SENTENCE_COMPLEXITY_TITLE;
            break;
        case FOREIGN_WORDS:
            this.chartName = StatisticFindingConstants.FOREIGN_TITLE;
            break;
        }
    }

    private void assignAxisLabel(StatisticFindingType typ) {
        switch (typ) {
        case VOCABULARY_DISTRIBUTION:
            this.xAxisLabel = StatisticFindingConstants.VOCABULARY_XLABEL;
            this.yAxisLabel = StatisticFindingConstants.VOCABULARY_YLABEL;
            break;
        case PUNCTUATION_DISTRIBUTION:
            this.xAxisLabel = StatisticFindingConstants.PUNCTUATION_XLABEL;
            this.yAxisLabel = StatisticFindingConstants.PUNCTUATION_YLABEL;
            break;
        case COMMON_ABBREVIATION:
            this.xAxisLabel = StatisticFindingConstants.ABBREVIATION_XLABEL;
            this.yAxisLabel = StatisticFindingConstants.ABBREVIATION_YLABEL;
            break;
        case COMMON_FOREIGN_WORD:
            this.xAxisLabel = StatisticFindingConstants.FOREIGN_XLABEL;
            this.yAxisLabel = StatisticFindingConstants.FOREIGN_YLABEL;
            break;
        case WORDING:
            this.xAxisLabel = StatisticFindingConstants.WORDING_XLABEL;
            this.yAxisLabel = StatisticFindingConstants.WORDING_YLABEL;
            break;
        case SENTENCE_COMPLEXITY:
            this.xAxisLabel = StatisticFindingConstants.SENTENCE_COMPLEXITY_XLABEL;
            this.yAxisLabel = StatisticFindingConstants.SENTENCE_COMPLEXITY_YLABEL;
            break;
        case FOREIGN_WORDS:
            this.xAxisLabel = StatisticFindingConstants.FOREIGN_XLABEL;
            this.yAxisLabel = StatisticFindingConstants.FOREIGN_YLABEL;
            break;
        }
    }

    public boolean isSort() {
        return Sort;
    }

    public void setSort(boolean sort) {
        Sort = sort;
    }

}
