package com.hzh.user.easyexcel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.hzh.common.pojo.HzhUser;

/**
 * @author Hou Zhonghu
 * @since 2022/9/1 9:24
 */
public class EasyExcelListenner  extends AnalysisEventListener<HzhUser> {
    @Override
    public void invoke(HzhUser hzhUser, AnalysisContext analysisContext) {
        System.out.println(analysisContext);
        System.out.println(hzhUser);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        System.out.println("完成");
    }
}
