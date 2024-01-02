package org.example.CassandraUltils;
import com.datastax.driver.core.Session;
import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import com.datastax.oss.driver.api.core.type.DataTypes;
import com.datastax.oss.driver.api.querybuilder.SchemaBuilder;
import com.datastax.oss.driver.api.querybuilder.schema.CreateTable;

public class StockRepository {
    private static final String TABLE_NAME = "stocks";

    private final Session session;

    public StockRepository(Session session) {
        this.session = session;
    }

    public void createTable() {
        createTable(null);
    }

    public void createTable(String keyspace) {
//        CreateTable createTable = SchemaBuilder.createTable(TABLE_NAME).ifNotExists()
//                .withPartitionKey("id", DataTypes.UUID)
//                .withColumn("sym", DataTypes.DOUBLE)
//                .withColumn("c", DataTypes.DOUBLE)
//                .withColumn("f", DataTypes.DOUBLE)
//                .withColumn("r", DataTypes.DOUBLE)
//                .withColumn("lastPrice", DataTypes.DOUBLE)
//                .withColumn("lastVolume", DataTypes.DOUBLE)
//                .withColumn("lot", DataTypes.DOUBLE)
//                .withColumn("ot", DataTypes.TEXT)
//                .withColumn("changePc", DataTypes.TEXT)
//                .withColumn("avePrice", DataTypes.TEXT)
//                .withColumn("highPrice", DataTypes.TEXT)
//                .withColumn("lowPrice", DataTypes.TEXT)
//                .withColumn("fRoom", DataTypes.TEXT)
//                .withColumn("g1", DataTypes.TEXT)
//                .withColumn("g2", DataTypes.TEXT)
//                .withColumn("g3", DataTypes.TEXT)
//                .withColumn("g4", DataTypes.TEXT)
//                .withColumn("g5", DataTypes.TEXT)
//                .withColumn("g6", DataTypes.TEXT)
//                .withColumn("g7", DataTypes.TEXT)
//                .withColumn("CWListedShare", DataTypes.TEXT)
//                .withColumn("industry", DataTypes.TEXT)
//                .withColumn("crawledTime", DataTypes.TIMESTAMP);
        StringBuilder sb = new StringBuilder("CREATE TABLE IF NOT EXISTS ")
                .append(TABLE_NAME).append("(")
                .append("id UUID PRIMARY KEY,")
                .append("sym DOUBLE,")
                .append("c DOUBLE,")
                .append("f DOUBLE,")
                .append("r DOUBLE,")
                .append("lastPrice DOUBLE,")
                .append("lastVolume DOUBLE,")
                .append("lot DOUBLE,")
                .append("ot TEXT,")
                .append("changePc TEXT,")
                .append("avePrice TEXT,")
                .append("highPrice TEXT,")
                .append("lowPrice TEXT,")
                .append("fRoom TEXT,")
                .append("g1 TEXT,")
                .append("g2 TEXT,")
                .append("g3 TEXT,")
                .append("g4 TEXT,")
                .append("g5 TEXT,")
                .append(" g6 TEXT,")
                .append("g7 TEXT,")
                .append("CWListedShare TEXT,")
                .append("industry TEXT,")
                .append("crawledTime TIMESTAMP );");
        session.execute(sb.toString());
    }
}
