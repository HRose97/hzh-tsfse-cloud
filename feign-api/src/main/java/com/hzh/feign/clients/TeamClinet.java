package com.hzh.feign.clients;

import com.hzh.common.pojo.vo.ResultVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

/**
 * 球队远程调用api接口
 *
 * @author Hou Zhonghu
 * @since 2022/7/7 15:10
 */
@FeignClient(value = "teamService",fallback = FuseTreatment.class)
public interface TeamClinet {



    @RequestMapping(value = "/hzh-team/team/coachInfo/crud",method = RequestMethod.POST)
    public ResultVO findById(@RequestBody Map map);



}
