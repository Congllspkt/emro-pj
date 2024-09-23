package smartsuite.security.authentication;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import smartsuite.security.userdetails.UserDetailsProxy;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

public class LogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler{
    
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        this.setDefaultTargetUrl("/invalidSession.do");
        
        if(authentication != null){
            Object principal = authentication.getPrincipal();
            if(principal instanceof UserDetails){
                UserDetailsProxy proxy = (UserDetailsProxy)principal;
                Map userInfo = proxy.getUserInfo();

                String locale = request.getParameter("locale");
                if("BUYER".equals(userInfo.get("usr_typ_ccd"))) {
                    if("ko_KR".equals(locale)){
                        this.setDefaultTargetUrl("/logoutSuccess.do");
                    }else{
                        this.setDefaultTargetUrl("/logoutSuccessEn.do");
                    }
                } else {
                    if("ko_KR".equals(locale)){
                        this.setDefaultTargetUrl("/spLogoutSuccess.do");
                    }else{
                       this.setDefaultTargetUrl("/spLogoutSuccessEn.do");
                    }
                }
            }
        }
        
       super.onLogoutSuccess(request, response, authentication);
    }

}
