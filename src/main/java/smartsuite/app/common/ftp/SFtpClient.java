package smartsuite.app.common.ftp;

import java.io.File;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileSystemOptions;
import org.apache.commons.vfs2.Selectors;
import org.apache.commons.vfs2.impl.StandardFileSystemManager;
import org.apache.commons.vfs2.provider.sftp.SftpFileSystemConfigBuilder;
import org.springframework.beans.factory.annotation.Value;

public class SFtpClient {

	private static final Log LOG = LogFactory.getLog(SFtpClient.class);
	
	@Value ("#{sftp['sftp.hostname']}")
	String hostname;

	@Value ("#{sftp['sftp.username']}")
	String username;

	@Value ("#{sftp['sftp.password']}")
	String password;

	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String formatURL() {
		return "sftp://" + this.username + ":" + this.password + "@" + this.hostname;
	}

	public FileSystemOptions getOptions() throws FileSystemException {
		// Create SFTP options
		FileSystemOptions options = new FileSystemOptions();

		// SSH Key checking
		SftpFileSystemConfigBuilder.getInstance().setStrictHostKeyChecking(options, "no");

		// Root directory set to user home
		SftpFileSystemConfigBuilder.getInstance().setUserDirIsRoot(options, false);

		// Timeout is count by Milliseconds
		SftpFileSystemConfigBuilder.getInstance().setTimeout(options, 10000);

		return options;
	}

	public boolean upload(String remoteFilePath, String localFilePath) {
		File file = new File(localFilePath);
		if (!file.exists()) {
			return false;
		}

		StandardFileSystemManager manager = new StandardFileSystemManager();
		try {
			manager.init();

			// Create local file object
			FileObject localFile = manager.resolveFile(file.getAbsolutePath());

			// Create remote file object
			FileObject remoteFile = manager.resolveFile(formatURL() + remoteFilePath, getOptions());

			// Copy local file to sftp server
			remoteFile.copyFrom(localFile, Selectors.SELECT_SELF);

		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return false;
		} finally {
			manager.close();
		}
		return true;
	}

	public boolean download(String localFilePath, String remoteFilePath) {

		StandardFileSystemManager manager = new StandardFileSystemManager();

		try {
			manager.init();

			// Append _downlaod_from_sftp to the given file name.
			// String downloadFilePath = localFilePath.substring(0, localFilePath.lastIndexOf(".")) + "_downlaod_from_sftp" +
			// localFilePath.substring(localFilePath.lastIndexOf("."), localFilePath.length());

			// Create local file object. Change location if necessary for new downloadFilePath
			FileObject localFile = manager.resolveFile(localFilePath);

			// Create remote file object
			FileObject remoteFile = manager.resolveFile(formatURL() + remoteFilePath, getOptions());

			// Copy local file to sftp server
			localFile.copyFrom(remoteFile, Selectors.SELECT_SELF);
		} catch (Exception e) {
			return false;
		} finally {
			manager.close();
		}
		return true;
	}

	public boolean move(String remoteSrcFilePath, String remoteDestFilePath) {
		StandardFileSystemManager manager = new StandardFileSystemManager();
		try {
			manager.init();

			// Create remote object
			FileObject remoteFile = manager.resolveFile(formatURL() + remoteSrcFilePath, getOptions());
			FileObject remoteDestFile = manager.resolveFile(formatURL() + remoteDestFilePath, getOptions());

			if (remoteFile.exists()) {
				remoteFile.moveTo(remoteDestFile);
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			return false;
		} finally {
			manager.close();
		}
	}

	public boolean delete(String remoteFilePath) {
		StandardFileSystemManager manager = new StandardFileSystemManager();
		try {
			manager.init();

			// Create remote object
			FileObject remoteFile = manager.resolveFile(formatURL() + remoteFilePath, getOptions());

			if (remoteFile.exists()) {
				remoteFile.delete();
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return false;
		} finally {
			manager.close();
		}
		return true;
	}

	public boolean exist(String remoteFilePath) {
		StandardFileSystemManager manager = new StandardFileSystemManager();
		try {
			manager.init();

			// Create remote object
			FileObject remoteFile = manager.resolveFile(formatURL() + remoteFilePath, getOptions());

			return remoteFile.exists();
		} catch (Exception e) {
			return false;
		} finally {
			manager.close();
		}
	}
}
