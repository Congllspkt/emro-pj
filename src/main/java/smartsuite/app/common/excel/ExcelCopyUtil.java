package smartsuite.app.common.excel;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Excel을 Copy하는 용도로만 사용할때 사용한다. ( Excel to Excel )
 */
@SuppressWarnings("unused")
@Service
public class ExcelCopyUtil {

    public static void copySheet(XSSFSheet source, XSSFSheet destination){
        copySheet(source, destination, true);
    }

    private static void copySheet(XSSFSheet source, XSSFSheet destination, boolean copyStyle) {
        int maxColumnNum = 0;
        List<CellStyle> styleMap2 = (copyStyle) ? new ArrayList<CellStyle>() : null;
        for (int i = source.getFirstRowNum(); i <= source.getLastRowNum(); i++) {
            XSSFRow srcRow = source.getRow(i);
            XSSFRow destRow = destination.createRow(i);
            if (srcRow != null) {
                copyRow(source, destination, srcRow, destRow, styleMap2);
                if (srcRow.getLastCellNum() > maxColumnNum) {
                    maxColumnNum = srcRow.getLastCellNum();
                }
            }
        }
        for (int i = 0; i <= maxColumnNum; i++) {
            destination.autoSizeColumn(i);
            destination.setColumnWidth(i, source.getColumnWidth(i));
        }
    }


    private static void copyRow(XSSFSheet srcSheet, XSSFSheet destSheet, XSSFRow srcRow, XSSFRow destRow,
                                List<CellStyle> styleMap) {
        Set<CellRangeAddressWrapper> mergedRegions = new TreeSet<CellRangeAddressWrapper>();
        short dh = srcSheet.getDefaultRowHeight();
        if (srcRow.getHeight() != dh) {
            destRow.setHeight(srcRow.getHeight());
        }


        int j = srcRow.getFirstCellNum();
        if (j < 0) {
            j = 0;
        }
        for (; j <= srcRow.getLastCellNum(); j++) {
            XSSFCell oldCell = srcRow.getCell(j); // ancienne cell
            XSSFCell newCell = destRow.getCell(j); // new cell
            if (oldCell != null) {
                if (newCell == null) {
                    newCell = destRow.createCell(j);
                }

                copyCell(oldCell, newCell, styleMap);
                CellRangeAddress mergedRegion = getMergedRegion(srcSheet, srcRow.getRowNum(),
                        (short) oldCell.getColumnIndex());

                if (mergedRegion != null) {
                    CellRangeAddress newMergedRegion = new CellRangeAddress(mergedRegion.getFirstRow(),
                            mergedRegion.getLastRow(), mergedRegion.getFirstColumn(), mergedRegion.getLastColumn());
                    CellRangeAddressWrapper wrapper = new CellRangeAddressWrapper(newMergedRegion);
                    if (isNewMergedRegion(wrapper, mergedRegions)) {
                        mergedRegions.add(wrapper);
                        destSheet.addMergedRegion(wrapper.range);
                    }
                }
            }
        }
    }

    public static class CellRangeAddressWrapper implements Comparable<CellRangeAddressWrapper> {

        public CellRangeAddress range;

        public CellRangeAddressWrapper(CellRangeAddress theRange) {
            this.range = theRange;
        }

        public int compareTo(CellRangeAddressWrapper o) {

            if (range.getFirstColumn() < o.range.getFirstColumn()
                    && range.getFirstRow() < o.range.getFirstRow()) {
                return -1;
            } else if (range.getFirstColumn() == o.range.getFirstColumn()
                    && range.getFirstRow() == o.range.getFirstRow()) {
                return 0;
            } else {
                return 1;
            }

        }

    }

    private static void copyCell(Cell oldCell, Cell newCell, List<CellStyle> styleList) {
        if (styleList != null) {
            if ((oldCell.getSheet().getWorkbook()).equals(newCell.getSheet().getWorkbook())) {
                newCell.setCellStyle(oldCell.getCellStyle());
            } else {
                XSSFCellStyle newCellStyle = (XSSFCellStyle) getSameCellStyle(oldCell, newCell, styleList);
                if (newCellStyle == null) {
                    newCellStyle = (XSSFCellStyle) newCell.getSheet().getWorkbook().createCellStyle();
                    newCellStyle.cloneStyleFrom(oldCell.getCellStyle());
                    styleList.add(newCellStyle);
                }
                newCell.setCellStyle(newCellStyle);
            }
        }

        switch (oldCell.getCellType()) {
            case 1:
                newCell.setCellValue(oldCell.getStringCellValue());
                break;
            case 0:
                newCell.setCellValue(oldCell.getNumericCellValue());
                break;
            case 3:
                newCell.setCellType(CellType.BLANK);
                break;
            case 4:
                newCell.setCellValue(oldCell.getBooleanCellValue());
                break;
            case 5:
                newCell.setCellErrorValue(oldCell.getErrorCellValue());
                break;
            case 2:
                newCell.setCellFormula(oldCell.getCellFormula());
                formulaInfoList.add(new FormulaInfo(oldCell.getSheet().getSheetName(), oldCell.getRowIndex(), oldCell
                        .getColumnIndex(), oldCell.getCellFormula()));
                break;
            default:
                break;
        }
    }
    private static class FormulaInfo {

        private String sheetName;
        private Integer rowIndex;
        private Integer cellIndex;
        private String formula;

        private FormulaInfo(String sheetName, Integer rowIndex, Integer cellIndex, String formula) {
            this.sheetName = sheetName;
            this.rowIndex = rowIndex;
            this.cellIndex = cellIndex;
            this.formula = formula;
        }

        public String getSheetName() {
            return sheetName;
        }

        public void setSheetName(String sheetName) {
            this.sheetName = sheetName;
        }

        public Integer getRowIndex() {
            return rowIndex;
        }

        public void setRowIndex(Integer rowIndex) {
            this.rowIndex = rowIndex;
        }

        public Integer getCellIndex() {
            return cellIndex;
        }

        public void setCellIndex(Integer cellIndex) {
            this.cellIndex = cellIndex;
        }

        public String getFormula() {
            return formula;
        }

        public void setFormula(String formula) {
            this.formula = formula;
        }
    }

    static List<FormulaInfo> formulaInfoList = new ArrayList<FormulaInfo>();

    public static void refreshFormula(XSSFWorkbook workbook) {
        for (FormulaInfo formulaInfo : formulaInfoList) {
            workbook.getSheet(formulaInfo.getSheetName()).getRow(formulaInfo.getRowIndex())
                    .getCell(formulaInfo.getCellIndex()).setCellFormula(formulaInfo.getFormula());
        }
        formulaInfoList.removeAll(formulaInfoList);
    }

    private static CellStyle getSameCellStyle(Cell oldCell, Cell newCell, List<CellStyle> styleList) {
        CellStyle styleToFind = oldCell.getCellStyle();
        CellStyle currentCellStyle = null;
        CellStyle returnCellStyle = null;
        Iterator<CellStyle> iterator = styleList.iterator();

        XSSFFont oldFont = null;
        XSSFFont newFont = null;
        while (iterator.hasNext() && returnCellStyle == null) {
            currentCellStyle = iterator.next();

            if(checkCurrentCellStyle(currentCellStyle,styleToFind)) continue;

            oldFont = (XSSFFont) oldCell.getSheet().getWorkbook().getFontAt(oldCell.getCellStyle().getFontIndex());
            newFont = (XSSFFont) newCell.getSheet().getWorkbook().getFontAt(currentCellStyle.getFontIndex());

            if(checkFontStyle(oldFont,newFont,oldCell,currentCellStyle)) continue;
            returnCellStyle = currentCellStyle;
        }
        return returnCellStyle;
    }

    private static boolean checkFontStyle(XSSFFont oldFont, XSSFFont newFont, Cell oldCell, CellStyle currentCellStyle) {
        if (newFont.getBold() == oldFont.getBold()) {
            return true;
        }
        if (newFont.getColor() == oldFont.getColor()) {
            return true;
        }
        if (newFont.getFontHeight() == oldFont.getFontHeight()) {
            return true;
        }
        if ((newFont.getFontName()).equals(oldFont.getFontName())) {
            return true;
        }
        if (newFont.getItalic() == oldFont.getItalic()) {
            return true;
        }
        if (newFont.getStrikeout() == oldFont.getStrikeout()) {
            return true;
        }
        if (newFont.getTypeOffset() == oldFont.getTypeOffset()) {
            return true;
        }
        if (newFont.getUnderline() == oldFont.getUnderline()) {
            return true;
        }
        if (newFont.getCharSet() == oldFont.getCharSet()) {
            return true;
        }
        if (oldCell.getCellStyle().getDataFormatString().equals(currentCellStyle.getDataFormatString())) {
            return true;
        }

        return false;
    }

    private static boolean checkCurrentCellStyle(CellStyle currentCellStyle, CellStyle styleToFind) {
        if (currentCellStyle.getAlignment() != styleToFind.getAlignment()) {
            return true;
        }
        if (currentCellStyle.getHidden() != styleToFind.getHidden()) {
            return true;
        }
        if (currentCellStyle.getLocked() != styleToFind.getLocked()) {
            return true;
        }
        if (currentCellStyle.getWrapText() != styleToFind.getWrapText()) {
            return true;
        }
        if (currentCellStyle.getBorderBottom() != styleToFind.getBorderBottom()) {
            return true;
        }
        if (currentCellStyle.getBorderLeft() != styleToFind.getBorderLeft()) {
            return true;
        }
        if (currentCellStyle.getBorderRight() != styleToFind.getBorderRight()) {
            return true;
        }
        if (currentCellStyle.getBorderTop() != styleToFind.getBorderTop()) {
            return true;
        }
        if (currentCellStyle.getBottomBorderColor() != styleToFind.getBottomBorderColor()) {
            return true;
        }
        if (currentCellStyle.getFillBackgroundColor() != styleToFind.getFillBackgroundColor()) {
            return true;
        }
        if (currentCellStyle.getFillForegroundColor() != styleToFind.getFillForegroundColor()) {
            return true;
        }
        if (currentCellStyle.getFillPattern() != styleToFind.getFillPattern()) {
            return true;
        }
        if (currentCellStyle.getIndention() != styleToFind.getIndention()) {
            return true;
        }
        if (currentCellStyle.getLeftBorderColor() != styleToFind.getLeftBorderColor()) {
            return true;
        }
        if (currentCellStyle.getRightBorderColor() != styleToFind.getRightBorderColor()) {
            return true;
        }
        if (currentCellStyle.getRotation() != styleToFind.getRotation()) {
            return true;
        }
        if (currentCellStyle.getTopBorderColor() != styleToFind.getTopBorderColor()) {
            return true;
        }
        if (currentCellStyle.getVerticalAlignment() != styleToFind.getVerticalAlignment()) {
            return true;
        }
        return false;
    }

    public static CellRangeAddress getMergedRegion(XSSFSheet sheet, int rowNum, short cellNum) {
        for (int i = 0; i < sheet.getNumMergedRegions(); i++) {
            CellRangeAddress merged = sheet.getMergedRegion(i);
            if (merged.isInRange(rowNum, cellNum)) {
                return merged;
            }
        }
        return null;
    }
    private static boolean isNewMergedRegion(CellRangeAddressWrapper newMergedRegion,
                                             Set<CellRangeAddressWrapper> mergedRegions) {
        return !mergedRegions.contains(newMergedRegion);
    }
}
