package smartsuite.app.common.excel.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SuppressWarnings("PMD")
public class RowInfoBean {

    public List<CellInfoBean> cellList = new ArrayList<CellInfoBean>();

    public String excel_row_uuid = "";

    public String eml_task_uuid = "";

    public String excel_sht_uuid = "";

    public int excel_row_no = 0;

    public String usrcert_id = "";

    public String usr_auth_uuid = "";

    public String regr_id = "";

    public Date reg_dttm = null;

    public String getUsr_auth_uuid() {
        return usr_auth_uuid;
    }

    public void setUsr_auth_uuid(String usr_auth_uuid) {
        this.usr_auth_uuid = usr_auth_uuid;
    }

    public List<CellInfoBean> getCellList() {
        return cellList;
    }

    public void setCellList(List<CellInfoBean> cellList) {
        this.cellList = cellList;
    }

    public String getExcel_row_uuid() {
        return excel_row_uuid;
    }

    public void setExcel_row_uuid(String excel_row_uuid) {
        this.excel_row_uuid = excel_row_uuid;
    }

    public String getEml_task_uuid() {
        return eml_task_uuid;
    }

    public void setEml_task_uuid(String eml_task_uuid) {
        this.eml_task_uuid = eml_task_uuid;
    }

    public String getExcel_sht_uuid() {
        return excel_sht_uuid;
    }

    public void setExcel_sht_uuid(String excel_sht_uuid) {
        this.excel_sht_uuid = excel_sht_uuid;
    }

    public int getExcel_row_no() {
        return excel_row_no;
    }

    public void setExcel_row_no(int excel_row_no) {
        this.excel_row_no = excel_row_no;
    }

    public String getUsrcert_id() {
        return usrcert_id;
    }

    public void setUsrcert_id(String usrcert_id) {
        this.usrcert_id = usrcert_id;
    }

    public String getRegr_id() {
        return regr_id;
    }

    public void setRegr_id(String regr_id) {
        this.regr_id = regr_id;
    }

    public Date getReg_dttm() {
        return reg_dttm;
    }

    public void setReg_dttm(Date reg_dttm) {
        this.reg_dttm = reg_dttm;
    }
}
