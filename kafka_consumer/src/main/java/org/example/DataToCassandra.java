package org.example;


import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.sql.*;
import org.apache.spark.sql.streaming.StreamingQuery;
import org.apache.spark.sql.streaming.StreamingQueryException;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;

import java.util.concurrent.TimeoutException;

import static org.apache.spark.sql.functions.*;

public class DataToCassandra {
    public void start() throws TimeoutException, StreamingQueryException {
        Logger.getLogger("org.apache").setLevel(Level.WARN);
        Logger.getLogger("org.apache.spark.storage").setLevel(Level.ERROR);

        SparkConf conf = new SparkConf();
        conf.setAppName("DataToEs")
            .setMaster("local[*]")
            .set("spark.cassandra.connection.host", "cassandra")
            .set("spark.cassandra.connection.port", "9042");

        SparkSession session = SparkSession.builder()
                .config(conf)
                .getOrCreate();

        session.sparkContext().setLogLevel("WARN");
        Dataset<Row> df = session.readStream()
                .format("kafka")
                .option("kafka.bootstrap.servers", "localhost:8097, localhost:8098, localhost:8099")
                .option("subscribe", "stockStreaming")
                .option("startingOffsets", "earliest")
                .option("includeHeaders", "true")
                .load();

        df.printSchema();
        Schema schema = new Schema();
        StructType scm = schema.getSchema();
        Dataset<Row> stocks = df.selectExpr("CAST(value AS STRING)")
                .select(functions.from_json(functions.col("value"), scm).as("data"))
                .select("data.*");
        Dataset<Row> stock_df = stocks.drop("CWMaturityDate")
                .drop("CWLastTradingDate")
                .drop("CWExcersisePrice")
                .drop("CWExerciseRatio")
                .drop("sType")
                .drop("sBenefit")
                .drop("id")
                .drop("mp")
                .drop("CWUnderlying")
                .drop("CWIssuerName")
                .drop("CWType")
                .drop("fBVol")
                .drop("fBValue")
                .drop("fSVolume")
                .drop("fSValue");

        StreamingQuery query = stocks.writeStream()
                .format("org.apache.spark.sql.cassandra")
//                .format("console")
                .outputMode("append")
                .foreachBatch((Dataset<Row> writeDF, Long batchId) -> {
                    writeToCassandra(writeDF, session);
                })
                .start();
        query.awaitTermination();
    }
    private static void writeToCassandra(Dataset<Row> writeDF, SparkSession spark) {
        writeDF.write()
                .format("org.apache.spark.sql.cassandra")
                .mode("append")
                .option("table", "stock_test")
                .option("keyspace", "spark_stock")
                .save();
    }
}
