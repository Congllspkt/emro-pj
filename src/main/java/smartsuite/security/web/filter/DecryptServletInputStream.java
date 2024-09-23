package smartsuite.security.web.filter;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import java.io.IOException;
import java.io.InputStream;

public class DecryptServletInputStream extends ServletInputStream {

	InputStream inputStream;

	public DecryptServletInputStream(InputStream in){
		this.inputStream = in;
	}

	@Override
	public int read() throws IOException {
		return this.inputStream.read();
	}
	
	@Override
	public int read(byte[] b) throws IOException {
		return this.inputStream.read(b);
	}

	@Override
	public boolean isFinished() {
		return false;
	}

	@Override
	public boolean isReady() {
		return false;
	}

	@Override
	public void setReadListener(ReadListener readListener) {

	}
}
