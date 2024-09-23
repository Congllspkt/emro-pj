package smartsuite.app.common.excel.data;

import java.util.List;
import java.util.Map;

public class SimpleExcelSheet {
	
	public String getAuthKey() {
		return authKey;
	}

	public void setAuthKey(String authKey) {
		this.authKey = authKey;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
	
	public List<SimpleExcelRowHeader> getHeaders() {
		return headers;
	}

	public void setHeaders(List<SimpleExcelRowHeader> headers) {
		this.headers = headers;
	}

	public List<Map<String, Object>> getRows() {
		return rows;
	}

	public void setRows(List<Map<String, Object>> rows) {
		this.rows = rows;
	}
	

	public String getRowIdKey() {
		return rowIdKey;
	}

	public void setRowIdKey(String rowIdKey) {
		this.rowIdKey = rowIdKey;
	}
	
	

	public Map<Integer, Map<String, Boolean>> getErrorCells() {
		return errorCells;
	}

	public void setErrorCells(Map<Integer, Map<String, Boolean>> errorCells) {
		this.errorCells = errorCells;
	}



	private String authKey;

	private String id;
	
	private String name;
	
	private String comment;
	
	private List<SimpleExcelRowHeader> headers;
	
	private List<Map<String,Object>> rows;
	
	private Map<Integer,Map<String,Boolean>> errorCells; 
	
	//입력된 정보에 업데이트 처리를 할경우 row를 식별하기위해 필요하다
	//기준이되는 키
	private String rowIdKey;

	@Override
	public String toString() {
		return "{" +
				"id='" + id + '\'' +
				", name='" + name + '\'' +
				", comment='" + comment + '\'' +
				", rowIdKey='" + rowIdKey + '\'' +", " +
				"  headers=" + headers +
				", rows=" + rows +
				'}';
	}
}
