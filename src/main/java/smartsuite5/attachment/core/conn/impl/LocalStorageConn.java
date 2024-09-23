package smartsuite5.attachment.core.conn.impl;

import java.io.File;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.channels.Channels;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.io.FileUtils;

import smartsuite5.attachment.core.annotation.StorageConnDescriptor;
import smartsuite5.attachment.core.conn.CommonStorageConn;
import smartsuite5.attachment.core.dat.StoredAthf;
import smartsuite5.attachment.core.dat.StoredAthfDescriptor;


/**
 * 파일 시스템 기반의 로컬 저장소 연결 객체
 * 
 * @since 5.0.2
 */
@StorageConnDescriptor(name="기본 로컬 저장소", description="로컬에서 직접 접근할 수 있는 저장소 ")
public class LocalStorageConn extends CommonStorageConn {

	@Override
	public StoredAthf doPutAthf(final StoredAthf athf, final Map<String, Object> extraParam) throws Exception {
		// 저장소 장소 생성
		String accessUrl = (accessInfo != null) ? accessInfo.getUrl() : "";
		String path = (locationInfo != null) ? locationInfo.getPath(athf) : Optional.ofNullable(athf.getAthgUuid()).orElse("");
		String dirPath = Paths.get(accessUrl, path).toString();
		FileUtils.forceMkdir(new File(dirPath));

		// 저장 경로 설정 및 저장
		String pathWithFileName = Paths.get(path, athf.getAthfNm()).toString();
		String putFullPath = Paths.get(accessUrl, pathWithFileName).toString();
		File putFile = new File(putFullPath);
		
		System.out.println("putFullPath :: "+putFullPath);
		
		FileUtils.copyInputStreamToFile(athf.getAthfOrigDat(), putFile);
		
		return StoredAthf.builder()
						.athfUuid(athf.getAthfUuid())
						.athgUuid(athf.getAthgUuid())
						.athfNm(athf.getAthfNm())
						.athfPath(pathWithFileName)
						.athfOrigNm(athf.getAthfOrigNm())
						.athfSize(athf.getAthfSize())
						.athfOrigDat(athf.getAthfOrigDat())
						.build();
	}

	@Override
	public InputStream doGetPartialAthf(final String athfPath, 
										final Long start, final Long end, 
										final Map<String, Object> extraParam) throws Exception {
		String accessUrl = (accessInfo != null) ? accessInfo.getUrl() : "";
		String fullPath = Paths.get(accessUrl, athfPath).toString();
		RandomAccessFile targetFile = new RandomAccessFile(new File(fullPath), "r");
		targetFile.seek(start);
		return Channels.newInputStream(targetFile.getChannel());
	}

	@Override
	public InputStream doGetAthf(final String athfPath, final Map<String, Object> extraParam) throws Exception {
		String accessUrl = (accessInfo != null) ? accessInfo.getUrl() : "";
		String fullPath = Paths.get(accessUrl, athfPath).toString();
		return FileUtils.openInputStream(new File(fullPath));
	}

	@Override
	public void doDelAthf(final String athfPath, final Map<String, Object> extraParam) throws Exception {
		String accessUrl = (accessInfo != null) ? accessInfo.getUrl() : "";
		String fullPath = Paths.get(accessUrl, athfPath).toString();
		if(storageFileDelYn) {
			FileUtils.delete(new File(fullPath));
		}
	}

	@Override
	public List<StoredAthfDescriptor> doGetAthfDescriptorListInPath(final String athfPath, final Map<String, Object> extraParam) throws Exception {
		String accessUrl = (accessInfo != null) ? accessInfo.getUrl() : "";
		String fullPath = Paths.get(accessUrl, athfPath).toString();
		File targetDir = new File(fullPath);
		
		if (!targetDir.exists()) {
			FileUtils.forceMkdir(targetDir);
		}
	
		return Stream.of(targetDir.listFiles())
				.map(file -> new StoredAthfDescriptor(
									file.getPath().replace(accessUrl, ""), athfPath, 
									file.getName(), athfPath, file.getName(), file.length())
				).collect(Collectors.toList());
	}

	@Override
	public StoredAthfDescriptor doGetAthfDescriptor(final String athfPath, final Map<String, Object> extraParam) {
		String accessUrl = (accessInfo != null) ? accessInfo.getUrl() : "";
		String fullPath = Paths.get(accessUrl, athfPath).toString();
		File target = new File(fullPath);
		return StoredAthfDescriptor.builder()
				.athfNm(target.getName())
				.athfOrigNm(target.getName())
				.athfSize(target.length())
				.build();
	}
	
}
