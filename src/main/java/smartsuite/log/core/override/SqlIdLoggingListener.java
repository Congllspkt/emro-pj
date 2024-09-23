package smartsuite.log.core.override;

import smartsuite.log.core.SLF4JQueryLoggingListener;
import smartsuite.security.ThreadLocalObjectHolder;

public class SqlIdLoggingListener extends SLF4JQueryLoggingListener {
	String logTemplate = "Tenancy:{}/{} Datasource:{} Time:{} [QET:{}] Num:{} Batch:{}/{} SqlId:{} Query:{}";
	
	Boolean includeSqlId = true;
	
	public void setIncludeSqlId(Boolean includeSqlId){
		this.includeSqlId = includeSqlId;
	}
	
	SqlIdLoggingListener(){
		super();
		if(includeSqlId)
			super.setLogTemplate(this.logTemplate);
	}
	
	@Override
	protected void writeLog(Object[] argArray) {
		if(includeSqlId){
			Object[] argAddedArray = new Object[argArray.length+1];  
			System.arraycopy(argArray, 0, argAddedArray, 0, argArray.length -1);
			argAddedArray[argArray.length] = argArray[argArray.length -1];
			argAddedArray[argArray.length - 1] = ThreadLocalObjectHolder.get();
			super.writeLog(argAddedArray);
		}
		else{
			super.writeLog(argArray);
		}
	}
}
