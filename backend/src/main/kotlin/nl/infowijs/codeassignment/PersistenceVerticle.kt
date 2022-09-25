package nl.infowijs.codeassignment

import io.vertx.config.ConfigRetriever
import io.vertx.config.ConfigRetrieverOptions
import io.vertx.config.ConfigStoreOptions
import io.vertx.core.AbstractVerticle
import io.vertx.core.Promise
import io.vertx.core.eventbus.Message
import io.vertx.core.eventbus.MessageConsumer
import io.vertx.core.json.JsonArray
import io.vertx.core.json.JsonObject
import io.vertx.ext.mongo.MongoClient
import io.vertx.kotlin.core.json.jsonObjectOf
import nl.infowijs.codeassignment.data.Contacts
import nl.infowijs.codeassignment.models.MyMessage
import java.time.Clock
import java.time.Instant

class PersistenceVerticle : AbstractVerticle() {

  private lateinit var client: MongoClient

  val fileStore = ConfigStoreOptions()
    .setType("file")
    .setFormat("json")
    .setConfig(JsonObject().put("path", "config.json"))


  private fun storeMessage(message: Message<JsonObject>) {
    val data = message.body().getString("data")
    val contact = Contacts.getContacts().random()

    val doc = JsonObject().put("message", data).put("dateTime", Instant.now(Clock.systemUTC()))
      .put("contactId", contact.id)
    client.save("messages", doc) { res ->
      if (res.succeeded()) {
        message.reply(res.result())
      } else {
        message.fail(1, "Could not store message")
      }
    }
  }

  private fun listMessages(message: Message<JsonObject>) {
    val query = JsonObject()
    client.find("messages", query) { response ->
      if (response.succeeded()) {
        println(response.result())
        val myMessages = JsonArray(response.result())
        message.reply(myMessages)
      } else {
        message.fail(1, "No messages found")
      }
    }
  }

  override fun start(startPromise: Promise<Void>?) {
    val options = ConfigRetrieverOptions().addStore(fileStore)
    val retriever = ConfigRetriever.create(vertx, options)

    retriever.getConfig {
      if (it.succeeded()) {
        val config = it.result()
        val mongoDBConnectionString = config.getString("connection_string")

        client = MongoClient.create(
          vertx,
          jsonObjectOf("connection_string" to mongoDBConnectionString)
        )

        val eventBus = vertx.eventBus()
        val consumer: MessageConsumer<JsonObject> = eventBus.consumer("persistence-address")

        consumer.handler { message ->
          when (val action = message.body().getString("action")) {
            "store-message" -> storeMessage(message)
            "list-messages" -> listMessages(message)
            else -> {
              message.fail(1, "unknown action :$action")
            }
          }
        }

        startPromise?.complete()
      } else {
        println(it.cause())
        startPromise?.fail(it.cause())
      }
    }


  }
}
