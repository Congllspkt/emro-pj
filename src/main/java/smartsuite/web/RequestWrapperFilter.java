package smartsuite.web;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

public class RequestWrapperFilter implements Filter{

	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub
		
	}

	@Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		
        RereadableRequestWrapper rereadableRequestWrapper = new RereadableRequestWrapper((HttpServletRequest)request);
		
        //request 요청 데이터 찍는 로직
        /*BufferedReader in = new BufferedReader(new InputStreamReader(rereadableRequestWrapper.getInputStream()));
		StringBuilder builder = new StringBuilder();
		String inputLine; 
		while ((inputLine = in.readLine()) != null) 
		{ 
			builder.append(inputLine);
		}
		in.close();
		String data = builder.toString(); 
		System.out.println("The response from the server is: doFilter 1649 " + data);
		*/
		
		chain.doFilter(rereadableRequestWrapper , response);
	}
	
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

}
