package nl.infowijs.codeassignment.controllers

import io.vertx.core.Vertx
import io.vertx.core.json.Json
import io.vertx.core.json.JsonArray
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.RoutingContext
import io.vertx.kotlin.core.json.jsonArrayOf
import nl.infowijs.codeassignment.data.Contacts
import nl.infowijs.codeassignment.models.MyMessage
import nl.infowijs.codeassignment.modules.WebResponse
import java.time.Clock
import java.time.Instant

class MessagesController(private val vertx: Vertx) {
  //TODO add pagination for messages
  fun listMessages(routingContext: RoutingContext) {
    val message = JsonObject()
    message.put("action", "list-messages")

    vertx.eventBus().request<JsonArray>("persistence-address", message){
      if(it.succeeded()){
        WebResponse(routingContext).end(it.result().body())
      }
      if(it.failed()){
        routingContext.response().setStatusCode(500).end()
      }
    }
  }

  fun storeMessage(routingContext: RoutingContext) {
    //fixme This should be sent as a JSON instead of a raw string. Running into trouble with decoding JSON into an object
    val message = JsonObject()
    message.put("action", "store-message")
    message.put("data", routingContext.body().asString())

    vertx.eventBus().request<JsonArray>("persistence-address", message){
      if(it.succeeded()){
        routingContext.response().setStatusCode(200).end()
      }
      if(it.failed()){
        routingContext.response().setStatusCode(500).end()
      }

    }
  }
}
