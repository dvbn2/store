package com.dvbn.listener;

import com.dvbn.clients.ProductClient;
import com.dvbn.pojo.Product;
import com.dvbn.doc.ProductDoc;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.DeleteByQueryRequest;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * description: 监控boot 程序启动,完成es数据的同步工作
 */
@Slf4j
@Component
public class SpringBootListener implements ApplicationRunner {


    @Resource
    private RestHighLevelClient client;

    @Resource
    private ProductClient productClient;

    private String indexStr = "{\n" +
            "  \"mappings\": {\n" +
            "    \"properties\": {\n" +
            "      \"productId\":{\n" +
            "        \"type\": \"integer\"\n" +
            "      },\n" +
            "      \"productName\":{\n" +
            "        \"type\": \"text\",\n" +
            "        \"analyzer\": \"ik_smart\",\n" +
            "        \"copy_to\": \"all\"\n" +
            "      },\n" +
            "      \"categoryId\":{\n" +
            "        \"type\": \"integer\"\n" +
            "      },\n" +
            "      \"productTitle\":{\n" +
            "        \"type\": \"text\",\n" +
            "        \"analyzer\": \"ik_smart\",\n" +
            "        \"copy_to\": \"all\"\n" +
            "      },\n" +
            "      \"productIntro\":{\n" +
            "        \"type\":\"text\",\n" +
            "        \"analyzer\": \"ik_smart\",\n" +
            "        \"copy_to\": \"all\"\n" +
            "      },\n" +
            "      \"productPicture\":{\n" +
            "        \"type\": \"keyword\",\n" +
            "        \"index\": false\n" +
            "      },\n" +
            "      \"productPrice\":{\n" +
            "        \"type\": \"double\",\n" +
            "        \"index\": true\n" +
            "      },\n" +
            "      \"productSellingPrice\":{\n" +
            "        \"type\": \"double\"\n" +
            "      },\n" +
            "      \"productNum\":{\n" +
            "        \"type\": \"integer\"\n" +
            "      },\n" +
            "      \"productSales\":{\n" +
            "        \"type\": \"integer\"\n" +
            "      },\n" +
            "      \"all\":{\n" +
            "        \"type\": \"text\",\n" +
            "        \"analyzer\": \"ik_max_word\"\n" +
            "      }\n" +
            "    }\n" +
            "  }\n" +
            "}";


    /**
     * 需要在此方法,完成es数据的同步!
     * 1.判断下es中product索引是否存在
     * 2.不存在, java代码创建一个
     * 3.存在删除原来的数据
     * 4.查询商品全部数据
     * 5.进行es库的更新工作[插入]
     *
     * @param args
     * @throws Exception
     */
    @Override
    public void run(ApplicationArguments args) throws Exception {
        //数据库数据初始化

        //判断是否存在product索引
        GetIndexRequest getIndexRequest = new GetIndexRequest("product");
        boolean exists = client.indices().exists(getIndexRequest, RequestOptions.DEFAULT);


        if (exists) {
            //存在 删除全部数据
            DeleteByQueryRequest deleteIndexRequest = new DeleteByQueryRequest("product");
            deleteIndexRequest.setQuery(QueryBuilders.matchAllQuery());
            client.deleteByQuery(deleteIndexRequest, RequestOptions.DEFAULT);

        } else {
            //不存在,创建新索引
            CreateIndexRequest createIndexRequest = new CreateIndexRequest("product");
            createIndexRequest.source(indexStr, XContentType.JSON);
            client.indices().create(createIndexRequest, RequestOptions.DEFAULT);
        }


        //查询全部数据
        List<Product> ProductList = productClient.allList();

        //插入全部数据
        BulkRequest bulkRequest = new BulkRequest();
        ObjectMapper objectMapper = new ObjectMapper();
        for (Product product : ProductList) {
            ProductDoc productDoc = new ProductDoc(product);

            IndexRequest indexRequest = new IndexRequest("product").id(productDoc.getProductId().toString());
            // 将productDoc转成JSON
            String json = objectMapper.writeValueAsString(productDoc);
            indexRequest.source(json, XContentType.JSON);
            bulkRequest.add(indexRequest);
        }

        client.bulk(bulkRequest, RequestOptions.DEFAULT);

        log.info("ApplicationRunListener.run业务结束，完成数据更新!");
    }
}
