package com.hzh.event.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hzh.common.enums.ResultEnum;
import com.hzh.common.pojo.ChinaCity;
import com.hzh.common.pojo.EventInfo;
import com.hzh.common.pojo.vo.ResultVO;
import com.hzh.event.service.ChinaCityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;


/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Hzh
 * @since 2022-07-05
 */

@Slf4j
@RestController
@RequestMapping("/common")
public class ChinaCityController {

    @Resource
    private ChinaCityService chinaCityService;


    @PostMapping("/chinacity/crud")
    public ResultVO chinaCityCRUD(@RequestBody HashMap map) {

        HashMap<Object, Object> result = new HashMap<>();

        String event = map.get("event").toString();
        long id = null == map.get("id") ? -1 : Long.parseLong(map.get("id").toString());
        int pid = null == map.get("pid") ? -1 : Integer.parseInt(map.get("pid").toString());
        int type = null == map.get("type") ? -1 : Integer.parseInt(map.get("type").toString());
        String cityname = null == map.get("cityname") ? "" : map.get("cityname").toString();
        String state = null == map.get("state") ? "" : map.get("state").toString();


        switch (event) {
            case "1":
                //分页查询
                int current = null == map.get("current") ? 1 : Integer.parseInt(map.get("current").toString());
                int size = null == map.get("size") ? 10 : Integer.parseInt(map.get("size").toString());
                Page<ChinaCity> page= new Page<>(current, size);
                IPage<ChinaCity> chinaCityIPage = chinaCityService.selectPage(page);
                return ResultVO.ok(chinaCityIPage);

            case "2":
                //新增
                ChinaCity chinaCityInsert = new ChinaCity();
                //chinaCityInsert.setId(id);
                chinaCityInsert.setPid(pid);
                chinaCityInsert.setCityname(cityname);
                chinaCityInsert.setType(type);
                chinaCityInsert.setState(state);


                int insert = chinaCityService.insert(chinaCityInsert);

                if (insert > 0) {
                    result.put("msg", "新增" + insert + "条数据成功");
                    result.put("state", "1");
                    return ResultVO.ok(result);
                } else {
                    result.put("msg", "新增" + insert + "条数据失败");
                    result.put("state", "0");
                    return ResultVO.ok(result);
                }

            case "3":
                //修改
                ChinaCity chinaCity = chinaCityService.selectById(id);
                if (chinaCity != null && id != -1) {
                    ChinaCity chinaCityUpdate = new ChinaCity();
                    chinaCityUpdate.setId(id);
                    chinaCityUpdate.setPid(pid);
                    chinaCityUpdate.setCityname(cityname);
                    chinaCityUpdate.setType(type);

                    boolean update = chinaCityService.updateById(chinaCityUpdate);

                    if (update) {
                        result.put("msg", "更新数据成功");
                        result.put("state", "1");
                        return ResultVO.ok(result);
                    } else {
                        log.info("更新失败");
                        result.put("msg", "更新数据失败");
                        result.put("state", "0");
                        return ResultVO.ok(result);
                    }
                } else {
                    log.error("根据该id未查询到数据");
                    return ResultVO.status(ResultEnum.VALIDATE_ERROR);
                }

            case "4":
                //逻辑删除
                ChinaCity chinaCity1 = chinaCityService.selectById(id);
                if (id != -1 && chinaCity1 != null) {
                    int delete = chinaCityService.updateById(id, state);
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
                List<ChinaCity> eventInfos = chinaCityService.selectList(null);
                return ResultVO.ok(eventInfos);
        }
    }


}

