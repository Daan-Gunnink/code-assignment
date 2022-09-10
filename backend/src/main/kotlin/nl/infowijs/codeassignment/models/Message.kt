package nl.infowijs.codeassignment.models

import io.vertx.core.json.JsonObject
import java.time.Instant

data class Message(val message: String, val datetime: Instant, val person: Person) {
  fun toJsonObject(): JsonObject {
    return JsonObject()
      .put("message", message)
      .put("datetime", datetime)
      .put("person", person.toJsonObject())
  }
}
