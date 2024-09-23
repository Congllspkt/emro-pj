package smartsuite.upload.entity;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.web.multipart.MultipartFile;

public class SimpleMultipartFileItem extends FileItem {
	
	private MultipartFile multipartFile;

	public SimpleMultipartFileItem(MultipartFile multipartFile) {
		this.multipartFile = multipartFile;
	}

	public SimpleMultipartFileItem() {
		super();
	}

	public SimpleMultipartFileItem(String id, String groupId, String name, String extension, long size,
			String reference, byte[] contents, File file) {
		super(id, groupId, name, extension, size, reference, contents, file);
	}

	public SimpleMultipartFileItem(String id, String groupId) {
		super(id, groupId);
	}

	public MultipartFile getMultipartFile() {
		return multipartFile;
	}

	public void setMultipartFile(MultipartFile multipartFile) {
		this.multipartFile = multipartFile;
	}
	
	public InputStream getInputStreamContents() {
		if(inputStreamContents != null) {
			return inputStreamContents;
		}
		
		try {
			InputStream is = toInputStream();
			return is;
		} catch(IOException e) {
			throw new RuntimeException(e);
		}
	}
}