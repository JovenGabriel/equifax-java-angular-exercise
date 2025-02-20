package com.equifax.users.utils;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.InputStream;
import java.util.*;

import org.springframework.web.multipart.MultipartFile;

public class ExcelParserHelper {

    /**
     * Parses the given Excel file and extracts its content into a list of maps,
     * where each map represents a row with key-value pairs corresponding to column headers and cell values.
     *
     * @param file the Excel file to parse, provided as a MultipartFile
     * @return a list of maps, where each map contains the data for a row with headers as keys and cell values as values
     * @throws RuntimeException if an error occurs while reading or processing the Excel file
     */
    public static List<Map<String, String>> parseExcel(MultipartFile file) {
        List<Map<String, String>> dataList = new ArrayList<>();

        try (InputStream inputStream = file.getInputStream();
             Workbook workbook = new XSSFWorkbook(inputStream)) {

            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();

            List<String> headers = new ArrayList<>();
            if (rowIterator.hasNext()) {
                Row headerRow = rowIterator.next();
                for (Cell cell : headerRow) {
                    headers.add(cell.getStringCellValue().trim());
                }
            }

            // Validate that the columns are "name", "rut", and "email" in that order
            if (headers.size() != 3 ||
                    !"name".equalsIgnoreCase(headers.get(0)) ||
                    !"rut".equalsIgnoreCase(headers.get(1)) ||
                    !"email".equalsIgnoreCase(headers.get(2))) {
                throw new IllegalArgumentException("Excel file must contain columns 'name', 'rut', and 'email' in that order.");
            }

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                Map<String, String> rowData = new HashMap<>();

                for (int i = 0; i < headers.size(); i++) {
                    Cell cell = row.getCell(i, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                    rowData.put(headers.get(i), getCellValue(cell));
                }

                dataList.add(rowData);
            }

        } catch (Exception e) {
            throw new RuntimeException("Error al leer el archivo Excel", e);
        }

        return dataList;
    }

    /**
     * Extracts the value of a given Excel cell as a String. The cell value is processed based on its type:
     * - STRING: Trims and returns the string value.
     * - NUMERIC: Converts the numeric value to its long string representation.
     * - BOOLEAN: Converts the boolean value to its string representation.
     * - Any other cell type or null cell returns an empty string.
     *
     * @param cell the Excel cell from which the value is retrieved
     * @return the string representation of the cell's value based on its type, or an empty string for unsupported or null cells
     */
    private static String getCellValue(Cell cell) {
        switch (cell.getCellType()) {
            case STRING: return cell.getStringCellValue().trim();
            case NUMERIC: return String.valueOf((long) cell.getNumericCellValue());
            case BOOLEAN: return String.valueOf(cell.getBooleanCellValue());
            default: return "";
        }
    }
}

