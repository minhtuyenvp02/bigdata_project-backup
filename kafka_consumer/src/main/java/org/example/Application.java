package org.example;

import org.apache.spark.sql.streaming.StreamingQueryException;

import java.util.concurrent.TimeoutException;

public class Application {
    public static void main(String[] args) throws TimeoutException, StreamingQueryException {
        DataToEs dataToEs = new DataToEs();
        DataToCassandra dataToCassandra = new DataToCassandra();
        dataToCassandra.start();
//        dataToEs.start();
    }
}
