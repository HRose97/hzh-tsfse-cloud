package com.hzh.event.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hzh.common.enums.ResultEnum;
import com.hzh.common.pojo.EventInfo;
import com.hzh.common.pojo.vo.ResultVO;
import com.hzh.common.utils.RedisKeyUtil;
import com.hzh.common.utils.RedisUtils;
import com.hzh.event.service.EventInfoService;
import lombok.extern.slf4j.Slf4j;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

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
@RequestMapping("/event")
public class EventInfoController {

    @Resource
    private EventInfoService eventInfoService;


    @Resource
    private RedisUtils redisUtils;

    @Resource
    private RedisKeyUtil redisKeyUtil;

    @PostMapping("/eventInfo/getAll")
    public ResultVO getAll(@RequestBody HashMap map) {
        int current = null == map.get("current") ? 1 : Integer.parseInt(map.get("current").toString());
        int size = null == map.get("size") ? 10 : Integer.parseInt(map.get("size").toString());
        Page<EventInfo> page = new Page<>(current, size);
        IPage<EventInfo> physicalHeldInfoIPage = eventInfoService.selectPage(page);
        return ResultVO.ok(physicalHeldInfoIPage);
    }

    @PostMapping("/eventInfo/crud")
    public ResultVO EventInfoCURD(@RequestBody HashMap map) throws Exception {

        HashMap<Object, Object> result = new HashMap<>();
        String event = map.get("event").toString();
        int physicalId = null == map.get("physicalId") ? -1 : Integer.parseInt(map.get("physicalId").toString());
        String physicalName = null == map.get("physicalName") ? "" : map.get("physicalName").toString();
        String heldCountry = null == map.get("heldCountry") ? "" : map.get("heldCountry").toString();
        String heldLocation = null == map.get("heldLocation") ? "" : map.get("heldLocation").toString();
        String heldHome = null == map.get("heldHome") ? "" : map.get("heldHome").toString();
        String maximumCapacity = null == map.get("maximumCapacity") ? "" : map.get("maximumCapacity").toString();
        String awayGround = null == map.get("awayGround") ? "" : map.get("awayGround").toString();
        String physicalHeldLogo = null == map.get("physicalHeldLogo") ? "" : map.get("physicalHeldLogo").toString();
        String physicalDesc = null == map.get("physicalDesc") ? "" : map.get("physicalDesc").toString();
        int physicalStatus = null == map.get("physicalStatus") ? 1 : Integer.parseInt(map.get("physicalStatus").toString());
        String matchTime = null == map.get("matchTime") ? "" : map.get("matchTime").toString();
        String heldVenues = null == map.get("heldVenues") ? "" : map.get("heldVenues").toString();

        switch (event) {
            case "1":
                //分页查询
                int current = null == map.get("current") ? 1 : Integer.parseInt(map.get("current").toString());
                int size = null == map.get("size") ? 10 : Integer.parseInt(map.get("size").toString());
                Page<EventInfo> page = new Page<>(current, size);
                IPage<EventInfo> physicalHeldInfoIPage = eventInfoService.selectPage(page);
                return ResultVO.ok(physicalHeldInfoIPage);

            case "2":
                //新增
                EventInfo eventInfoInsert = new EventInfo();
                eventInfoInsert.setPhysicalName(physicalName);
                eventInfoInsert.setHeldCountry(heldCountry);
                eventInfoInsert.setHeldLocation(heldLocation);
                eventInfoInsert.setHeldHome(heldHome);
                eventInfoInsert.setMaximumCapacity(maximumCapacity);
                eventInfoInsert.setAwayGround(awayGround);
                eventInfoInsert.setPhysicalHeldLogo(physicalHeldLogo);
                eventInfoInsert.setPhysicalDesc(physicalDesc);
                eventInfoInsert.setPhysicalStatus(physicalStatus);
                eventInfoInsert.setMatchTime(matchTime);
                eventInfoInsert.setHeldVenues(heldVenues);

                int maxId = 0 == eventInfoService.getMaxId() ? 1 : eventInfoService.getMaxId();
                eventInfoInsert.setPhysicalId(maxId+1);
                String mkEventRedisKey = redisKeyUtil.mkEventRedisKey(eventInfoInsert);
                try {
                    redisUtils.set(mkEventRedisKey, String.valueOf(eventInfoInsert));
                    int insert = eventInfoService.insert(eventInfoInsert);

                    if (insert > 0) {
                        result.put("msg", "新增" + insert + "条数据成功");
                        result.put("state", "1");
                        return ResultVO.ok(result);
                    } else {
                        result.put("msg", "新增" + insert + "条数据失败");
                        result.put("state", "0");
                        return ResultVO.ok(result);
                    }
                }catch (Exception e){
                    throw new RuntimeException("添加赛事缓存异常");
                }

            case "3":
                //修改
                EventInfo eventInfo = eventInfoService.selectById(physicalId);
                if (eventInfo != null && physicalId != -1) {
                    EventInfo eventInfoUpdate = new EventInfo();
                    eventInfoUpdate.setPhysicalId(physicalId);
                    eventInfoUpdate.setPhysicalName(physicalName);
                    eventInfoUpdate.setHeldCountry(heldCountry);
                    eventInfoUpdate.setHeldLocation(heldLocation);
                    eventInfoUpdate.setHeldHome(heldHome);
                    eventInfoUpdate.setMaximumCapacity(maximumCapacity);
                    eventInfoUpdate.setAwayGround(awayGround);
                    eventInfoUpdate.setPhysicalHeldLogo(physicalHeldLogo);
                    eventInfoUpdate.setPhysicalDesc(physicalDesc);
                    eventInfoUpdate.setPhysicalStatus(physicalStatus);
                    eventInfoUpdate.setMatchTime(matchTime);
                    eventInfoUpdate.setHeldVenues(heldVenues);




                    boolean update = eventInfoService.updateById(eventInfoUpdate);

                    if (update) {
                        result.put("msg", "更新" + update + "条数据成功");
                        result.put("state", "1");
                        return ResultVO.ok(result);
                    } else {
                        log.info("更新失败");
                        result.put("msg", "更新" + update + "条数据失败");
                        result.put("state", "0");
                        return ResultVO.ok(result);
                    }
                } else {
                    log.error("根据该id未查询到数据");
                    return ResultVO.status(ResultEnum.VALIDATE_ERROR);
                }

            case "4":
                //逻辑删除
                if (physicalId != -1) {
                    int delete = eventInfoService.updateById(physicalId, physicalStatus);
                    if (delete > 0) {
                        result.put("msg", "删除" + delete + "条数据成功");
                        result.put("state", "1");
                        return ResultVO.ok(result);
                    } else {
                        log.info("删除失败");
                        result.put("msg", "删除" + delete + "条数据失败");
                        result.put("state", "0");
                        return ResultVO.ok(result);
                    }
                } else {
                    log.error("根据该id未查询到数据");
                    return ResultVO.status(ResultEnum.VALIDATE_ERROR);
                }
            default:
                //查询
                List<EventInfo> eventInfos = eventInfoService.selectList(null);
                return ResultVO.ok(eventInfos);
        }
    }

}

