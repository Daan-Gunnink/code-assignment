package nl.infowijs.codeassignment.models

import io.vertx.core.json.JsonObject
import java.time.Instant

data class MyMessage(val id: String, val message: String, val datetime: Instant, val contactId: String) {
  fun toJsonObject(): JsonObject {
    return JsonObject()
      .put("id", id)
      .put("message", message)
      .put("datetime", datetime)
      .put("contactId", contactId)
  }
}
