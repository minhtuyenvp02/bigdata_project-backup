package org.example.CassandraUltils;
import com.datastax.driver.core.Cluster.Builder;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.Cluster;

public class CassandraConnector {

    private  Cluster cluster;

    private Session session;

    public void connect(String node, Integer port) {
       Builder b = com.datastax.driver.core.Cluster.builder().addContactPoint(node);
        if (port != null) {
            b.withPort(port);
        }
        cluster = b.build();

        session = cluster.connect();
    }

    public Session getSession() {
        return this.session;
    }

    public void close() {
        session.close();
        cluster.close();
    }
}
