package smartsuite.security.account.repository;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Service;
import smartsuite.security.account.info.AccountSettings;

import javax.inject.Inject;
import java.util.Date;
import java.util.List;
import java.util.Map;


@Service
public class AccountRepository {

	@Inject
	SqlSession sqlSession;

	static final String NAMESPACE = "account.";

	public AccountSettings load() {
		return sqlSession.selectOne(NAMESPACE + "load");
	}

	public void update(AccountSettings accountSettings) {
		sqlSession.update(NAMESPACE + "update", accountSettings);
	}

	public void create(AccountSettings defaultAccountSettings) {
		sqlSession.insert(NAMESPACE + "create", defaultAccountSettings);
	}

	public void accountDisable(Date disableByLastLoginDate) {
		sqlSession.update(NAMESPACE + "accountDisable", disableByLastLoginDate);
	}

	public List<Map<String,String>> ipAddressLoad() {
		return sqlSession.selectList(NAMESPACE + "ipAddressLoad");
	}

	public void ipAddressSave(Map<String, String> item) {
			sqlSession.insert(NAMESPACE + "ipAddressSave", item);
	}
	public void ipAddressDelete(Map<String, String> item) {
			sqlSession.insert(NAMESPACE + "ipAddressDelete", item);
	}

}
