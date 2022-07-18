package com.hzh.feign.clients;

import com.hzh.common.pojo.vo.ResultVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

/**
 * @author Hou Zhonghu
 * @since 2022/7/6 17:27
 */
@FeignClient(value = "eventService",fallback = FuseTreatment.class)
public interface EventClient {

    @RequestMapping(value = "/hzh-event/event/eventInfo/crud",method = RequestMethod.POST)
    public ResultVO findById(@RequestBody Map map);


    @RequestMapping(value = "/hzh-event/common/chinacity/crud",method = RequestMethod.POST)
    public ResultVO chinaCityCRUD(@RequestBody Map map);


}
