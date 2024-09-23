package smartsuite.upload.entity;

import java.util.List;

public class FileGroup {

	private String id;

	private FileList fileList;
	
	public FileGroup(String id, FileList fileList) {
		this.id = id;
		this.fileList = fileList;
	}

	public FileGroup(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public FileList getFileList() {
		return fileList;
	}

	public void setFileList(FileList fileList) {
		this.fileList = fileList;
	}

	public FileItem get(String id) {
		return fileList.get(id);
	}

	public List<FileItem> getItems() {
		return fileList.getItems();
	}

	public int getCount() {
		return fileList.getCount();
	}

	public long getSize() {
		return fileList.getSize();
	}
	
}
