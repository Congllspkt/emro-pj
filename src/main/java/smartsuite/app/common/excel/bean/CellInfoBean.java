package smartsuite.app.common.excel.bean;

import java.util.UUID;

@SuppressWarnings("PMD")
public class CellInfoBean {


    public String excel_cel_uuid = null;

    public String sheet_id = null;

    public String excel_row_uuid = null;

    //FONT BOLD
    public boolean ltr_thk_yn = false;

    //FONT HEIGHT
    public short ltr_hgt = 220;

    //FONT FONT NAME
    public String ltr_font_nm = "맑은 고딕";

    //FONT LTR_ITAL_YN
    public boolean ltr_ital_yn = false;

    //FONT LTR_STRKTH_YN
    public boolean ltr_strkth_yn = false;

    //FONT TYPEOFFSET
    public short offset_use_yn = 0;

    //FONT UNDERLINE
    public byte undln_scop = 0;

    //FONT CHARSET
    public int str_set = 129;

    //FONT COLOR
    public int ltr_clr = -16777216;

    //FONT DATAFORMAT
    public short dat_fmt = 0;

    //CELL_STYLE ALIGNMENT_CODE code값을 가지고 다시 찾아와 비지니스 로직에서 박아주는 형태로 진행되어야 할것으로 보임. (정렬 코드)
    /**
     *     int INT_GENERAL = 1;
     *     int INT_LEFT = 2;
     *     int INT_CENTER = 3;
     *     int INT_RIGHT = 4;
     *     int INT_FILL = 5;
     *     int INT_JUSTIFY = 6;
     *     int INT_CENTER_CONTINUOUS = 7;
     *     int INT_DISTRIBUTED = 8;
     */
    public int cel_custmz_yn = 0;

    //CELL_STYLE HIDDEN ( 숨김 여부 )
    public boolean hide_yn = false;

    //CELL_STYLE LOCKED ( 잠금여부 )
    public boolean lckd_yn = false;

    //CELL_STYLE WRAPTEXT (줄바꿈여부)
    public boolean lnbrk_yn = false;

    //CELL_STYLE BORDER CODE 4방향 동일 code값을 가지고 다시 찾아와 비지니스 로직에서 박아주는 형태로 진행되어야 할것으로 보임.
    /**
     *     short BORDER_NONE = 0;
     *     short BORDER_THIN = 1;
     *     short BORDER_MEDIUM = 2;
     *     short BORDER_DASHED = 3;
     *     short BORDER_HAIR = 7;
     *     short BORDER_THICK = 5;
     *     short BORDER_DOUBLE = 6;
     *     short BORDER_DOTTED = 4;
     *     short BORDER_MEDIUM_DASHED = 8;
     *     short BORDER_DASH_DOT = 9;
     *     short BORDER_MEDIUM_DASH_DOT = 10;
     *     short BORDER_DASH_DOT_DOT = 11;
     *     short BORDER_MEDIUM_DASH_DOT_DOT = 12;
     *     short BORDER_SLANTED_DASH_DOT = 13;
     */
    public short cel_brd_bttm_cd = 1;
    public short cel_brd_lft_cd = 1;
    public short cel_brd_rgt_cd = 1;
    public short cel_brd_top_cd = 1;








    // COLOR 들에 한하여서는 RGB 값을 빼와서.. tiny 값을 기준으로 처리를 할 생각
    //CELL_STYLE BottomBorderColor
    public int cel_brd_bttm_clr = 0;

    //CELL_STYLE FillBackgroundColor
    public int bgd_fill_clr = 0;

    //CELL_STYLE FillBackgroundColor
    public int fgd_fill_clr = 0;

    //CELL_STYLE LeftBorderColor
    public int cel_brd_lft_clr = 0;

    //CELL_STYLE RightBorderColor
    public int cel_brd_rgt_clr = 0;

    //CELL_STYLE TopBorderColor
    public int cel_brd_top_clr = 0;

    //CELL_STYLE Indention
    public short cel_marg = 0;

    //CELL_STYLE Rotation
    public short tov_yn = 0;

    //CELL_STYLE VerticalAlignment code값을 가지고 다시 찾아와 비지니스 로직에서 박아주는 형태로 진행되어야 할것으로 보임.
    /**
     *     int INT_TOP = 1;
     *     int INT_CENTER = 2;
     *     int INT_BOTTOM = 3;
     *     int INT_JUSTIFY = 4;
     *     int INT_DISTRIBUTED = 5;
     */
    public int cel_vert_custmz_typ_cd = 2;

    //EXCEL_CEL_TYP
    /**
     *      STRING: 1
     *      NUMERIC: 0
     *      BLANK: 3
     *      BOOLEAN: 4
     *      ERROR: 5
     *      FORMULA: 2
     *      DEFAULT : 0 -> 해당 코드는 NULL 처리 되어야함.
     */
    public int excel_cel_typ = 0;

    //CELL_STYLE FillPattern
    /**
     *     NO_FILL = 0;
     *     SOLID_FOREGROUND = 1;
     *
     *     FINE_DOTS = 2;
     *     ALT_BARS = 3;
     *     SPARSE_DOTS = 4;
     *     THICK_HORZ_BANDS = 5;
     *     THICK_VERT_BANDS = 6;
     *     THICK_BACKWARD_DIAG = 7;
     *     THICK_FORWARD_DIAG = 8;
     *     BIG_SPOTS = 9;
     *     BRICKS = 10;
     *     THIN_HORZ_BANDS = 11;
     *     THIN_VERT_BANDS = 12;
     *     THIN_BACKWARD_DIAG = 13;
     *     THIN_FORWARD_DIAG = 14;
     *     SQUARES = 15;
     *     DIAMONDS = 16;
     */
    public short fill_patt_typ_cd = 0;


    /**
     *  switch (oldCell.getCellType()) {
     *             case STRING:
     *                 newCell.setCellValue(oldCell.getStringCellValue());
     *                 break;
     *             case NUMERIC:
     *                 newCell.setCellValue(oldCell.getNumericCellValue());
     *                 break;
     *             case BLANK:
     *                 newCell.setCellType(CellType.BLANK);
     *                 break;
     *             case BOOLEAN:
     *                 newCell.setCellValue(oldCell.getBooleanCellValue());
     *                 break;
     *             case ERROR:
     *                 newCell.setCellErrorValue(oldCell.getErrorCellValue());
     *                 break;
     *             case FORMULA:
     *                 newCell.setCellFormula(oldCell.getCellFormula());
     *                 formulaInfoList.add(new FormulaInfo(oldCell.getSheet().getSheetName(), oldCell.getRowIndex(), oldCell
     *                         .getColumnIndex(), oldCell.getCellFormula()));
     *                 break;
     *             default:
     *                 break;
     *         }
     *
     */


    public String str_cel_val ="";

    public double dbl_cel_val = 0;

    public boolean bool_cel_val = false;

    public byte err_cel_val = 0;

    public String fmla_cel_val = null;

    public int cel_idx_no = 0;

    public int col_size = 0;

    public int getCol_width() {
        return col_size;
    }

    public void setCol_width(int col_size) {
        this.col_size = col_size;
    }

    public short getDat_fmt() {
        return dat_fmt;
    }

    public void setDat_fmt(short dat_fmt) {
        this.dat_fmt = dat_fmt;
    }

    // 개발자가 추가적으로 Cell을 생성하기 위한 플레그
    public boolean appendCellFlag = false;

    public String getExcel_cel_uuid() {
        return excel_cel_uuid;
    }

    public void setExcel_cel_uuid(String excel_cel_uuid) {
        this.excel_cel_uuid = excel_cel_uuid;
    }

    public String getSheet_id() {
        return sheet_id;
    }

    public void setSheet_id(String sheet_id) {
        this.sheet_id = sheet_id;
    }

    public String getExcel_row_uuid() {
        return excel_row_uuid;
    }

    public void setExcel_row_uuid(String excel_row_uuid) {
        this.excel_row_uuid = excel_row_uuid;
    }

    public boolean isLtr_thk_yn() {
        return ltr_thk_yn;
    }

    public void setLtr_thk_yn(boolean ltr_thk_yn) {
        this.ltr_thk_yn = ltr_thk_yn;
    }

    public short getLtr_hgt() {
        return ltr_hgt;
    }

    public void setLtr_hgt(short ltr_hgt) {
        this.ltr_hgt = ltr_hgt;
    }

    public String getLtr_font_nm() {
        return ltr_font_nm;
    }

    public void setLtr_font_nm(String ltr_font_nm) {
        this.ltr_font_nm = ltr_font_nm;
    }

    public boolean isLtr_ital_yn() {
        return ltr_ital_yn;
    }

    public void setLtr_ital_yn(boolean ltr_ital_yn) {
        this.ltr_ital_yn = ltr_ital_yn;
    }

    public boolean isLtr_strkth_yn() {
        return ltr_strkth_yn;
    }

    public void setLtr_strkth_yn(boolean ltr_strkth_yn) {
        this.ltr_strkth_yn = ltr_strkth_yn;
    }

    public short getOffset_use_yn() {
        return offset_use_yn;
    }

    public void setOffset_use_yn(short offset_use_yn) {
        this.offset_use_yn = offset_use_yn;
    }

    public byte getUndln_scop() {
        return undln_scop;
    }

    public void setUndln_scop(byte undln_scop) {
        this.undln_scop = undln_scop;
    }

    public int getStr_set() {
        return str_set;
    }

    public void setStr_set(int str_set) {
        this.str_set = str_set;
    }

    public int getLtr_clr() {
        return ltr_clr;
    }

    public void setLtr_clr(int ltr_clr) {
        this.ltr_clr = ltr_clr;
    }



    public boolean isHide_yn() {
        return hide_yn;
    }

    public void setHide_yn(boolean hide_yn) {
        this.hide_yn = hide_yn;
    }

    public boolean isLckd_yn() {
        return lckd_yn;
    }

    public void setLckd_yn(boolean lckd_yn) {
        this.lckd_yn = lckd_yn;
    }

    public boolean isLnbrk_yn() {
        return lnbrk_yn;
    }

    public void setLnbrk_yn(boolean lnbrk_yn) {
        this.lnbrk_yn = lnbrk_yn;
    }


    public int getCel_brd_bttm_clr() {
        return cel_brd_bttm_clr;
    }

    public void setCel_brd_bttm_clr(int cel_brd_bttm_clr) {
        this.cel_brd_bttm_clr = cel_brd_bttm_clr;
    }

    public int getBgd_fill_clr() {
        return bgd_fill_clr;
    }

    public void setBgd_fill_clr(int bgd_fill_clr) {
        this.bgd_fill_clr = bgd_fill_clr;
    }

    public int getFgd_fill_clr() {
        return fgd_fill_clr;
    }

    public void setFgd_fill_clr(int fgd_fill_clr) {
        this.fgd_fill_clr = fgd_fill_clr;
    }

    public int getCel_brd_lft_clr() {
        return cel_brd_lft_clr;
    }

    public void setCel_brd_lft_clr(int cel_brd_lft_clr) {
        this.cel_brd_lft_clr = cel_brd_lft_clr;
    }

    public int getCel_brd_rgt_clr() {
        return cel_brd_rgt_clr;
    }

    public void setCel_brd_rgt_clr(int cel_brd_rgt_clr) {
        this.cel_brd_rgt_clr = cel_brd_rgt_clr;
    }

    public int getCel_brd_top_clr() {
        return cel_brd_top_clr;
    }

    public void setCel_brd_top_clr(int cel_brd_top_clr) {
        this.cel_brd_top_clr = cel_brd_top_clr;
    }

    public short getCel_marg() {
        return cel_marg;
    }

    public void setCel_marg(short cel_marg) {
        this.cel_marg = cel_marg;
    }

    public short getTov_yn() {
        return tov_yn;
    }

    public void setTov_yn(short tov_yn) {
        this.tov_yn = tov_yn;
    }

    public int getCel_custmz_yn() {
        return cel_custmz_yn;
    }

    public void setCel_custmz_yn(int cel_custmz_yn) {
        this.cel_custmz_yn = cel_custmz_yn;
    }

    public short getCel_brd_bttm_cd() {
        return cel_brd_bttm_cd;
    }

    public void setCel_brd_bttm_cd(short cel_brd_bttm_cd) {
        this.cel_brd_bttm_cd = cel_brd_bttm_cd;
    }

    public short getCel_brd_lft_cd() {
        return cel_brd_lft_cd;
    }

    public void setCel_brd_lft_cd(short cel_brd_lft_cd) {
        this.cel_brd_lft_cd = cel_brd_lft_cd;
    }

    public short getCel_brd_rgt_cd() {
        return cel_brd_rgt_cd;
    }

    public void setCel_brd_rgt_cd(short cel_brd_rgt_cd) {
        this.cel_brd_rgt_cd = cel_brd_rgt_cd;
    }

    public short getCel_brd_top_cd() {
        return cel_brd_top_cd;
    }

    public void setCel_brd_top_cd(short cel_brd_top_cd) {
        this.cel_brd_top_cd = cel_brd_top_cd;
    }

    public int getCel_vert_custmz_typ_cd() {
        return cel_vert_custmz_typ_cd;
    }

    public void setCel_vert_custmz_typ_cd(int cel_vert_custmz_typ_cd) {
        this.cel_vert_custmz_typ_cd = cel_vert_custmz_typ_cd;
    }

    public int getExcel_cel_typ() {
        return excel_cel_typ;
    }

    public void setExcel_cel_typ(int excel_cel_typ) {
        this.excel_cel_typ = excel_cel_typ;
    }

    public short getFill_patt_typ_cd() {
        return fill_patt_typ_cd;
    }

    public void setFill_patt_typ_cd(short fill_patt_typ_cd) {
        this.fill_patt_typ_cd = fill_patt_typ_cd;
    }

    public String getStr_cel_val() {
        return str_cel_val;
    }

    public void setStr_cel_val(String str_cel_val) {
        this.str_cel_val = str_cel_val;
    }

    public double getDbl_cel_val() {
        return dbl_cel_val;
    }

    public void setDbl_cel_val(double dbl_cel_val) {
        this.dbl_cel_val = dbl_cel_val;
    }

    public boolean isBool_cel_val() {
        return bool_cel_val;
    }

    public void setBool_cel_val(boolean bool_cel_val) {
        this.bool_cel_val = bool_cel_val;
    }

    public byte getErr_cel_val() {
        return err_cel_val;
    }

    public void setErr_cel_val(byte err_cel_val) {
        this.err_cel_val = err_cel_val;
    }

    public String getFmla_cel_val() {
        return fmla_cel_val;
    }

    public void setFmla_cel_val(String fmla_cel_val) {
        this.fmla_cel_val = fmla_cel_val;
    }

    public int getCel_idx_no() {
        return cel_idx_no;
    }

    public void setCel_idx_no(int cel_idx_no) {
        this.cel_idx_no = cel_idx_no;
    }

    public boolean isAppendCellFlag() {
        return appendCellFlag;
    }

    public void setAppendCellFlag(boolean appendCellFlag) {
        this.appendCellFlag = appendCellFlag;
    }

    /**
     * 개발자가 신규로 동적인 Cell을 생성하기 위하여 기본적인 값을 만든다. 여기서 Cell의 색 및 기타 커스텀 데이터를 집어넣는 방향으로 간다.
     * 기타 값들은 기본값을 셋업 해두었음.
     * @param customCellInfo
     * @return
     */
    public CellInfoBean dumyCreateCell(CellInfoBean customCellInfo){
        CellInfoBean cellInfoBean = new CellInfoBean();

        if(null != customCellInfo ){
            String sheetKey = customCellInfo.getSheet_id();
            String rowKey = customCellInfo.getExcel_row_uuid();
            int cellIndex = customCellInfo.getCel_idx_no();

            cellInfoBean.setExcel_cel_uuid(UUID.randomUUID().toString());
            cellInfoBean.setSheet_id(sheetKey);
            cellInfoBean.setExcel_row_uuid(rowKey);
            cellInfoBean.setCel_idx_no(cellIndex);
        }


        return cellInfoBean;
    }


}
