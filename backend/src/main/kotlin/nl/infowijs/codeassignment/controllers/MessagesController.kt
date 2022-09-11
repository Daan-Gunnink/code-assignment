package nl.infowijs.codeassignment.controllers

import io.vertx.core.json.JsonArray
import io.vertx.core.json.JsonObject
import io.vertx.ext.mongo.MongoClient
import io.vertx.ext.web.RoutingContext
import io.vertx.kotlin.core.json.get
import nl.infowijs.codeassignment.data.Contacts
import nl.infowijs.codeassignment.models.Message
import nl.infowijs.codeassignment.modules.WebResponse
import java.time.Clock
import java.time.Instant

class MessagesController(private val mongoClient: MongoClient){

  private val storedMessages = mutableListOf<Message>()

  //TODO add pagination for messages
  fun listMessages(routingContext: RoutingContext) {
    val query = JsonObject()
    mongoClient.find("messages", query) { response ->
      if(response.succeeded()){
        val messages = JsonArray(response.result())
        WebResponse(routingContext).end(messages)
      }
      else{
        routingContext.response().setStatusCode(500).end()
      }
    }
  }

  fun storeMessage(routingContext: RoutingContext){
    //fixme This should be sent as a JSON instead of a raw string. Running into trouble with decoding JSON into an object
    val newMessage = routingContext.body().asString()

    //fixme Don't grab a random contact here..
    val contact = Contacts.getContacts().random()

    val doc = JsonObject().put("message", newMessage).put("dateTime", Instant.now(Clock.systemUTC())).put("contactId", contact.id)
    mongoClient.save("messages", doc) { res ->
      if (res.succeeded()) {
        println("added message ${res.result()}")
      }
      else{
        res.cause().printStackTrace()
      }
    }

    routingContext.response().setStatusCode(200).end()
  }
}
