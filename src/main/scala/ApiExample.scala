import swaggerblocks._
import swaggerblocks.Implicits.stringToSomeString
import internal.rendering._

object ApiExample extends App {

  lazy val petstoreRoot = swaggerRoot("2.0")(
    host = "petstore.swagger.wordnik.com",
    basePath = "/api",

    info(
      version = "1.0.0",
      title = "Swagger Petstore",
      description = "A sample API that uses a petstore as an example",

      contact = contact(
        name = "Wordnik API Team"
      ),

      license = license(
        name = "MIT"
      )
    )
  )

  lazy val petsPath = swaggerPath("/pets")(
    description = "The pets route",

    operations = List(
      operation(GET)(
        description = "Returns a list of pet objects",

        parameters = List(
          parameter(
            name = "tags",
            in = "query",
            required = false,
            description = "tags to filter by",
            schema = None
          )
        ),

        responses = List(
          response(200)(
            description = "pet response",
            schema = valueOf(s.String)
          )
        )
      )
    )
  )

  lazy val petSchema = swaggerSchema("Pet")(
    property("id")(
      typ = s.Int
    ),

    property("name")(
      typ = s.String,
      description = "Name of the pet"
    ),

    property("tag")(
      typ = s.String,
      required = false
    )
  )

  println(render(
    petstoreRoot,
    List(petsPath),
    List(petSchema)
  ))
}
