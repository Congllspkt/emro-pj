package smartsuite.security.account.repository;

import org.apache.ibatis.session.SqlSession;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import smartsuite.security.account.info.AccountSettings;
import smartsuite.security.userdetails.User;

import javax.inject.Inject;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;


@Service
public class UserSessionRepository {

	@Inject
	SqlSession sqlSession;

	static final String NAMESPACE = "user-session.";


	public Map findUserSessionInfo(String userId) {
		return sqlSession.selectOne(NAMESPACE + "findUserSessionInfo", userId);
	}

	public User findUserNameAndPassword(String username) {
		return sqlSession.selectOne(NAMESPACE + "findUserNameAndPassword", username);
	}

	public Map<String, Object> findUserAuthenticationInfo(String username) {
		return sqlSession.selectOne(NAMESPACE + "findUserAuthenticationInfo", username);
	}

	public void updateUserAuthenticationFailCountAndAccountLock(Map<String, Object> param) {
		sqlSession.update(NAMESPACE + "updateUserAuthenticationFailCountAndAccountLock", param);
	}

	public void updateUserLoginClientIpAndDate(Map<String, String> param) {
		sqlSession.update(NAMESPACE + "updateUserLoginClientIpAndDate", param);
	}

	public Collection<GrantedAuthority> findListRoleCodeByLoginUserSession(Map<String, Object> userInfo) {
		return sqlSession.selectList(NAMESPACE + "findListRoleCodeByLoginUserSession", userInfo);
	}

	public Map<String, Object> findSupplierUserSessionInfo(String username) {
		return sqlSession.selectOne(NAMESPACE + "findSupplierUserSessionInfo", username);
	}

	public User findUserNameAndPasswordForSupplier(String username) {
		return sqlSession.selectOne(NAMESPACE + "findUserNameAndPasswordForSupplier", username);
	}
}
