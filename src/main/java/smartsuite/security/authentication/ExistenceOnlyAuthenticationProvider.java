package smartsuite.security.authentication;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

public class ExistenceOnlyAuthenticationProvider extends DaoAuthenticationProvider {

	@Override
	protected void additionalAuthenticationChecks(UserDetails userDetails,
			UsernamePasswordAuthenticationToken authentication)
			throws AuthenticationException {
		
		/*
		 여기까지 실행되면 전달된 username 에 해당하는 사용자가 존재 함.
		  
		 userDetails    : UserDetailsService에서 DB 조회 후의 결과값
		 authentication : 사용자가 입력한 유저아이디와 비빌번호를 담은 인증정보
		   => DaoAuthenticationProvider 의 경우 userDetails와 authentication 비교하여 인증 성공여부 판단
		      ExistenceOnlyAuthenticationProvider의 경우 사용자가 비밀번호를 입력하지 않았음으로 비교 구문 PASS
		 
		 필요 시 추가 로직 구현
		 
		 */
	}
}
