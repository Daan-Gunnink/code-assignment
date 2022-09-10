package nl.infowijs.codeassignment

import io.vertx.core.AbstractVerticle
import io.vertx.core.Promise
import io.vertx.core.Vertx
import io.vertx.ext.web.Router
import io.vertx.ext.web.handler.BodyHandler
import nl.infowijs.codeassignment.controllers.HealthController
import nl.infowijs.codeassignment.controllers.MessagesController

class MainVerticle : AbstractVerticle() {
  private val messagesController = MessagesController()
  fun createRouter(vertx: Vertx) = Router.router(vertx).apply {
    get("/healthz").handler(HealthController.healthCheck)
    get("/messages").produces("application/json").handler(messagesController::listMessages)

    //fixme This should be sent as a application/json instead of a text/plain. Running into trouble with decoding JSON into an object
    post("/messages").consumes("text/plain").handler(BodyHandler.create()).handler(messagesController::storeMessage)
  }

  override fun start(startPromise: Promise<Void>) {
    val router = createRouter(vertx)

    vertx
      .createHttpServer()
      .requestHandler(router)
      .listen(8888) { http ->
        if (http.succeeded()) {
          startPromise.complete()
          println("HTTP server started on port 8888")
        } else {
          startPromise.fail(http.cause());
        }
      }
  }
}
