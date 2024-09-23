package smartsuite.app.common.stateful;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import smartsuite.app.common.shared.ResultMap;
import smartsuite.app.common.stateful.service.StatefulService;


/**
 * @author DongMyeong Won
 * @see
 * @since 2016. 11. 18
 * @FileName StatefulController.java
 * @package smartsuite.app.bp.admin.stateful
 * @변경이력 : [2016. 11. 18] DongMyeong Won 최초작성
 */
@Controller
@RequestMapping(value="**/stateful/")
public class StatefulController {

    @Inject
    StatefulService statefulService;

    /**
     * 캐시 버스트 가져오기
     *
     * @author : DongMyeong Won
     * @Date : 2016. 11. 18
     * @Method Name : findCacheBustValue
     */
    @RequestMapping (value = "findCacheBustValue.do")
    public @ResponseBody String findCacheBustValue() {
        return statefulService.findCacheBustValue();
    }
    
    /**
     * 캐시 버스트 업데이트
     *
     * @author : DongMyeong Won
     * @Date : 2016. 11. 18
     * @Method Name : updateCacheBust
     */
    @RequestMapping (value = "updateCacheBust.do")
    public @ResponseBody String updateCacheBust() {
        statefulService.updateCacheBust();
        return statefulService.findCacheBustValue();
    }
    
    
    /**
     * 개인화 정보 가져오기
     *
     * @author : shkim
     * @Date : 2017. 08. 02
     * @Method Name : findUserPersonalizationClientData
     */
    @RequestMapping (value = "findUserPersonalizationClientData.do")
    public @ResponseBody Map<String, Map<String,Object>> findUserPersonalizationClientData() {
    	return statefulService.findUserPersonalizationClientDataProcess();
    }

    /**
     * 개인화 정보 저장하기
     *
     * @author : shkim
     * @Date : 2017. 08. 02
     * @Method Name : saveUserPersonalizationClientData
     */
    @RequestMapping (value = "saveUserPersonalizationClientData.do")
    @ResponseStatus(value = HttpStatus.OK)
    public @ResponseBody ResultMap saveUserPersonalizationClientData(@RequestBody List<Map<String,Object>> states) {
    	return statefulService.saveUserPersonalizationClientData(states);
    }
    
    /**
     * 개인화 정보 삭제하기
     *
     * @author : shkim
     * @Date : 2017. 08. 02
     * @Method Name : allDeleteUserPersonalizationClientData
     */
    @RequestMapping (value = "allDeleteUserPersonalizationClientData.do")
    @ResponseStatus(value = HttpStatus.OK)
    public @ResponseBody ResultMap allDeleteUserPersonalizationClientData() {
    	return statefulService.allDeleteUserPersonalizationClientData();
    }
}