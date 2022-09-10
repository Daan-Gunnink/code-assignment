package nl.infowijs.codeassignment.controllers

import io.vertx.core.Handler
import io.vertx.ext.web.RoutingContext

class HealthController {
  companion object {
    val healthCheck = Handler<RoutingContext> { req ->
      req.response().setStatusCode(204).end()
    }
  }
}
