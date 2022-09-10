package nl.infowijs.codeassignment.controllers

import io.vertx.core.Handler
import io.vertx.core.json.JsonArray
import io.vertx.ext.web.RoutingContext
import nl.infowijs.codeassignment.data.Contacts
import nl.infowijs.codeassignment.modules.WebResponse

class ContactsController {
  fun listContacts(routingContext: RoutingContext) {
    val contactsObject = Contacts.getContacts().map { it.toJsonObject() }
    val contacts = JsonArray.of(contactsObject)
    WebResponse(routingContext).end(contacts)
  }
}
