package com.hzh.feign.clients;

import com.hzh.common.enums.ResultEnum;
import com.hzh.common.pojo.vo.ResultVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * fegin远程调度熔断处理类
 *
 * @author Hou Zhonghu
 * @since 2022/7/7 15:09
 */
@Component
public class FuseTreatment implements EventClient,UserClient,OrderClient,TeamClinet{

    private static final Logger log = LoggerFactory.getLogger(FuseTreatment.class);

    @Override
    public ResultVO findById(Map map) {
        log.error("赛事中心调度发生熔断");
        return ResultVO.status(ResultEnum.SERVER_FEING_EVENTCLIENT);
    }




}
