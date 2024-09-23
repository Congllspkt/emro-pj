package smartsuite.app.common.attach;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import smartsuite.upload.StdFileService;
import smartsuite.upload.entity.FileItem;
import smartsuite.upload.entity.FileList;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RequestMapping(value={"**/viewer/**/"})
@Controller
public class AttachViewerController {
	
	static final Logger LOG = LoggerFactory.getLogger(AttachViewerController.class);

	@Inject
	StdFileService stdFileService;

	@RequestMapping(value="findListFileItem.do", method = RequestMethod.POST)
	public @ResponseBody List<FileItem> list(@RequestParam(value="groupId", required =false) String groupId) throws Exception {
		FileList fileList = stdFileService.findFileListWithoutContents(groupId);
		return fileList.getItems();
	}
	
	@RequestMapping(value = "findVideoViewer.do", method = RequestMethod.GET)
	public ResponseEntity<byte[]> findVideoViewer(@RequestParam(value="id", required =false) String id, HttpServletRequest request) {
		FileItem fileItem = null;
		InputStream io = null;
		ResponseEntity<byte[]> responseEntity = null;

		try {
			fileItem = stdFileService.findFileItemWithContents(id);
			HttpHeaders headers = new HttpHeaders();

			responseEntity = new ResponseEntity<byte[]>(fileItem.toByteArray(), headers, HttpStatus.OK);
			io.close();

		} catch(Exception e) {
			LOG.error(e.getMessage());
		} finally {
			try {
				if(io != null) io.close();
			} catch(IOException e) {
				LOG.error(e.getMessage());
			}
		}
	    return responseEntity;
	}

}
