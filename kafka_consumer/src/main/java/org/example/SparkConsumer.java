package org.example;

import org.apache.http.HttpHost;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.sql.*;
import org.apache.spark.sql.streaming.StreamingQuery;
import org.apache.spark.sql.streaming.StreamingQueryException;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;

import java.sql.Timestamp;
import java.util.concurrent.TimeoutException;

import static org.apache.spark.sql.functions.*;

public class SparkConsumer {
    public static void main(String[] args) throws TimeoutException, StreamingQueryException {
        Logger.getLogger("org.apache").setLevel(Level.WARN);
        Logger.getLogger("org.apache.spark.storage").setLevel(Level.ERROR);
        SparkSession session = SparkSession.builder()
                .master("local[*]")
                .appName("ProcessStockData")
                .getOrCreate();
        session.sparkContext().setLogLevel("WARN");
        session.conf().set("spark.sql.debug.maxToStringFields", 1000);
        Dataset<Row> df = session.readStream()
                .format("kafka")
                .option("kafka.bootstrap.servers", "kafka11:8097, kafka12:8098, kafka13:8099")
                .option("subscribe", "firstdemo")
                .option("startingOffsets", "earliest")
                .option("includeHeaders", "true")
                .load();
        df.printSchema();
        Schema schema = new Schema();
        StructType scm = schema.getSchema();
        Dataset<Row> stocks = df.selectExpr("CAST(value AS STRING)")
                .select(functions.from_json(functions.col("value"), scm).as("data"))
                .select("data.*");
        StreamingQuery query = stocks.writeStream()
                .format("console")
                .outputMode("append")
                .start();

        query.awaitTermination();
        passDataToEs(stocks);
    }

    public static Dataset<Row> parseDataFromKafkaMessage(Dataset<Row> sdf, StructType schema) {
        if (!sdf.isStreaming()) {
            throw new IllegalArgumentException("DataFrame doesn't receive streaming data");
        }

        Column col = split(sdf.col("data.*"), ",");

        for (int idx = 0; idx < schema.length(); idx++) {
            StructField field = schema.apply(idx);
            sdf = sdf.withColumn(field.name(), col.getItem(idx).cast(field.dataType()));
        }

        Column[] columns = new Column[schema.length()];
        for (int idx = 0; idx < schema.length(); idx++) {
            StructField field = schema.apply(idx);
            columns[idx] = sdf.col(field.name());
        }

        return sdf.select(columns);
    }

    public static void passDataToEs(Dataset<Row> stocks) {
        IndexRequest indexRequest = new IndexRequest("stock");

        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(new HttpHost("localhost", 9200, "http")));

        stocks.foreach(row -> {
            Stock stock = new Stock(
                    row.getAs("id"),
                    row.getAs("sym"),
                    row.getAs("mc"),
                    row.getAs("c"),
                    row.getAs("f"),
                    row.getAs("r"),
                    row.getAs("lastPrice"),
                    row.getAs("lastVolume"),
                    row.getAs("lot"),
                    row.getAs("ot"),
                    row.getAs("changePc"),
                    row.getAs("avePrice"),
                    row.getAs("highPrice"),
                    row.getAs("lowPrice"),
                    row.getAs("fBVol"),
                    row.getAs("fBValue"),
                    row.getAs("fSVolume"),
                    row.getAs("fSValue"),
                    row.getAs("fRoom"),
                    row.getAs("g1"),
                    row.getAs("g2"),
                    row.getAs("g3"),
                    row.getAs("g4"),
                    row.getAs("g5"),
                    row.getAs("g6"),
                    row.getAs("g7"),
                    row.getAs("mp"),
                    row.getAs("CWUnderlying"),
                    row.getAs("CWIssuerName"),
                    row.getAs("CWType"),
                    row.getAs("CWMaturityDate"),
                    row.getAs("CWLastTradingDate"),
                    row.getAs("CWExcersisePrice"),
                    row.getAs("CWExerciseRatio"),
                    row.getAs("CWListedShare"),
                    row.getAs("sType"),
                    row.getAs("sBenefit"),
                    row.getAs("industry"),
                    Timestamp.valueOf((String) row.getAs("crawledTime"))
            );

            indexRequest.source(new ObjectMapper().writeValueAsString(stock), XContentType.JSON);
            client.index(indexRequest, RequestOptions.DEFAULT);
        });

        System.out.println("Pass data success!!!");
    }
}
