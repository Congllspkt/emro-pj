package smartsuite.app.bp.admin.validator;

import java.util.Map;

import smartsuite.app.common.shared.ResultMap;

public interface Validator {
	
	public ResultMap validate(Map<String, Object> param);
	
}