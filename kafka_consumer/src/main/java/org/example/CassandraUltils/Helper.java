package org.example.CassandraUltils;

import com.datastax.driver.core.Session;
import com.datastax.oss.driver.api.core.CqlSession;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.common.settings.Settings;
import com.datastax.driver.core.Cluster;
import java.io.IOException;
import java.util.List;

public class Helper {
    final String keyspace = "stock_exchange";
    final String tableCass = "first_table";
    public static void main(String[] args) throws IOException {
        Helper helper = new Helper();
//        helper.createIndexInES();
        helper.createTableCass();
    }
    public void createTableCass() {
        CassandraConnector connector = new CassandraConnector();
        connector.connect("127.0.0.1", 9042);
//        connector.connect("127.0.0.1", 9043, "my-datacenter-1");
//        connector.connect("127.0.0.1", 9044, "my-datacenter-1");
         Session session = connector.getSession();
        StockRepository stockRepository = new StockRepository(session);
        KeyspaceRepository keyspaceRepository = new KeyspaceRepository(session);


        keyspaceRepository.createKeyspace("testKeyspace", "SimpleStrategy", 3);
        keyspaceRepository.useKeyspace("testKeyspace");
        stockRepository.createTable("testKeyspace");
        connector.close();
    }

    public void createIndexInES() throws IOException {
        RestHighLevelClient client = new RestHighLevelClient(RestClient.builder(new HttpHost("localhost", 9200, "http")));
        CreateIndexRequest request = new CreateIndexRequest("stock_streaming");
        request.settings(Settings.builder().put("index.number_of_shards", 1).put("index.number_of_replicas", 2));
        CreateIndexResponse createIndexResponse = client.indices().create(request, RequestOptions.DEFAULT);
        System.out.println("response id: " + createIndexResponse.index());
    }

}
