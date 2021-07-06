package com.walmart.fulfillmentutiltrigger.elastic;

import com.walmart.fulfillmentutiltrigger.Mapper.FoIndex;
import com.walmart.fulfillmentutiltrigger.Util.JsonUtil;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

/**
 * Created by p0k00df on 06/07/21
 **/
@Component("elasticQueryExecutor")
//@Qualifier("elasticQueryExecutor")
public class ElasticQueryExecutor {

    private Long time = 1625486465000L;

    private int DEFAULT_BATCH_SIZE = 9999;

    private String FR_INDEX_NAME = "fo-sams-index";

    private long DEFAULT_SCROLL_TIME_IN_MILLIS = 20000;

    @Autowired
    private RestHighLevelClient client;

    public List<String> getDataFromES() {
        BoolQueryBuilder boolQuery = new BoolQueryBuilder();
        synchronized (ElasticQueryExecutor.class) {
            long newTime = addTime(time);
            System.out.println("Thread " + Thread.currentThread().getName() + " start: " + time + " and end: " + newTime);
            boolQuery.must(QueryBuilders.rangeQuery("foCreatedDate").gte(time).lte(newTime).includeLower(true));
            time = newTime;
        }
        SearchSourceBuilder builder = new SearchSourceBuilder();
        builder.size(DEFAULT_BATCH_SIZE);
        builder.query(boolQuery).size(DEFAULT_BATCH_SIZE);
        String[] includeFields = new String[]{"id", "purchaseOrderNo"};
        builder.fetchSource(includeFields, null);
        SearchRequest searchRequest = new SearchRequest(FR_INDEX_NAME).source(builder);
        return executeQuery(searchRequest);
    }

    public List<String> executeQuery(SearchRequest searchRequest) {

        List<String> pos = new ArrayList<>();
        SearchResponse searchResponse = null;
        try {
            searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
//
//            pos = Arrays.stream(searchResponse.getHits().getHits())
//                            .map(hit -> JsonUtil.getObjectMapper().convertValue(hit.getSourceAsMap(), FoIndex.class))
//                            .collect(Collectors.toList());
            System.out.println("Hits in Thread " + Thread.currentThread().getName() + " is " + searchResponse.getHits().getHits().length);
            for (SearchHit hit : searchResponse.getHits().getHits()) {
                Map<String, Object> sourceAsMap = hit.getSourceAsMap();
                FoIndex foIndex = JsonUtil.getObjectMapper().convertValue(sourceAsMap, FoIndex.class);
                pos.add(foIndex.getPurchaseOrderNo());
            }

//            while (searchResponse.getHits().getHits().length != 0) {
//                SearchHits hits = searchResponse.getHits();
//                SearchHit[] searchHits = hits.getHits();
//
//                for (SearchHit hit : searchHits) {
//                    Map<String, Object> sourceAsMap = hit.getSourceAsMap();
//                    FoIndex foIndex = JsonUtil.getObjectMapper().convertValue(sourceAsMap, FoIndex.class);
//                    pos.add(foIndex.getPurchaseOrderNo());
//                    i++;
//                }
//
//                searchResponse = client.scroll(new SearchScrollRequest(scrollId).
//                        scroll(new TimeValue(DEFAULT_SCROLL_TIME_IN_MILLIS)), RequestOptions.DEFAULT);
//            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return pos;
    }

    public static Long addTime(Long time) {
        Date timestamp = new Date(time);
        timestamp.setTime(time + 5*1000);
        return timestamp.getTime();
    }

//    public static Long addTime(Long time) {
//        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
//         cal.setTimeInMillis(time*1000);
//        cal.add(Calendar.SECOND, 5);
//        return new Timestamp(cal.getTime().getTime()).getTime();
//    }

//    public static void main(String[] args) {
//        System.out.println(addTime(1625486465000L));
//    }
}
