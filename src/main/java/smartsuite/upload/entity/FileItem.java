package smartsuite.upload.entity;

import org.apache.commons.io.IOUtils;
import org.springframework.core.io.InputStreamResource;

import java.io.*;

public class FileItem {
	
	protected String id;
	
	protected String groupId;
	
	protected String name;
	
	protected String extension;
	
	protected String mimeType;
	
	protected long size = 0;
	
	protected String reference;
	
	protected byte[] contents;
	
	protected InputStream inputStreamContents;
	
	protected File file;
	
	public FileItem() {}
	
	public FileItem(String id, String groupId, String name, String extension, long size, String reference,
			byte[] contents, File file) {
		this.id = id;
		this.groupId = groupId;
		this.name = name;
		this.extension = extension;
		this.size = size;
		this.reference = reference;
		this.contents = contents;
		this.file = file;
	}

	public FileItem(String id, String groupId) {
		this.id = id;
		this.groupId = groupId;
	}
	
	public InputStream toInputStream() throws IOException {
		if(inputStreamContents != null) {
			return inputStreamContents;
		}
		if(contents != null) {
			return new ByteArrayInputStream(contents);
		}
		else if(file != null) {
			if(file.exists()) {
				if(file.isDirectory()) {
					throw new IOException("File '" + file + "' exists but is a directory");
				}
				if(file.canRead() == false) {
					throw new IOException("File '" + file + "' cannot be read");
				}
			}
			else {
				throw new FileNotFoundException("File '" + file + "' does not exist");
			}
			return new FileInputStream(file);
		}
		else {
			throw new IOException("FileItem contents and file field is null");
		}
	}
	
	public byte[] toByteArray() throws IOException {
		if(contents != null) {
			return contents;
		}
		if(inputStreamContents != null) {
			return IOUtils.toByteArray(inputStreamContents);
		}
		else {
			throw new IOException("FileItem contents and inputStreamContents is null");
		}
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getExtension() {
		return extension;
	}

	public void setExtension(String extension) {
		this.extension = extension;
	}
	
	public String getMimeType() {
		return mimeType;
	}
	
	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	public long getSize() {
		if(file != null) {
			return file.length();
		}else if(contents != null) {
			try {
				return new InputStreamResource(new ByteArrayInputStream(contents)).contentLength();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return size;
		}else {
			return size;
		}
	}

	public void setSize(long size) {
		this.size = size;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public byte[] getContents() {
		return contents;
	}

	public void setContents(byte[] contents) {
		this.contents = contents; 
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public InputStream getInputStreamContents() {
		return inputStreamContents;
	}

	public void setInputStreamContents(InputStream inputStreamContents) {
		this.inputStreamContents = inputStreamContents;
	}
	
}