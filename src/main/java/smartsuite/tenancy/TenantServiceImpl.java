package smartsuite.tenancy;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import smartsuite.spring.tenancy.web.TenantService;

@Transactional
@Service
public class TenantServiceImpl implements TenantService {

	@Inject
	private SqlSession sqlSession;
	
	/* NAMESPACE*/
	private static final String NAMESPACE = "infra-tenancy.";
	
	@Override
	public Map find(Map param) {
		return sqlSession.selectOne(NAMESPACE+"getTenant", param);
	}

	@Override
	public List<Map> findAll() {
		return null;
	}
	
	@Override
	public void create(Map param) {
		sqlSession.selectOne(NAMESPACE+"createTenant", param);
	}

	@Override
	public void delete(Map param) {
		sqlSession.selectOne(NAMESPACE+"deleteTenant", param);
	}

	@Override
	public void update(Map param) {
		sqlSession.selectOne(NAMESPACE+"updateTenant", param);
	}

}
