package smartsuite.app.common.excel.bean;

import java.util.*;

@SuppressWarnings("PMD")
public class ExcelInfoBean {

    public List<SheetInfoBean> sheetList = new ArrayList<SheetInfoBean>();

    // 이메일 업무 아이디
    public String eml_task_uuid = "";
    
    // 메일 설정 아이디
    public String eml_tmpl_cd = "";

    // 메일 업무 명
    public String eml_task_nm = "";

    // 메일 업무 설명
    public String eml_task_expln = "";

    // 메일 업무 코드
    public String eml_task_typ_ccd = "";

    // 사용 여부
    public String use_yn = "Y";

    public String regr_id = "";

    public Date reg_dttm = null;

    public String mod_id = "";

    public Date mod_dttm = null;

    public String cnfd_yn = "N";

    public String athg_uuid = "";

    public ExcelInfoBean(){};

    public ExcelInfoBean(String eml_task_uuid, String eml_tmpl_cd, String eml_task_nm, String eml_task_expln, String eml_task_typ_ccd, String use_yn, String regr_id, Date reg_dttm, String mod_id, Date mod_dttm, String cnfd_yn, String athg_uuid) {
        this.eml_task_uuid = eml_task_uuid;
        this.eml_tmpl_cd = eml_tmpl_cd;
        this.eml_task_nm = eml_task_nm;
        this.eml_task_expln = eml_task_expln;
        this.eml_task_typ_ccd = eml_task_typ_ccd;
        this.use_yn = use_yn;
        this.regr_id = regr_id;
        this.reg_dttm = reg_dttm;
        this.mod_id = mod_id;
        this.mod_dttm = mod_dttm;
        this.cnfd_yn = cnfd_yn;
        this.athg_uuid = athg_uuid;
    }

    public ExcelInfoBean(Map<String,Object> excelInfoBean){
        String attNo = excelInfoBean.getOrDefault("athg_uuid","")== null? "" : (String) excelInfoBean.getOrDefault("athg_uuid","");
        String emailWorkNm = excelInfoBean.getOrDefault("eml_task_nm","")== null? "" : (String) excelInfoBean.getOrDefault("eml_task_nm","");
        String emailWorkCd = excelInfoBean.getOrDefault("eml_task_typ_ccd","")== null? "" : (String) excelInfoBean.getOrDefault("eml_task_typ_ccd","");
        String mailSetId = excelInfoBean.getOrDefault("eml_tmpl_cd","")== null? "" : (String) excelInfoBean.getOrDefault("eml_tmpl_cd","");
        String useYn  =  excelInfoBean.getOrDefault("use_yn","")== null? "" : (String) excelInfoBean.getOrDefault("use_yn","");
        String emailWorkId = excelInfoBean.getOrDefault("eml_task_uuid","")== null? "" : (String) excelInfoBean.getOrDefault("eml_task_uuid","");

        this.eml_task_typ_ccd = emailWorkCd;
        this.eml_task_uuid = emailWorkId;
        this.eml_tmpl_cd = mailSetId;
        this.eml_task_nm = emailWorkNm;
        this.use_yn = useYn;
        this.athg_uuid = attNo;
    }

    public List<SheetInfoBean> getSheetList() {
        return sheetList;
    }

    public void setSheetList(List<SheetInfoBean> sheetList) {
        this.sheetList = sheetList;
    }

    public String getEml_task_uuid() {
        return eml_task_uuid;
    }

    public void setEml_task_uuid(String eml_task_uuid) {
        this.eml_task_uuid = eml_task_uuid;
    }

    public String getEml_tmpl_cd() {
        return eml_tmpl_cd;
    }

    public void setEml_tmpl_cd(String eml_tmpl_cd) {
        this.eml_tmpl_cd = eml_tmpl_cd;
    }

    public String getEml_task_nm() {
        return eml_task_nm;
    }

    public void setEml_task_nm(String eml_task_nm) {
        this.eml_task_nm = eml_task_nm;
    }

    public String getEml_task_expln() {
        return eml_task_expln;
    }

    public void setEml_task_expln(String eml_task_expln) {
        this.eml_task_expln = eml_task_expln;
    }

    public String getEml_task_typ_ccd() {
        return eml_task_typ_ccd;
    }

    public void setEml_task_typ_ccd(String eml_task_typ_ccd) {
        this.eml_task_typ_ccd = eml_task_typ_ccd;
    }

    public String getUse_yn() {
        return use_yn;
    }

    public void setUse_yn(String use_yn) {
        this.use_yn = use_yn;
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

    public String getMod_id() {
        return mod_id;
    }

    public void setMod_id(String mod_id) {
        this.mod_id = mod_id;
    }

    public Date getMod_dttm() {
        return mod_dttm;
    }

    public void setMod_dttm(Date mod_dttm) {
        this.mod_dttm = mod_dttm;
    }

    public String getCnfd_yn() {
        return cnfd_yn;
    }

    public void setCnfd_yn(String cnfd_yn) {
        this.cnfd_yn = cnfd_yn;
    }

    public String getAthg_uuid() {
        return athg_uuid;
    }

    public void setAthg_uuid(String athg_uuid) {
        this.athg_uuid = athg_uuid;
    }
}
