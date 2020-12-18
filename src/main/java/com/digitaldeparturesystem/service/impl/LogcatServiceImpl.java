package com.digitaldeparturesystem.service.impl;

import com.digitaldeparturesystem.pojo.Clerk;
import com.digitaldeparturesystem.pojo.Log;
import com.digitaldeparturesystem.pojo.LogSearchCondition;
import com.digitaldeparturesystem.pojo.Student;
import com.digitaldeparturesystem.response.ResponseResult;
import com.digitaldeparturesystem.service.ILogcatService;
import com.digitaldeparturesystem.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service  //图书馆
@Transactional
public class LogcatServiceImpl implements ILogcatService {

    //这里Autowired，字段名称要和类名一样，如果想自定义，就加一个Qualifier
    @Autowired
    @Qualifier("restHighLevelClient")
    private RestHighLevelClient client;

    private void getLogList(List<Log> logResult, SearchResponse searchResponse) {
        if (searchResponse.getHits().getHits().length == 0){
            return;
        }
        for (int i = 0; i < searchResponse.getHits().getTotalHits().value; i++) {
            Log logBean = new Log();
            //正则匹配
            String log = searchResponse.getHits().getAt(i).getSourceAsMap().get("message").toString();
            Pattern pattern = Pattern.compile("\\[(.*?)\\] \\[(.*?)\\] \\[(.*?)\\] \\[(.*?)\\] : (.*)");
            // 现在创建 matcher 对象
            Matcher m = pattern.matcher(log);
            if (m.find()) {
                logBean.setLogDate(m.group(1));
                logBean.setLogClass(m.group(3));
                logBean.setOption(m.group(5));
                logResult.add(logBean);
            }
        }
    }

    @Override
    public ResponseResult getLogs(String account, LogSearchCondition condition) {
        int startPage = condition.getStartPage();
        int pageSize = condition.getPageSize();
        String fromDate = condition.getFromDate();
        String toDate = condition.getToDate();
        String logClass = condition.getLogClass();
        //关键字查询
        String keyWord = condition.getKeyWord();

        List<Log> logResult = new ArrayList<>();
        SearchRequest searchRequest = new SearchRequest("dds");
        //查询条件，我们可以直接使用QueryBuilders 工具类实现
        BoolQueryBuilder builder = QueryBuilders.boolQuery();
        //构建搜索条件
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        //账号查询
        TermQueryBuilder userQuery = QueryBuilders.termQuery("message",account);
        builder.must(userQuery);
        RangeQueryBuilder timeRangeQuery = null;
        if (!TextUtils.isEmpty(fromDate)&&!(TextUtils.isEmpty(toDate))){
            //时间查询
            timeRangeQuery = QueryBuilders.rangeQuery("@timestamp");
            //时间范围   .must(timeRangeQuery)
            timeRangeQuery.gte(fromDate);
            timeRangeQuery.lte(toDate);
            builder.must(timeRangeQuery);
        }
        TermQueryBuilder logClassQuery = null;
        if (!TextUtils.isEmpty(logClass)){
            //类别查询
            logClassQuery = QueryBuilders.termQuery("message",logClass);
            builder.must(logClassQuery);
        }
        TermQueryBuilder keyWordQuery = null;
        if (!TextUtils.isEmpty(keyWord)){
            //关键字查询
            logClassQuery = QueryBuilders.termQuery("message",keyWord);
            builder.must(logClassQuery);
        }
        //查询
        sourceBuilder.query(builder);
        sourceBuilder.from((startPage-1)*pageSize);
        sourceBuilder.size(pageSize);//都有默认参数
        //倒序排序
        sourceBuilder.sort("@timestamp", SortOrder.DESC);
        //查询条件配置好之后，开始做查询
        searchRequest.source(sourceBuilder);
        SearchResponse searchResponse = null;
        try {
            searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            return ResponseResult.FAILED("查询日志失败");
        }
        //获取日志列表
        getLogList(logResult, searchResponse);
        return ResponseResult.SUCCESS("查询日志成功").setData(logResult);
    }
}
