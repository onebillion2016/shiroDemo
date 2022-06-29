package com.example.shirodemo.common.utils;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.fastjson.JSON;
import org.apache.commons.compress.utils.Lists;
import org.apache.log4j.Logger;

import java.util.List;

public class ExcelListener extends AnalysisEventListener {

    public static final Logger logger= Logger.getLogger(ExcelListener.class.toString());

    private static final int BATCH_COUNT = 5;

    private List<Object> datas = Lists.newArrayList();


    public List<Object> getDatas() {
        return datas;
    }

    public void setDatas(List<Object> datas) {
        this.datas = datas;
    }

    @Override
    public void invoke(Object o, AnalysisContext analysisContext) {
        logger.info("解析到一条数据:{}"+ JSON.toJSONString(o));
        //数据存储到list，
        datas.add(o);
        // 达到BATCH_COUNT了，需要去存储一次数据库，防止数据几万条数据在内存，容易OOM
        if (datas.size() >= BATCH_COUNT) {
            saveData();
            // 存储完成清理 list
            datas.clear();
        }

    }

    private void saveData() {
        logger.info("{"+datas.size()+"}条数据，开始存储数据库！" );
        //这个方法自己实现  能完成保存数据入库即可
        logger.info("存储数据库成功！");
    }


    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        saveData();
        logger.info("所有数据解析完成！");
    }
}
