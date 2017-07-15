object ReadmeExamples {

  object pathsAndSchema {
    // format: off
    import swaggerblocks._
    import swaggerblocks.Implicits._

    object PetsController {

      lazy val petListPath = swaggerPath("/pets")(
        operations = List(
          operation(GET)(
            description = "Returns a list of pet objects",
            summary = "Returns a list of pet objects",

            tags = List("pet"),

            parameters = List(
              queryParameter(
                name = "tag",
                required = false,
                schema = t.string,
                description = "tag to filter by"
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

      // format: on
    }

  }

  object swaggerController {
    import ReadmeExamples.pathsAndSchema.PetsController

    // format: off

    import swaggerblocks._
    import swaggerblocks.Implicits._
    import swaggerblocks.rendering.json.renderPretty

    // could be play or some other framework
    class SwaggerController {

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

      val paths = List(
        PetsController.petListPath
      )
      val schemas = List(
        PetsController.petSchema
      )

      def json = {
        val swaggerJson: String = renderPretty(petstoreRoot, paths, schemas)

        // response with swaggerJson as content
      }
    }
    // format: on
  }

  object exampleExtensions {
    // format off

    import swaggerblocks._
    import swaggerblocks.Implicits._
    import swaggerblocks.extensions.json.ExampleExtension

    import io.circe.syntax._
    import io.circe.generic.auto._

    lazy val schemaWithExample = swaggerSchema("SchemaWithExample")(
      property("id")(
        schema = t.integer
      ),
      property("name")(
        schema = t.string
      )
    ).withExample(
      Map(
        "id"   -> 123.asJson, // if the values in a map are not uniform, you have to be
        "name" -> "Bello".asJson // explicit about it being serializable (or use a case class)
      )
    )

    // you can of course use all features provided by circe like
    // case class serialization

    case class Dog(id: Int, name: String)

    lazy val schemaWithCaseClassExample = swaggerSchema("SchemaWithCaseClassExample")(
      property("id")(
        schema = t.integer
      ),
      property("name")(
        schema = t.string
      )
    ).withExample(
      Dog(123, "Fiffi")
    )
  }

}
