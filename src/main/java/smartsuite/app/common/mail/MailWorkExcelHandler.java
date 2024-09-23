package smartsuite.app.common.mail;

import smartsuite.app.common.excel.data.SimpleExcelSheet;

import java.util.List;
import java.util.Map;

public interface MailWorkExcelHandler {

    public void excelParsingAfterAppUploadService(Map<String, Object> dataResultMap) throws Exception;

}
