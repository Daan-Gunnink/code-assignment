package nl.infowijs.codeassignment

import io.vertx.config.ConfigRetriever
import io.vertx.config.ConfigRetrieverOptions
import io.vertx.config.ConfigStoreOptions
import io.vertx.core.AbstractVerticle
import io.vertx.core.CompositeFuture
import io.vertx.core.Future
import io.vertx.core.Handler
import io.vertx.core.Promise
import io.vertx.core.Vertx
import io.vertx.core.json.JsonObject
import io.vertx.ext.mongo.MongoClient
import io.vertx.ext.web.Router
import io.vertx.ext.web.handler.BodyHandler
import io.vertx.kotlin.core.json.get
import io.vertx.kotlin.core.json.jsonObjectOf
import nl.infowijs.codeassignment.controllers.ContactsController
import nl.infowijs.codeassignment.controllers.HealthController
import nl.infowijs.codeassignment.controllers.MessagesController
import org.apache.log4j.BasicConfigurator


class MainVerticle : AbstractVerticle() {

  override fun start(startPromise: Promise<Void>?) {
    CompositeFuture.all(
      deployVerticle(HttpVerticle::class.java.name),
      deployVerticle(PersistenceVerticle::class.java.name)
    ).onSuccess {
      BasicConfigurator.configure()
      startPromise?.complete()
    }.onFailure { f ->
      println(f.cause)
      startPromise?.fail(f.cause) }

  }

  fun deployVerticle(verticleName: String): Future<Void> {
    val retVal = Future.future<Void> {
      vertx.deployVerticle(verticleName) { event ->
        if (event.succeeded()) {
          it.complete()
        } else {
          println(event.cause())
          it.fail(event.cause())
        }
      }
    }

    return retVal
  }


}
