package com.hzh.team.feign.event;

import com.hzh.common.pojo.EventInfo;
import com.hzh.common.pojo.vo.ResultVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

/**
 *
 * 调用分析后数据提取接口
 * @author Hou Zhonghu
 * @since 2022/7/6 19:17
 *
 */
@FeignClient(value = "eventservice" , fallback = DataExtractionFallback.class)
public interface DataExtraction {


    @PostMapping("/hzh-event/event/eventInfo/crud")
    public ResultVO findById(@RequestBody EventInfo  eventInfo);


}
