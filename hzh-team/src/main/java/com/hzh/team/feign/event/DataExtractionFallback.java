package com.hzh.team.feign.event;

import com.hzh.common.pojo.EventInfo;
import com.hzh.common.pojo.vo.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * DataExtraction熔断类
 *
 * @author Hou Zhonghu
 * @since 2022/7/6 19:18
 *
 *
 */
@Component
public class DataExtractionFallback implements DataExtraction{

    private static final Logger log = LoggerFactory.getLogger(DataExtractionFallback.class);

    private String TIMEOUT  =   "3000";             //重试间隔步长



    @Override
    public ResultVO findById(EventInfo eventInfo) {
        log.error("查询分析赛事中心数据出现熔断,查询赛事中心id为:" + eventInfo.getPhysicalId());
        HashMap<Object,Object> res = new HashMap<>();
        res.put("state","0");
        res.put("msg","查询分析赛事中心数据出现熔断,请稍后重试");
        res.put("result",TIMEOUT);
        return ResultVO.ok(res);
    }
}
