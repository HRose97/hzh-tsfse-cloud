package com.hzh.user.controller;

import com.hzh.common.enums.ResultEnum;
import com.hzh.common.pojo.vo.ResultVO;
import com.hzh.feign.clients.EventClient;
import com.hzh.feign.clients.OrderClient;
import com.hzh.feign.clients.TeamClinet;
import com.hzh.user.service.HzhUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Hou Zhonghu
 * @since 2022/7/12 14:50
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class FeignControllrt {

    @Resource
    private OrderClient  orderClient;

    @Resource
    private EventClient eventClient;

    @Resource
    private TeamClinet teamClinet;


    //远程调用  获取该用户的订单信息
    @PostMapping("/order/getAll")
    public ResultVO getUserOrderAll(@RequestBody Map map1){
        int userId = null ==  map1.get("userId") ? 0 : Integer.parseInt(map1.get("userId").toString());
        if ("0".equals(userId)){
            log.error("userId is null");
            return ResultVO.status(ResultEnum.VALIDATE_ERROR);
        }else {
            HashMap<String,Object> map = new HashMap<>();
            map.put("userId",userId);
            map.put("event","1");
            //远程调用Order服务
            ResultVO byId = orderClient.findById(map);
            System.out.println("远程调用订单中心得到："+ byId);
            if (byId != null){
                return byId;
            }else {
                return ResultVO.status(ResultEnum.SERVER_FEING_ORDERNOSELECT);
            }
        }
    }


    //远程调用   获取赛事中心查看信息
    @PostMapping("/order/getEventAll")
    public ResultVO getEventAll(@RequestBody Map map1){
        int userId = null ==  map1.get("userId") ? 0 : Integer.parseInt(map1.get("userId").toString());
        if ("0".equals(userId)){
            log.error("userId is null");
            return ResultVO.status(ResultEnum.VALIDATE_ERROR);
        }else {
            //远程调用Event服务
            HashMap<String,Object> eventMap = new HashMap<>();
            eventMap.put("event","1");
            ResultVO byId = eventClient.findById(eventMap);
            System.out.println("远程调用赛事中心得到:" + byId);
            if (byId != null){
                return byId;
            }else {
                return ResultVO.status(ResultEnum.SERVER_FEING_EVENTCLIENT);
            }
        }
    }


    //远程调用   获取球队中心查看信息
    @PostMapping("/order/getTeamAll")
    public ResultVO getTeamAll(@RequestBody Map map){
        int userId = null ==  map.get("userId") ? 0 : Integer.parseInt(map.get("userId").toString());
        if ("0".equals(userId)){
            log.error("userId is null");
            return ResultVO.status(ResultEnum.VALIDATE_ERROR);
        }else {
            //远程调用Team服务
            HashMap<String,Object> eventMap = new HashMap<>();
            eventMap.put("event","1");
            ResultVO byId = teamClinet.findById(eventMap);
            System.out.println("远程调用球队中心得到:" + byId);
            if (byId != null){
                return byId;
            }else {
                return ResultVO.status(ResultEnum.SERVER_FEING_TEAMCLIENT);
            }
        }
    }




}
