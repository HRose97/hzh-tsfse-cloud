package com.hzh.event.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hzh.common.enums.ResultEnum;
import com.hzh.common.pojo.GlobalLocation;
import com.hzh.common.pojo.vo.ResultVO;
import com.hzh.event.service.GlobalLocationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
public class GlobalLocationController {

    @Resource
    private GlobalLocationService globalLocationService;


    @PostMapping("/globalLocation/crud")
    public ResultVO globalLocationCURD(@RequestBody HashMap map) {

        HashMap<Object, Object> result = new HashMap<>();

        String event = map.get("event").toString();
        int glId = null == map.get("glId") ? -1 : Integer.parseInt(map.get("glId").toString());
        int glpId = null == map.get("glpId") ? -1 : Integer.parseInt(map.get("glpId").toString());
        int level = null == map.get("level") ? -1 : Integer.parseInt(map.get("level").toString());
        String path = null == map.get("path") ? "" : map.get("path").toString();
        String code = null == map.get("code") ? "" : map.get("code").toString();
        String abbreviation = null == map.get("abbreviation") ? "" : map.get("abbreviation").toString();
        String chineseName = null == map.get("chineseName") ? "" : map.get("chineseName").toString();
        String englishName = null == map.get("englishName") ? "" : map.get("englishName").toString();
        String initials = null == map.get("initials") ? "" : map.get("initials").toString();
        String pinyin = null == map.get("pinyin") ? "" : map.get("pinyin").toString();


        switch (event) {
            case "1":
                //分页查询
                int current = null == map.get("current") ? 1 : Integer.parseInt(map.get("current").toString());
                int size = null == map.get("size") ? 10 : Integer.parseInt(map.get("size").toString());
                Page<GlobalLocation> page= new Page<>(current, size);
                IPage<GlobalLocation> globalLocationIPage = globalLocationService.selectPage(page);
                return ResultVO.ok(globalLocationIPage);

            case "2":
                //新增
                GlobalLocation globalLocationInsert = new GlobalLocation();
                //chinaCityInsert.setId(id);
                globalLocationInsert.setGlpId(glpId);
                globalLocationInsert.setLevel(level);
                globalLocationInsert.setPath(path);
                globalLocationInsert.setCode(code);
                globalLocationInsert.setAbbreviation(abbreviation);
                globalLocationInsert.setChineseName(chineseName);
                globalLocationInsert.setEnglishName(englishName);
                globalLocationInsert.setInitials(initials);
                globalLocationInsert.setPinyin(pinyin);


                int insert = globalLocationService.insert(globalLocationInsert);

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
                GlobalLocation globalLocation = globalLocationService.selectById(glId);
                if (globalLocation != null && glId != -1) {
                    GlobalLocation globalLocationUpdate = new GlobalLocation();
                    globalLocationUpdate.setGlpId(glpId);
                    globalLocationUpdate.setLevel(level);
                    globalLocationUpdate.setPath(path);
                    globalLocationUpdate.setCode(code);
                    globalLocationUpdate.setAbbreviation(abbreviation);
                    globalLocationUpdate.setChineseName(chineseName);
                    globalLocationUpdate.setEnglishName(englishName);
                    globalLocationUpdate.setInitials(initials);
                    globalLocationUpdate.setPinyin(pinyin);

                    boolean update = globalLocationService.updateById(globalLocationUpdate);

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

            default:
                //查询
                List<GlobalLocation> globalLocations = globalLocationService.selectList(null);
                return ResultVO.ok(globalLocations);
        }
    }

}

