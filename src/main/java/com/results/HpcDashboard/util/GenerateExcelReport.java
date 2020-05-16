package com.results.HpcDashboard.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import com.results.HpcDashboard.models.Result;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class GenerateExcelReport {

    public static ByteArrayInputStream resultsToExcel(List<Result> results) throws IOException {
        String[] COLUMNS = { "Job Id", "App Name", "Benchmark", "Nodes", "Cores", "Node Name", "Metric", "CPU","OS", "BIOS version", "Cluster", "User", "Platform", "CPU generation", "Run type", "Setting"  };
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream();) {
            Sheet sheet = workbook.createSheet("Results");

            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setColor(IndexedColors.BLUE.getIndex());

            CellStyle headerCellStyle = workbook.createCellStyle();
            headerCellStyle.setFont(headerFont);

            Row headerRow = sheet.createRow(0);

            for (int col = 0; col < COLUMNS.length; col++) {
                Cell cell = headerRow.createCell(col);
                cell.setCellValue(COLUMNS[col]);
                cell.setCellStyle(headerCellStyle);
            }

            int rowIdx = 1;
            for (Result result : results) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(result.getJobId());
                row.createCell(1).setCellValue(result.getAppName());
                row.createCell(2).setCellValue(result.getBmName());
                row.createCell(3).setCellValue(result.getNodes());
                row.createCell(4).setCellValue(result.getCores());
                row.createCell(5).setCellValue(result.getNodeName());
                row.createCell(6).setCellValue(result.getResult());
                row.createCell(7).setCellValue(result.getCpu());
                row.createCell(8).setCellValue(result.getOs());
                row.createCell(9).setCellValue(result.getBiosVer());
                row.createCell(10).setCellValue(result.getCluster());
                row.createCell(11).setCellValue(result.getUser());
                row.createCell(12).setCellValue(result.getPlatform());
                row.createCell(13).setCellValue(result.getCpuGen());
                row.createCell(14).setCellValue(result.getRunType());
                row.createCell(15).setCellValue(result.getSetting());
             }

            sheet.autoSizeColumn(0);
            sheet.autoSizeColumn(1);
            sheet.autoSizeColumn(2);
            sheet.autoSizeColumn(3);
            sheet.autoSizeColumn(4);
            sheet.autoSizeColumn(5);
            sheet.autoSizeColumn(6);
            sheet.autoSizeColumn(7);
            sheet.autoSizeColumn(8);
            sheet.autoSizeColumn(9);
            sheet.autoSizeColumn(10);
            sheet.autoSizeColumn(11);
            sheet.autoSizeColumn(12);
            sheet.autoSizeColumn(13);
            sheet.autoSizeColumn(14);
            sheet.autoSizeColumn(15);

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        }
    }
}
