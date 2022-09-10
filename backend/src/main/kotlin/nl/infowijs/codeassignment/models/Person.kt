package nl.infowijs.codeassignment.models

import io.vertx.core.json.JsonObject

data class Person(val id: String,val name: String, val avatar: String,val role: String? = null, val email: String? = null, val phone: String? = null) {
  fun toJsonObject(): JsonObject {
    return JsonObject()
      .put("id", id)
      .put("name", name)
      .put("avatar", avatar)
      .put("phone", phone)
      .put("role", role)
      .put("email", email)
  }
}
