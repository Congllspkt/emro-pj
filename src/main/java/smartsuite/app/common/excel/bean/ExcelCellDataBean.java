package smartsuite.app.common.excel.bean;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;
import org.apache.poi.xssf.usermodel.extensions.XSSFCellBorder;
import org.springframework.beans.BeanUtils;

import java.text.SimpleDateFormat;
import java.util.*;

public class ExcelCellDataBean {
	// value 로만 단일 값에 대해서 처리하기로함
	public static final String STARTEXPRESSIONTOKEN = "${";
	public static final String ENDEXPRESSIONTOKEN = "}";

	// list 처리할때 사용 하기로 함
	public static final String STARTFORMULATOKEN = "$[";
	public static final String ENDFORMULATOKEN = "]";

	public static final int REPLACE_TYPE_VALUE = 2;
	public static final int REPLACE_TYPE_LIST = 1;
	public static final int REPLACE_TYPE_NONE = 0;


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

	public static CellInfoBean readCell(Cell oldCell, int cellIndex, String emailWorkId, String rowKey) {


		XSSFFont oldFont = (XSSFFont) oldCell.getSheet().getWorkbook().getFontAt(oldCell.getCellStyle().getFontIndex());
		CellInfoBean cellInfoBean = new CellInfoBean();
		cellInfoBean.setExcel_cel_uuid(UUID.randomUUID().toString());
		cellInfoBean.setSheet_id(emailWorkId);
		cellInfoBean.setExcel_row_uuid(rowKey);
		cellInfoBean.setLtr_thk_yn(oldFont.getBold());
		cellInfoBean.setLtr_hgt(oldFont.getFontHeight());
		cellInfoBean.setLtr_font_nm(oldFont.getFontName());
		cellInfoBean.setLtr_ital_yn(oldFont.getItalic());
		cellInfoBean.setLtr_strkth_yn(oldFont.getStrikeout());
		cellInfoBean.setOffset_use_yn(oldFont.getTypeOffset());
		cellInfoBean.setUndln_scop(oldFont.getUnderline());
		cellInfoBean.setStr_set(oldFont.getCharSet());
		cellInfoBean.setLtr_clr(getColorRGB(oldFont.getXSSFColor()));
		cellInfoBean.setDat_fmt(oldCell.getCellStyle().getDataFormat());
		cellInfoBean.setCel_custmz_yn(oldCell.getCellStyle().getAlignment());
		cellInfoBean.setHide_yn(oldCell.getCellStyle().getHidden());
		cellInfoBean.setLckd_yn(oldCell.getCellStyle().getLocked());
		cellInfoBean.setLnbrk_yn(oldCell.getCellStyle().getWrapText());
		cellInfoBean.setCel_brd_bttm_cd(oldCell.getCellStyle().getBorderBottom());
		cellInfoBean.setCel_brd_lft_cd(oldCell.getCellStyle().getBorderLeft());
		cellInfoBean.setCel_brd_rgt_cd(oldCell.getCellStyle().getBorderRight());
		cellInfoBean.setCel_brd_top_cd(oldCell.getCellStyle().getBorderTop());
		cellInfoBean.setCel_brd_bttm_clr(getBottomBorderColorRGB((XSSFCellStyle)oldCell.getCellStyle()));
		cellInfoBean.setBgd_fill_clr(getFillBackgroundColorRGB((XSSFCellStyle)oldCell.getCellStyle()));
		cellInfoBean.setFgd_fill_clr(getFillForegroundColorRGB((XSSFCellStyle)oldCell.getCellStyle()));
		cellInfoBean.setFill_patt_typ_cd(oldCell.getCellStyle().getFillPattern());
		cellInfoBean.setCel_marg(oldCell.getCellStyle().getIndention());
		cellInfoBean.setCel_brd_lft_clr(getLeftBorderColorRGB((XSSFCellStyle)oldCell.getCellStyle()));
		cellInfoBean.setCel_brd_rgt_clr(getRightBorderColorRGB((XSSFCellStyle)oldCell.getCellStyle()));
		cellInfoBean.setTov_yn(oldCell.getCellStyle().getRotation());
		cellInfoBean.setCel_brd_top_clr(getTopBorderColorRGB((XSSFCellStyle)oldCell.getCellStyle()));
		cellInfoBean.setCel_vert_custmz_typ_cd(oldCell.getCellStyle().getVerticalAlignment());
		cellInfoBean.setCel_idx_no(cellIndex);
		int cellColumnIndex =  oldCell.getSheet().getColumnWidth(oldCell.getColumnIndex());
		cellInfoBean.setCol_width(cellColumnIndex);


		/**
		 *      STRING: 1
		 *      NUMERIC: 0
		 *      BLANK: 4
		 *      BOOLEAN: 5
		 *      ERROR: 6
		 *      FORMULA: 2
		 *      DEFAULT : 0 -> 해당 코드는 NULL 처리 되어야함.
		 * */

		switch (oldCell.getCellType()) {
			case 1:
				cellInfoBean.setExcel_cel_typ(1);
				cellInfoBean.setStr_cel_val(oldCell.getStringCellValue());
				break;
			case 0:
				if( DateUtil.isCellDateFormatted(oldCell)) {
					Date date = oldCell.getDateCellValue();
					String cellString = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(date);
					cellInfoBean.setExcel_cel_typ(1);
					cellInfoBean.setStr_cel_val(cellString);
				}else{
					cellInfoBean.setExcel_cel_typ(0);
					cellInfoBean.setDbl_cel_val(oldCell.getNumericCellValue());
				}
				break;
			case 3:
				cellInfoBean.setExcel_cel_typ(3);
				break;
			case 4:
				cellInfoBean.setExcel_cel_typ(4);
				cellInfoBean.setBool_cel_val(oldCell.getBooleanCellValue());
				break;
			case 5:
				cellInfoBean.setExcel_cel_typ(5);
				cellInfoBean.setErr_cel_val(oldCell.getErrorCellValue());
				break;
			case 2:
				cellInfoBean.setExcel_cel_typ(2);
				cellInfoBean.setFmla_cel_val(oldCell.getCellFormula());
				break;
			default:
				break;
		}

		return cellInfoBean;
	}
	public static class FormulaInfo {

		public String sheetName;
		public Integer rowIndex;
		public Integer cellIndex;
		public String formula;

		public FormulaInfo(String sheetName, Integer rowIndex, Integer cellIndex, String formula) {
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

	public static XSSFColor getColor(XSSFColor c) {
		if (c == null) {
			return null;
		}else if (c instanceof XSSFColor) {
			XSSFColor xc = c;
			byte[] data = null;
			if (xc.getTint() != 0.0) {
				data = getRgbWithTint(xc);
				byte[] argb = xc.getARGB();
			} else {
				data = xc.getARGB();
			}
			if (data == null) {
				return c;
			}
			int idx = 0;
			int alpha = 255;
			if (data.length == 4) {
				alpha = data[idx++] & 0xFF;
			}
			int r = data[idx++] & 0xFF;
			int g = data[idx++] & 0xFF;
			int b = data[idx++] & 0xFF;

			java.awt.Color color =new java.awt.Color(r, g, b, alpha);
			c.setRGB(data);
			return c;
		} else {
			throw new IllegalStateException();
		}
	}


	public static XSSFColor getFillBackgroundColor(XSSFCellStyle xcs) {
		return getColor(xcs.getFillBackgroundColorColor());

	}

	public static XSSFColor getFillForegroundColor(XSSFCellStyle xcs) {
		return getColor(xcs.getFillForegroundColorColor());

	}

	public static XSSFColor getLeftBorderColor(XSSFCellStyle xcs) {
		return getColor(xcs.getBorderColor(XSSFCellBorder.BorderSide.LEFT));

	}

	public static XSSFColor getRightBorderColor(XSSFCellStyle xcs) {
		return getColor(xcs.getBorderColor(XSSFCellBorder.BorderSide.RIGHT));

	}

	public static XSSFColor getTopBorderColor(XSSFCellStyle xcs) {
		return getColor(xcs.getBorderColor(XSSFCellBorder.BorderSide.TOP));

	}

	public static XSSFColor getBottomBorderColor(XSSFCellStyle xcs) {
		return getColor(xcs.getBorderColor(XSSFCellBorder.BorderSide.BOTTOM));

	}

	public static byte[] getRgbWithTint(XSSFColor c) {
		byte[] rgb = c.getCTColor().getRgb();
		double tint = c.getTint();
		if (rgb != null && tint != 0.0) {
			if(rgb.length == 4) {
				byte[] tmp = new byte[3];
				System.arraycopy(rgb, 1, tmp, 0, 3);
				rgb = tmp;
			}
			for (int i=0; i<rgb.length; i++) {
				int lum = rgb[i] & 0xFF;
				double d = sRGB_to_scRGB(lum / 255.0);
				d = tint > 0 ? d * (1.0 - tint) + tint : d * (1 + tint);
				d = scRGB_to_sRGB(d);
				rgb[i] = (byte)Math.round(d * 255.0);
			}
		}
		return rgb;
	}

	public static double sRGB_to_scRGB(double value) {
		if (value < 0.0) {
			return 0.0;
		}
		if (value <= 0.04045) {
			return value /12.92;
		}
		if (value <= 1.0) {
			return Math.pow(((value + 0.055) / 1.055), 2.4);
		}
		return 1.0;
	}

	public static double scRGB_to_sRGB(double value) {
		if (value < 0.0) {
			return 0.0;
		}
		if (value <= 0.0031308) {
			return value * 12.92;
		}
		if (value < 1.0) {
			return 1.055 * (Math.pow(value, (1.0 / 2.4))) - 0.055;
		}
		return 1.0;
	}



	public static int getColorRGB(XSSFColor c) {
		if (c == null) {
			return 0;
		}else if (c instanceof XSSFColor) {
			XSSFColor xc = c;
			byte[] data = null;
			if (xc.getTint() != 0.0) {
				data = getRgbWithTint(xc);
			} else {
				data = xc.getRGB();
			}
			if (data == null) {
				return 0;
			}
			int idx = 0;
			int alpha = 255;
			if (data.length == 4) {
				alpha = data[idx++] & 0xFF;
			}
			int r = data[idx++] & 0xFF;
			int g = data[idx++] & 0xFF;
			int b = data[idx++] & 0xFF;

			java.awt.Color color =new java.awt.Color(r, g, b, alpha);
			return color.getRGB();
		} else {
			throw new IllegalStateException();
		}
	}


	public static int getFillBackgroundColorRGB(XSSFCellStyle xcs) {
		return getColorRGB(xcs.getFillBackgroundColorColor());

	}

	public static int getFillForegroundColorRGB(XSSFCellStyle xcs) {
		return getColorRGB(xcs.getFillForegroundColorColor());

	}

	public static int getLeftBorderColorRGB(XSSFCellStyle xcs) {
		return getColorRGB(xcs.getBorderColor(XSSFCellBorder.BorderSide.LEFT));

	}

	public static int getRightBorderColorRGB(XSSFCellStyle xcs) {
		return getColorRGB(xcs.getBorderColor(XSSFCellBorder.BorderSide.RIGHT));

	}

	public static int getTopBorderColorRGB(XSSFCellStyle xcs) {
		return getColorRGB(xcs.getBorderColor(XSSFCellBorder.BorderSide.TOP));

	}

	public static int getBottomBorderColorRGB(XSSFCellStyle xcs) {
		return getColorRGB(xcs.getBorderColor(XSSFCellBorder.BorderSide.BOTTOM));

	}


	public static List<CellInfoBean> readRow(XSSFSheet srcSheet, XSSFRow srcRow , String emailWorkId , String rowKey) {
		short dh = srcSheet.getDefaultRowHeight();
		if (srcRow.getHeight() != dh) { //NOPMD
			//
			//destRow.setHeight(srcRow.getHeight());
		}

		List<CellInfoBean> cellInfoBeanList = new ArrayList<CellInfoBean>();


		int j = srcRow.getFirstCellNum();
		if (j < 0) {
			j = 0;
		}
		for (; j <= srcRow.getLastCellNum(); j++) {
			XSSFCell oldCell = srcRow.getCell(j); // ancienne cell
			if (oldCell != null) {
				cellInfoBeanList.add(ExcelCellDataBean.readCell(oldCell,j,emailWorkId,rowKey));
			}
		}

		return cellInfoBeanList;
	}

	public static String cellTypeCheck(CellInfoBean getCellInfo) {
		String getStringValue = "";
		switch (getCellInfo.getExcel_cel_typ()) {
			case 1:
				getStringValue = getCellInfo.getStr_cel_val();
				break;
			case 0:
				Integer getDoubleValue = getCellInfo.getDbl_cel_val() == 0? 0: (int)getCellInfo.getDbl_cel_val();
				getStringValue = Integer.toString(getDoubleValue);
				break;
			default:
				break;
		}
		return getStringValue;
	}


	public static void cellInfoCopy(CellInfoBean getCellInfo, List<CellInfoBean> getCellList, int cellIndex) {
		for(CellInfoBean getCell : getCellList){

			int getCellIndex = getCell.getCel_idx_no();

			//rowno 기준으로 동일 시 판단 한다.
			if(cellIndex == getCellIndex){
				BeanUtils.copyProperties(getCell,getCellInfo);
				break;
			}
		}
	}

	public static void rowInfoCopy(RowInfoBean getRowInfo, List<RowInfoBean> getRowList, int tempRowNo) {
		for(RowInfoBean getRow : getRowList){

			int getRowNo = getRow.getExcel_row_no();

			//rowno 기준으로 동일 시 판단 한다.
			if(tempRowNo == getRowNo){
				BeanUtils.copyProperties(getRow,getRowInfo);
				break;
			}
		}
	}

	public static void sheetInfoCopy(SheetInfoBean getSheetInfo, List<SheetInfoBean> getExcelSheetList, String tempSheetName) {
		for(SheetInfoBean sheetInfo : getExcelSheetList){
			String getSheetInfoSheetName =  sheetInfo.getXls_work_sht_nm() == null? "" :  sheetInfo.getXls_work_sht_nm();

			//SheetName은 독립적이기에 비교하여, 처리 가능. SheetInfo를 여기서 가져옴.
			if(tempSheetName.equals(getSheetInfoSheetName)) {
				BeanUtils.copyProperties(sheetInfo,getSheetInfo);
				break;
			}
		}
	}


}
