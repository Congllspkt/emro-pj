package smartsuite.upload;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import smartsuite.upload.entity.FileGroup;
import smartsuite.upload.entity.FileItem;
import smartsuite.upload.entity.FileList;
import smartsuite.upload.util.AthfServiceUtil;
import smartsuite5.attachment.core.dat.StoredAthf;
import smartsuite5.attachment.core.dat.StoredAthfDescriptor;
import smartsuite5.attachment.core.mgr.conn.StorageConnMgr;
import smartsuite5.attachment.core.util.MimeTypeUtil;
import smartsuite5.attachment.web.service.StorageAthfService;

import javax.inject.Inject;


@Transactional
public class FileServiceManager {

	/** 저장소 데이터 관련 서비스 */
	private final StorageAthfService storageAthfService;
	
	private final StorageConnMgr storageConnMgr;
	
	private AthfServiceUtil athfUtil = new AthfServiceUtil();
	
	@Inject
	MimeTypeUtil mimeTypeUtil;
	
	public FileServiceManager(StorageAthfService storageAthfService, StorageConnMgr storageConnMgr) {
		this.storageAthfService = storageAthfService;
		this.storageConnMgr = storageConnMgr;
	}
	
	protected FileItem findItemWithoutContents(String id) throws Exception {
		StoredAthfDescriptor storedAthfDescriptor = storageAthfService.getAthfDescriptor(id);
		return athfUtil.getFileItemByAthfDescriptor(storedAthfDescriptor, mimeTypeUtil);
	}
	
	protected FileItem findItem(String id) throws Exception {
		StoredAthfDescriptor storedAthfDescriptor = storageAthfService.getAthfDescriptor(id);
		return athfUtil.getFileItemByAthfDescriptor(storedAthfDescriptor, mimeTypeUtil);
	}
	
	protected FileItem findDownloadItem(String id, boolean isEncrypt) throws Exception {
		Map<String, Object> extarParam = isEncrypt ? athfUtil.newEncExtraMap() : athfUtil.newExtraMap();
		StoredAthf storedAthf = storageAthfService.getAthf(
				storageConnMgr.getDefaultStorageId(), 
				id, 
				extarParam);
		return athfUtil.getFileItemByAthf(storedAthf, mimeTypeUtil);
	}
	
	/** FileGroup : 해당 그룹코드에 해당하는 모든 첨부파일 목록 조회 */
	protected FileGroup findGroup(String groupId) throws Exception  {
		List<StoredAthfDescriptor> attachments = storageAthfService.getAthfDescriptorListInPath(groupId);
		FileList fileList = new FileList();
		for (StoredAthfDescriptor storedAthfDescriptor : attachments) {
			fileList.add(athfUtil.getFileItemByAthfDescriptor(storedAthfDescriptor, mimeTypeUtil));
		}
		return new FileGroup(groupId, fileList);
	}

	protected FileList findList(String groupId) throws Exception {
		List<StoredAthfDescriptor> attachments = storageAthfService.getAthfDescriptorListInPath(groupId);
		FileList fileList = new FileList();
		for (StoredAthfDescriptor storedAthfDescriptor : attachments) {
			fileList.add(athfUtil.getFileItemByAthfDescriptor(storedAthfDescriptor, mimeTypeUtil));
		}
		return fileList;
	}

	protected FileList findListWithoutContents(String groupId) throws Exception {
		List<StoredAthfDescriptor> attachments = storageAthfService.getAthfDescriptorListInPath(groupId);
		FileList fileList = new FileList();
		for (StoredAthfDescriptor storedAthfDescriptor : attachments) {
			fileList.add(athfUtil.getFileItemByAthfDescriptor(storedAthfDescriptor, mimeTypeUtil));
		}
		return fileList;
	}
	
	protected FileList findDownloadList(String groupId, boolean isEncrypt) throws Exception {
		List<StoredAthfDescriptor> attachments = storageAthfService.getAthfDescriptorListInPath(groupId);
		FileList fileList = new FileList();
		for (StoredAthfDescriptor storedAthfDescriptor : attachments) {
			fileList.add(findDownloadItem(storedAthfDescriptor.getAthfUuid(), isEncrypt));
		}
		return fileList;
	}
	
	protected void create(FileItem fileItem) throws IOException, Exception {
		this.create(null, fileItem);
	}
	
	protected void create(String storageId, FileItem fileItem) throws IOException, Exception {
		StoredAthf athf = athfUtil.getAthfByFileItem(fileItem);
		if(storageId == null) {
			storageAthfService.uploadAthf(athf, new HashMap());
		} else {
			storageAthfService.uploadAthf(storageId, athf, new HashMap());
		}
	}
	
}