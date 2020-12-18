package com.digitaldeparturesystem.mapper;

import com.alibaba.fastjson.JSON;
import com.digitaldeparturesystem.response.ResponseResult;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ESApiTest {

    @Resource
    private RestHighLevelClient client;

    //获得文档信息
    @Test
    void testGetDocument() throws IOException {
        //1.创建获取请求
        GetRequest getRequest = new GetRequest("system-2020.12.18","system","AXZzoEvuIqxxE5mNHWIT");
        boolean exists = client.exists(getRequest, RequestOptions.DEFAULT);
        System.out.println(exists);

        //获取索引
        GetResponse getResponse = client.get(getRequest, RequestOptions.DEFAULT);
        System.out.println("=========================================================");
        Map<String, Object> source = getResponse.getSource();
        for (Map.Entry<String, Object> entry : source.entrySet()) {
            System.out.println(entry.getKey() + " --> " + entry.getValue());
        }
        System.out.println("message --> " + source.get("message"));
        System.out.println("===========================================================");
        Pattern pattern = Pattern.compile("\\[(.*?)\\] \\[(.*?)\\] \\[(.*?)\\] \\[(.*?)\\] : (.*)");
        // 现在创建 matcher 对象
        Matcher m = pattern.matcher("[2020/12/17-19:57:32] [main] [INFO ] [com.digitaldeparturesystem.DDSApplication] : Started DDSApplication in 6.933 seconds (JVM running for 8.288)");
        if (m.find()){
            System.out.println(m.group(0));
            System.out.println(m.group(1));
            System.out.println(m.group(2));
            System.out.println(m.group(3));
            System.out.println(m.group(4));
            System.out.println(m.group(5));
        }
        System.out.println("===========================================================");
    }

    @Test
    void testConditionSearch() throws IOException {
        SearchRequest searchRequest = new SearchRequest("dds-2020.11.29");
        //构建搜索条件
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        //查询条件，我们可以直接使用QueryBuilders 工具类实现
//		QueryBuilders.termQuery:精确
//		QueryBuilders.matchAllQuery：匹配所有
        //TODO:报错
        //时间查询
        RangeQueryBuilder timeRangeQuery = QueryBuilders.rangeQuery("@timestamp");
        //时间范围
        timeRangeQuery.gte("2020-11-29T13:50:31.239Z");
        timeRangeQuery.lte("2020-11-29T13:52:53.051Z");

        //内容查询
        MatchQueryBuilder contentQuery = QueryBuilders.matchQuery("message","2018110427");

        //一起构造
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery().must(timeRangeQuery).must(contentQuery);

        sourceBuilder.query(boolQueryBuilder);
        sourceBuilder.from(0);
        sourceBuilder.size(50);//都有默认参数
        //倒序排序
        sourceBuilder.sort("@timestamp", SortOrder.DESC);

        //查询条件配置好之后，开始做查询
        searchRequest.source(sourceBuilder);
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        System.out.println("总的条数 --> " + searchResponse.getHits().getTotalHits());
        for (int i = 0; i < searchResponse.getHits().getTotalHits().value; i++) {
            String log = searchResponse.getHits().getAt(i).getSourceAsMap().get("message").toString();
            System.out.println(log);
            Pattern pattern = Pattern.compile("\\[(.*?)\\] \\[(.*?)\\] \\[(.*?)\\] \\[(.*?)\\] : (.*)");
            // 现在创建 matcher 对象
            Matcher m = pattern.matcher(log);
            if (m.find()){
                System.out.println(m.group(0));
                System.out.println(m.group(1));
                System.out.println(m.group(2));
                System.out.println(m.group(3));
                System.out.println(m.group(4));
                System.out.println(m.group(5));
            }
        }

    }

    @Test
    void testSearch() throws IOException {
        SearchRequest searchRequest = new SearchRequest("system-2020.12.18");

        //构建搜索条件
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        //查询条件，我们可以直接使用QueryBuilders 工具类实现
//        //QueryBuilders.termQuery:精确
//        //QueryBuilders.matchAllQuery：匹配所有
        MatchAllQueryBuilder queryBuilder = QueryBuilders.matchAllQuery();
        sourceBuilder.query(queryBuilder);
        sourceBuilder.from();
        sourceBuilder.size();//都有默认参数
        sourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));//查询的时间

        //在search里面做查询
        searchRequest.source(sourceBuilder);

        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        System.out.println("总的条数 --> " + searchResponse.getHits().getTotalHits());
        for (int i = 0; i < searchResponse.getHits().getTotalHits().value; i++) {
            String log = searchResponse.getHits().getAt(i).getSourceAsMap().get("message").toString();
            System.out.println(log);
            Pattern pattern = Pattern.compile("\\[(.*?)\\] \\[(.*?)\\] \\[(.*?)\\] \\[(.*?)\\] : (.*)");
            // 现在创建 matcher 对象
            Matcher m = pattern.matcher(log);
            if (m.find()){
                System.out.println(m.group(0));
                System.out.println(m.group(1));
                System.out.println(m.group(2));
                System.out.println(m.group(3));
                System.out.println(m.group(4));
                System.out.println(m.group(5));
            }
        }
    }
    //searchResponse.getHits()：就是一个索引
    //searchResponse.getHits().getAt(0).getSourceAsMap()：就是一条里面得所有字段
    //searchResponse.getHits().getAt(i).getSourceAsMap().get("message")：对应得message字段
    //Pattern pattern = Pattern.compile("\\[(.*?)\\] \\[(.*?)\\] \\[(.*?)\\] \\[(.*?)\\] : (.*)");：匹配message里面得每个字段

}
