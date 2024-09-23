package smartsuite.upload.entity;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;

public class FileList {
	
	private static final Logger logger = LoggerFactory.getLogger(FileList.class);

	public FileList() {
		items = new ArrayList<>();
	}
	
	public FileList(List<FileItem> items) {
		this.items = items;
	}
	
	private List<FileItem> items;
	
	public List<FileItem> getItems() {
		return items;
	}

	public FileItem get(String id) {
		for(FileItem fileItem : items) {
			if(id.equals(fileItem.getId())) {
				return fileItem;
			}
		}
		return null;
	}

	public int getCount() {
		return items.size();
	}

	public long getSize() {
		long size = 0;
		for(FileItem fileItem : items) {
			size += fileItem.getSize();
		}
		return size;
	}

	public boolean remove(String id) {
		for(FileItem fileItem : items) {
			if(id.equals(fileItem.getId())) {
				items.remove(fileItem);
				return true;
			}
		}
		return false;
	}

	public void itemsToInputStream(boolean isInMemoryStream, ServletOutputStream os) throws IOException {
		Map<String,Integer> files = Maps.newHashMap();
		ZipArchiveOutputStream zos = null;
		try {
			zos = new ZipArchiveOutputStream(os);
			InputStream is = null;
			for(FileItem fileItem : items) {
				boolean isDBMS = fileItem.getReference().equalsIgnoreCase("dbms");
				String fileName = fileItem.getName();
				if(files.containsKey(fileName)) {
					files.put(fileName, files.get(fileName)+1);
					fileName = FilenameUtils.getBaseName(fileName) + "(" + files.get(fileName) + ")." + FilenameUtils.getExtension(fileName);
				}
				else {
					files.put(fileName, 0);
				}
				ZipArchiveEntry ae = new ZipArchiveEntry(fileName);
				try {
					zos.putArchiveEntry(ae);
					if(isInMemoryStream || isDBMS) {
						is = IOUtils.toBufferedInputStream(new ByteArrayInputStream(fileItem.getContents()));
					}
					else {
						is = FileUtils.openInputStream(fileItem.getFile());
					}
					
					int size;
					byte[] buffer = new byte[2048];
					while ((size = is.read(buffer, 0, buffer.length)) != -1)
					{
						zos.write(buffer, 0, size);
					}
					
				} catch (IOException e) {
					logger.error(e.getMessage(), e);
					throw e;
				} finally {
					if(is != null) {
						is.close();
					}
					if(zos != null) {
						zos.closeArchiveEntry();
					}
				}
			}
		} finally {
			if(zos != null) {
				zos.close();
			}
		}
	}

	public boolean add(FileItem item) {
		return items.add(item);
	}
	
}
