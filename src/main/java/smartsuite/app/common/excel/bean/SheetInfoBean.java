package smartsuite.app.common.excel.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SuppressWarnings("PMD")
public class SheetInfoBean {

    public List<RowInfoBean> rowList = new ArrayList<RowInfoBean>();

    public String eml_task_uuid = "";

    public String excel_sht_uuid = "";

    public String excel_sht_nm = "";

    public String regr_id = "";

    public Date reg_dttm = null;


    public List<RowInfoBean> getRowList() {
        return rowList;
    }

    public void setRowList(List<RowInfoBean> rowList) {
        this.rowList = rowList;
    }

    public String getEmail_work_id() {
        return eml_task_uuid;
    }

    public void setEmail_work_id(String eml_task_uuid) {
        this.eml_task_uuid = eml_task_uuid;
    }

    public String getExcel_sht_uuid() {
        return excel_sht_uuid;
    }

    public void setExcel_sht_uuid(String excel_sht_uuid) {
        this.excel_sht_uuid = excel_sht_uuid;
    }

    public String getXls_work_sht_nm() {
        return excel_sht_nm;
    }

    public void setXls_work_sht_nm(String excel_sht_nm) {
        this.excel_sht_nm = excel_sht_nm;
    }

    public String getReg_id() {
        return regr_id;
    }

    public void setReg_id(String regr_id) {
        this.regr_id = regr_id;
    }

    public Date getReg_dt() {
        return reg_dttm;
    }

    public void setReg_dt(Date reg_dttm) {
        this.reg_dttm = reg_dttm;
    }
}
