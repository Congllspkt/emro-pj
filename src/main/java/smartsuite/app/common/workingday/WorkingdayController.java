package smartsuite.app.common.workingday;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import smartsuite.app.common.shared.ResultMap;
import smartsuite.app.common.workingday.service.WorkingdayService;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;

/**
 * 근무일 관련 controller
 */
@Controller
@RequestMapping(value = "**/workingday/")
public class WorkingdayController {

    @Inject
    WorkingdayService workingdayService;

    /**
     * 근무일 목록 조회
     *
     * @param
     * @return the list
     */
    @RequestMapping(value = "findListWorkingdayAtFirst.do")
    public @ResponseBody List<Map<String, Object>> findListWorkingdayAtFirst(@RequestBody Map<String, Object> param) {
        return workingdayService.findListWorkingdayAtFirst(param);
    }

    /**
     * 근무일 목록 초기화
     *
     * @param
     * @return the list
     */
    @RequestMapping(value = "resetListWorkingday.do")
    public @ResponseBody List<Map<String, Object>> resetListWorkingday(@RequestBody Map<String, Object> param) {
        return workingdayService.resetListWorkingday(param);
    }

    /**
     * 근무일 목록 저장
     *
     * @param
     * @return resultmap
     */
    @RequestMapping(value = "saveListWorkingday.do")
    public @ResponseBody ResultMap saveListWorkingday(@RequestBody Map<String, Object> param) {
        return workingdayService.saveListWorkingday(param);
    }

}
