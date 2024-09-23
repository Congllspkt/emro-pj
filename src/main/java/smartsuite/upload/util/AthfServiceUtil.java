package smartsuite.upload.util;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;

import smartsuite5.attachment.core.util.MimeTypeUtil;
import smartsuite.upload.entity.FileItem;
import smartsuite.upload.entity.SimpleMultipartFileItem;
import smartsuite5.attachment.core.dat.StoredAthf;
import smartsuite5.attachment.core.dat.StoredAthfDescriptor;

public class AthfServiceUtil {

	protected final String USE_CRYPTO = "@useCryptoUtil";
	
	public Map getExtraMap(Map map, boolean useEncAthfUuid) {
		if(useEncAthfUuid) {
			map.put(USE_CRYPTO, useEncAthfUuid);
		}
		return map;
	}
	
	/** 내부적으로 첨부파일 아이디가 암호화 되어 있지 않은 경우 */
	public Map newExtraMap() {
		return getExtraMap(new HashMap(), false);
	}
	
	/** 내부적으로 첨부파일 아이디가 암호화 되어 있는 경우 */
	public Map newEncExtraMap() {
		return getExtraMap(new HashMap(), true);
	}
	
	public Map newExtraMap(boolean useEncAthfUuid) {
		return getExtraMap(new HashMap(), useEncAthfUuid);
	}
	
	public FileItem getFileItemByAthfDescriptor(StoredAthfDescriptor storedAthfDescriptor, MimeTypeUtil mimeTypeUtil) {
		FileItem fileItem = new FileItem(
				storedAthfDescriptor.getAthfUuid(), 
				storedAthfDescriptor.getAthgUuid());
		fileItem.setName(storedAthfDescriptor.getAthfOrigNm());
		fileItem.setExtension(FilenameUtils.getExtension(storedAthfDescriptor.getAthfOrigNm()));
		fileItem.setMimeType(mimeTypeUtil.mimeTypeForFileExtension(fileItem.getExtension()));
		fileItem.setSize(storedAthfDescriptor.getAthfSize());
		fileItem.setReference(storedAthfDescriptor.getAthfPath());
		return fileItem;
	}
	
	public FileItem getFileItemByAthf(StoredAthf storedAthf, MimeTypeUtil mimeTypeUtil) {
		FileItem fileItem = new FileItem(
				storedAthf.getAthfUuid(), 
				storedAthf.getAthgUuid());
		fileItem.setName(storedAthf.getAthfOrigNm());
		fileItem.setExtension(FilenameUtils.getExtension(storedAthf.getAthfOrigNm()));
		fileItem.setMimeType(mimeTypeUtil.mimeTypeForFileExtension(fileItem.getExtension()));
		fileItem.setSize(storedAthf.getAthfSize());
		fileItem.setReference(storedAthf.getAthfPath());
		fileItem.setInputStreamContents(storedAthf.getAthfOrigDat());
		return fileItem;
	}
	
	public StoredAthf getAthfByFileItem(FileItem fileItem) {
		return StoredAthf.builder()
			.athfUuid(fileItem.getId())
			.athgUuid(fileItem.getGroupId())
			.athfNm(fileItem.getId())
			.athfOrigNm(fileItem.getName())
			.athfSize(fileItem.getSize())
			.athfOrigDat(fileItem.getInputStreamContents())
			.build();
	}
	
	public StoredAthf getAthfByFileItemWithoutContents(FileItem fileItem) {
		return StoredAthf.builder()
			.athfUuid(fileItem.getId())
			.athgUuid(fileItem.getGroupId())
			.athfNm(fileItem.getId())
			.athfOrigNm(fileItem.getName())
			.athfSize(fileItem.getSize())
			.build();
	}
	
	public static SimpleMultipartFileItem newMultipartFileItem(String id, String groupId, String name, File file) {
		return new SimpleMultipartFileItem(
				id, 
				groupId, 
				name, 
				FilenameUtils.getExtension(file.getName()), 
				file.length(), 
				null, 
				null, 
				file);
	}
	
}
