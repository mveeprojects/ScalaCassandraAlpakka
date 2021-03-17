package setup

import com.datastax.oss.driver.api.core.CqlSession
import utils.Logging

import java.net.InetSocketAddress

object DBConfig extends Logging {

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
