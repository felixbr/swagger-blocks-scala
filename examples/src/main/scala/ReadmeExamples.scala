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

    import javax.inject._
    import play.api.mvc._

    import swaggerblocks._
    import swaggerblocks.Implicits._
    import swaggerblocks.rendering.playJson.renderPretty

    @Singleton
    class SwaggerController @Inject()() extends Controller {

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

      def json = Action {
        val swaggerJson = renderPretty(petstoreRoot, paths, schemas)

        Ok(swaggerJson)
      }
    }

    // format: on
  }

  object exampleExtensions {
    // format off

    import swaggerblocks._
    import swaggerblocks.Implicits._
    import swaggerblocks.extensions.playJson.ExampleExtension

    import play.api.libs.json._

    lazy val schemaWithExample = swaggerSchema("SchemaWithExample")(
      property("id")(
        schema = t.integer
      ),
      property("name")(
        schema = t.string
      )
    ).withExample(
      Json.obj(
        "id"   -> 123,
        "name" -> "Bello"
      )
    )

    // you can of course use all features provided by the json/yaml lib like
    // case class serialization

    case class Dog(id: Int, name: String)
    implicit val dogFormat = Json.format[Dog]

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
