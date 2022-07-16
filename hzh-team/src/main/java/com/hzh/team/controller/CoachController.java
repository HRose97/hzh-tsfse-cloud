package com.hzh.team.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hzh.common.pojo.CoachInfo;
import com.hzh.common.pojo.vo.ResultVO;
import com.hzh.common.utils.DateUtils;
import com.hzh.feign.clients.EventClient;
import com.hzh.team.service.CoachService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;

/**
 * <p>
 * 体育赛事举办信息表 前端控制器
 * </p>
 *
 * @author Hzh
 * @since 2022-07-05
 */
@Slf4j
@RestController
@RequestMapping("/team")
public class CoachController {

    @Resource
    private CoachService coachService;

    @Resource
    private EventClient eventClient;

    @PostMapping("/coachInfo/crud")
    public ResultVO teanInfoCURD(@RequestBody HashMap map) {

        String date = DateUtils.getCurrent(DateUtils.year_of_minute);
        HashMap<Object, Object> result = new HashMap<>();

        String event = map.get("event").toString();
        int coachTeamId = null == map.get("coachTeamId") ? -1 : Integer.parseInt(map.get("coachTeamId").toString());
        int coachId = null == map.get("coachId") ? -1 : Integer.parseInt(map.get("coachId").toString());
        String coachName = null == map.get("coachName") ? "" : map.get("coachName").toString();
        int coachHeight = null == map.get("coachHeight") ? 1 : Integer.parseInt(map.get("coachHeight").toString());
        int coachWonNum = null == map.get("coachWonNum") ? 1 : Integer.parseInt(map.get("coachWonNum").toString());
        String coachTime = date;
        int coachTotal = null == map.get("coachTotal") ? 1 : Integer.parseInt(map.get("coachTotal").toString());
        int coachWeight = null == map.get("coachWeight") ? 1 : Integer.parseInt(map.get("coachWeight").toString());
        int coachCountry = null == map.get("coachCountry") ? 1 : Integer.parseInt(map.get("coachCountry").toString());
        int coachAge = null == map.get("coachAge") ? 1 : Integer.parseInt(map.get("coachAge").toString());
        int coachUniformNumber = null == map.get("coachUniformNumber") ? 1 : Integer.parseInt(map.get("coachUniformNumber").toString());
        int coachLocation = null == map.get("coachLocation") ? 1 : Integer.parseInt(map.get("coachLocation").toString());
        int coachPractitionersAge = null == map.get("coachPractitionersAge") ? 1 : Integer.parseInt(map.get("coachPractitionersAge").toString());

        switch (event) {
            case "1":
                //分页查询
                int current = null == map.get("current") ? 1 : Integer.parseInt(map.get("current").toString());
                int size = null == map.get("size") ? 10 : Integer.parseInt(map.get("size").toString());
                Page<CoachInfo> page = new Page<>(current, size);
                IPage<CoachInfo> coachIPage = coachService.selectPage(page);
                return ResultVO.ok(coachIPage);
            default:
                //查询
                //List<Coach> eventInfos = coachService.selectList(null);
                return ResultVO.ok();
        }
    }


    //查询球队的赛事日程
    @PostMapping("/team/contest")
    public ResultVO contest(@RequestBody HashMap map){
        int id = null == map.get("id") ? -1 : Integer.parseInt(map.get("id").toString());
        ResultVO byId = eventClient.findById(map);
        return byId;
    }

}

