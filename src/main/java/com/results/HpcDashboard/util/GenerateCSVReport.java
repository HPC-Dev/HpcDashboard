package com.results.HpcDashboard.util;

import com.opencsv.CSVWriter;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvException;
import java.io.PrintWriter;
import java.util.List;

import com.results.HpcDashboard.models.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GenerateCSVReport {

    private static final Logger LOGGER = LoggerFactory.getLogger(GenerateCSVReport.class);

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static void writeResults(PrintWriter writer, List<Result> results)  {

        try {

            ColumnPositionMappingStrategy mapStrategy
                    = new ColumnPositionMappingStrategy();

            mapStrategy.setType(Result.class);

            String[] columns = new String[]{"jobId", "appName", "bmName", "cores", "cpu", "nodeName", "nodes", "result"};
            mapStrategy.setColumnMapping(columns);
            mapStrategy.generateHeader(columns);
            StatefulBeanToCsv btcsv = new StatefulBeanToCsvBuilder(writer)
                    .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
                    .withMappingStrategy(mapStrategy)
                    .withSeparator(',')
                    .build();

            btcsv.write(results);

        } catch (CsvException ex) {

            LOGGER.error("Error mapping Bean to CSV", ex);
        }
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static void writeResult(PrintWriter writer, Result result) {

        try {

            ColumnPositionMappingStrategy mapStrategy
                    = new ColumnPositionMappingStrategy();

            mapStrategy.setType(Result.class);

            String[] columns = new String[]{"jobId", "appName", "bmName", "cores", "cpu", "nodeName", "nodes", "result"};
            mapStrategy.setColumnMapping(columns);

            StatefulBeanToCsv btcsv = new StatefulBeanToCsvBuilder(writer)
                    .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
                    .withMappingStrategy(mapStrategy)
                    .withSeparator(',')
                    .build();

            btcsv.write(result);

        } catch (CsvException ex) {

            LOGGER.error("Error mapping Bean to CSV", ex);
        }
    }
}
