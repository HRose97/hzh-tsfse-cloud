package com.hzh.feign.clients;

import com.hzh.common.pojo.vo.ResultVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

/**
 * @author Hou Zhonghu
 * @since 2022/7/7 15:16
 */
@FeignClient(value = "userService",fallback = FuseTreatment.class)
public interface UserClient {



    @RequestMapping(value = "/hzh-user/user/userInfo/crud",method = RequestMethod.POST)
    public ResultVO findById(@RequestBody Map map);




}
