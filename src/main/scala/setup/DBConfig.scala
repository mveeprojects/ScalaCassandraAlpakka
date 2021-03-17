package setup

import com.datastax.driver.core.Cluster
import com.datastax.oss.driver.api.core.CqlSession
import config.AppConfig.appConfig.cassandra._
import io.getquill.{CamelCase, CassandraAsyncContext}
import utils.Logging

import java.net.InetSocketAddress

object DBConfig extends Logging {

  private val quillCluster = Cluster
    .builder()
    .addContactPoint(host)
    .withoutJMXReporting
    .build()

  val quillDB = new CassandraAsyncContext(CamelCase, quillCluster, keyspace, preparedstatementcache)

  def openDBInitSession: (String, Int, String) => CqlSession = (node: String, port: Int, datacentre: String) =>
    CqlSession.builder
      .addContactPoint(new InetSocketAddress(node, port))
      .withLocalDatacenter(datacentre)
      .build

  def closeDBInitSession(session: CqlSession): Unit = {
    logger.info("Closing DB initialisation session.")
    session.close()
  }
}

