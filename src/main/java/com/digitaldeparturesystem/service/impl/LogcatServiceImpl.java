package com.digitaldeparturesystem.service.impl;

import com.digitaldeparturesystem.response.ResponseResult;
import com.digitaldeparturesystem.service.ILogcatService;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.io.IOException;

public class LogcatServiceImpl implements ILogcatService {

    //这里Autowired，字段名称要和类名一样，如果想自定义，就加一个Qualifier
    @Autowired
    @Qualifier("restHighLevelClient")
    private RestHighLevelClient client;

    @Override
    public ResponseResult getLogs() {
        GetResponse getResponse = null;
        try {
            GetRequest getRequest = new GetRequest("ls_index", "1");
            getResponse = client.get(getRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResponseResult.SUCCESS("获取日志成功").setData(getResponse.getSourceAsString());
    }
}
