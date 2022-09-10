package nl.infowijs.codeassignment.controllers

import io.vertx.core.json.Json
import io.vertx.core.json.JsonArray
import io.vertx.core.json.impl.JsonUtil
import io.vertx.ext.web.RoutingContext
import nl.infowijs.codeassignment.data.Contacts
import nl.infowijs.codeassignment.models.Message
import nl.infowijs.codeassignment.models.NewMessage
import nl.infowijs.codeassignment.modules.WebResponse
import java.time.Clock
import java.time.Instant
import java.util.*

class MessagesController{

  private val storedMessages = mutableListOf<Message>()
  fun listMessages(routingContext: RoutingContext) {

    val messages = JsonArray.of(storedMessages.map { it.toJsonObject() })
    WebResponse(routingContext).end(messages)
  }

  fun storeMessage(routingContext: RoutingContext){
    print("found ${routingContext.body().asString()}")

    //fixme This should be sent as a JSON instead of a raw string. Running into trouble with decoding JSON into an object
    val newMessage = routingContext.body().asString()

    //fixme Don't grab a random contact here..
    val contact = Contacts.getContacts().random()

    storedMessages.add(
      Message(
        message = newMessage,
        datetime = Instant.now(Clock.systemUTC()),
        person = contact
      )
    )

    routingContext.response().setStatusCode(200).end()
  }
}
