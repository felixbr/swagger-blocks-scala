package fixtures

import swaggerblocks._
import swaggerblocks.Implicits._

object PetstoreAPI {

  lazy val petstoreRoot = swaggerRoot("2.0")(
    host = "petstore.swagger.wordnik.com",
    basePath = "/api",

    info(
      version = "1.0.0",
      title = "Swagger Petstore",
      description = "A sample API that uses a petstore as an example",

      contact = contact(
        name = "Wordnik API Team",
        email = "foo@example.com",
        url = "http://madskristensen.net"
      ),

      license = license(
        name = "MIT",
        url = "http://github.com/gruntjs/grunt/blob/master/LICENSE-MIT"
      )
    ),

    externalDocs(
      description = "Find more info here",
      url = "https://swagger.io"
    )
  )

  lazy val path = swaggerPath("/pets")(
    operations = List(
      operation(GET)(
        description = "Returns a list of pet objects",

        tags = List("pet"),

        parameters = List(
          queryParameter(
            name = "tags",
            required = false,
            schema = parameter.oneOf(t.string),
            description = "tags to filter by"
          )
        ),

        responses = List(
          response(200)(
            description = "pet response",
            schema = manyOf(petSchema)
          )
        )
      )
    )
  )

  lazy val petSchema = swaggerSchema("Pet")(
    property("id")(
      schema = t.integer
    ),

    property("name")(
      schema = t.string,
      description = "Name of the pet"
    ),

    property("tag")(
      schema = t.string,
      required = false
    )
  )

  lazy val paths = List(path)
  lazy val schemas = List(petSchema)

}
