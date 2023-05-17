package com.dvbn.serivce.impl;

import com.dvbn.param.ProductSearchParams;
import com.dvbn.pojo.Product;
import com.dvbn.serivce.SearchService;
import com.dvbn.utils.Result;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;

@Service
@Slf4j
public class SearchServiceImpl implements SearchService {

    @Resource
    private RestHighLevelClient restHighLevelClient;

    /**
     * 根据关键字和分页进行数据库数据查询
     * 1.判断关键字是否为null null查询全部不为null all字段查询
     * 2.添加分页属性
     * 3. es查询
     * 4.结果处理
     *
     * @param productSearchParams
     * @return
     */
    @Override
    public Result search(ProductSearchParams productSearchParams) {

        SearchRequest searchRequest = new SearchRequest("product");
        String search = productSearchParams.getSearch();

        if (StringUtils.isEmpty(search)) {
            // 为null 不添加all关键字,查询全部即可
            searchRequest.source().query(QueryBuilders.matchAllQuery()); // 查询全部
        } else {
            // 不为null 添加all的匹配
            searchRequest.source().query(QueryBuilders.matchQuery("all", search));
        }
        // 进行分页数据添加
        searchRequest.source().from((productSearchParams.getCurrentPage() - 1) * productSearchParams.getPageSize()); // 偏移量 （当前页数-1）*页容量
        searchRequest.source().size(productSearchParams.getPageSize()); // 传入要查询的数据数量

        SearchResponse searchResponse = null;
        try {
            searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new RuntimeException("查询错误");
        }

        // 在se表对象中获取数据
        SearchHits hits = searchResponse.getHits();

        long total = hits.getTotalHits().value; // 查询到符合数据的数量

        SearchHit[] hitsHits = hits.getHits();

        ArrayList<Product> productList = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();

        for (SearchHit hitsHit : hitsHits) {
            // 查询的内容数据  productDoc模型对应的json数据
            String sourceAsString = hitsHit.getSourceAsString();
            // 将json转为product对象
            Product product;
            try {
                // productDoc中有all , product中没有all ,json转Product反序列化时会报错
                //TODO:修改product的实体类, 添加忽略没有属性的注解! @JsonIgnoreProperties(ignoreUnknown = true)
                product = objectMapper.readValue(sourceAsString, Product.class);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            productList.add(product);
        }

        return Result.ok(null, productList, total);
    }
}
