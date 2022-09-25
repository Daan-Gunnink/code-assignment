package nl.infowijs.codeassignment

import io.vertx.config.ConfigRetriever
import io.vertx.config.ConfigRetrieverOptions
import io.vertx.config.ConfigStoreOptions
import io.vertx.core.AbstractVerticle
import io.vertx.core.Promise
import io.vertx.core.Vertx
import io.vertx.core.json.JsonObject
import io.vertx.ext.mongo.MongoClient
import io.vertx.ext.web.Router
import io.vertx.ext.web.handler.BodyHandler
import io.vertx.kotlin.core.json.jsonObjectOf
import nl.infowijs.codeassignment.controllers.ContactsController
import nl.infowijs.codeassignment.controllers.HealthController
import nl.infowijs.codeassignment.controllers.MessagesController

class HttpVerticle : AbstractVerticle() {

  fun createRouter(vertx: Vertx) = Router.router(vertx).apply {
    val messagesController = MessagesController(vertx)
    val contactsController = ContactsController()

    get("/healthz").handler(HealthController.healthCheck)
    get("/messages").produces("application/json").handler(messagesController::listMessages)
    get("/contacts").produces("application/json").handler(contactsController::listContacts)

    //fixme This should be sent as a application/json instead of a text/plain. Running into trouble with decoding JSON into an object
    post("/messages").consumes("text/plain").handler(BodyHandler.create()).handler(messagesController::storeMessage)
  }

  override fun start(startPromise: Promise<Void>) {
    val fileStore = ConfigStoreOptions()
      .setType("file")
      .setFormat("json")
      .setConfig(JsonObject().put("path", "config.json"))

    val options = ConfigRetrieverOptions().addStore(fileStore)
    val retriever = ConfigRetriever.create(vertx, options)

    retriever.getConfig {
      if (it.succeeded()) {
        val config = it.result()
        val port = config.getInteger("port")

        val router = createRouter(vertx)

        vertx
          .createHttpServer()
          .requestHandler(router)
          .listen(port) { http ->
            if (http.succeeded()) {
              startPromise.complete()
              println("HTTP server started on port 8888")
            } else {
              println(http.cause())
              startPromise.fail(http.cause())
            }
          }
      } else {
        it.cause().printStackTrace()
        startPromise.fail(it.cause())
      }
    }
  }
}
