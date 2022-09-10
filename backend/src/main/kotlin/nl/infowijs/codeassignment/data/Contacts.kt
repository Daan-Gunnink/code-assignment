package nl.infowijs.codeassignment.data

import nl.infowijs.codeassignment.models.Person
import java.util.UUID

class Contacts() {
  companion object {
    fun getContacts(): List<Person> {
      return listOf(
        Person(
          id = UUID.randomUUID().toString(),
          name = "Hans van Maanen",
          avatar = "https://randomuser.me/api/portraits/men/12.jpg",
          role = "Placeholder",
          email = "hans@email.com"
        ),
        Person(
          UUID.randomUUID().toString(),
          name = "Belinda de Vries",
          avatar = "https://randomuser.me/api/portraits/women/12.jpg",
          role = "Placeholder",
          phone = "+31612345678"
        ),
        Person(
          UUID.randomUUID().toString(),
          name = "Gerrit Groot",
          avatar = "https://randomuser.me/api/portraits/men/24.jpg",
          role = "Placeholder",
          email = "gerritemail.com",
          phone = "06 - 00 000 000"
        ),
        Person(
          UUID.randomUUID().toString(),
          name = "Monique Willems",
          avatar = "https://randomuser.me/api/portraits/women/24.jpg",
          role = "Placeholder",
          email = "monique@email.com"
        ),
      )
    }
  }
}
