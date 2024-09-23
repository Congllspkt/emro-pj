package smartsuite.upload;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;
import smartsuite.upload.entity.FileItem;
import smartsuite.upload.entity.FileList;
import smartsuite.upload.entity.SimpleMultipartFileItem;
import smartsuite5.attachment.core.mgr.conn.StorageConnMgr;
import smartsuite5.attachment.web.service.StorageAthfService;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.UUID;

@Transactional
public class StdFileService extends FileServiceManager {
	
	static final Logger LOG = LoggerFactory.getLogger(StdFileService.class);
	

	public StdFileService(StorageAthfService storageAthfService, StorageConnMgr storageConnMgr) {
		super(storageAthfService, storageConnMgr);
	}
	
	public OutputStream createFileOutputStream(String filePathname) {
		File tempFile = null;
		tempFile = new File(filePathname);
		try {
			return new FileOutputStream(tempFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void createWithMultipart(SimpleMultipartFileItem fileItem) {
		try {
			fileItem.setMultipartFile(
					new MockMultipartFile(fileItem.getName(), 
							FileUtils.openInputStream(fileItem.getFile())));
			this.create(fileItem);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public FileList findFileListWithoutContents(String groupId) throws Exception {
		return this.findListWithoutContents(groupId);
	}
	
	public FileList findFileListWithContents(String groupId) throws Exception {
		return super.findDownloadList(groupId, true);
	}

	public FileList findFileListWithContents(String groupId, boolean isEncrypt) throws Exception {
		return super.findDownloadList(groupId, isEncrypt);
	}
	
	public FileItem findFileItemWithContents(String fileItemId) throws Exception {
		return this.findDownloadItem(fileItemId, true);
	}
	
	public FileItem findFileItemWithContents(String fileItemId, boolean isEncrypt) throws Exception {
		return this.findDownloadItem(fileItemId, isEncrypt);
	}
	
	/**
	 * 첨부파일 그룹 키 기준으로 신규 그룹키 생성한다.
	 * 물리적인 파일 존재 시 같이 생성한다.
	 *
	 * @param storageId 저장소 ID
	 * @param groupId 복사할 그룹 키
	 * @param copyGroupId 복사될 그룹 키 (신규일 경우 null)
	 * @return 신규 생성된 그룹 키
	 * @throws Exception
	 */
	public String copyFile(String storageId, String groupId, String copyGroupId) {
		if(groupId == null) {
			return null;
		}
		FileList fileList = null;
		try {
			fileList = this.findFileListWithContents(groupId);
		} catch(Exception e) {
			LOG.error(e.getMessage());
		}
		if(fileList == null || fileList.getItems() == null) {
			LOG.info("file not found!");
		}
		String newGroupId = copyGroupId;
		if(newGroupId == null) {
			newGroupId = UUID.randomUUID().toString();
		}
		List<FileItem> fileItems = fileList.getItems();
		for(FileItem fileItem : fileItems) {
			fileItem.setId(UUID.randomUUID().toString());
			fileItem.setGroupId(newGroupId);
			if(storageId == null) {
				try {
					this.create(fileItem);
				} catch(Exception e) {
					LOG.error(e.getMessage());
				}
			} else {
				try {
					this.create(storageId, fileItem);
				} catch(Exception e) {
					LOG.error(e.getMessage());
				}
			}
		}
		return newGroupId;
	}
	
	/**
	 * 첨부파일 그룹 키 기준으로 신규 그룹키 생성한다.
	 * 물리적인 파일 존재 시 같이 생성한다.
	 * default 저장소에 저장한다.
	 *
	 * @param groupId 복사할 그룹 키
	 * @param copyGroupId 복사될 그룹 키 (신규일 경우 null)
	 * @return 신규 생성된 그룹 키
	 * @throws Exception
	 */
	public String copyFile(String groupId, String copyGroupId) {
		return this.copyFile(null, groupId, copyGroupId);
	}
	
	/**
	 * 첨부파일 그룹 키 기준으로 신규 그룹키 생성한다.
	 * 물리적인 파일 존재 시 같이 생성한다.
	 * default 저장소에 저장한다.
	 *
	 * @param groupId 복사할 그룹 키
	 * @return 신규 생성된 그룹 키
	 * @throws Exception
	 */
	public String copyFile(String groupId) {
		return this.copyFile(groupId, null);
	}
}