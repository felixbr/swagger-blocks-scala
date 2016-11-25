import swaggerblocks._
import swaggerblocks.Implicits._

// format: off

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
    operations = List(
      operation(GET)(
        description = "Returns a list of pet objects",

        tags = List("pet"),

        parameters = List(
          queryParameter(
            name = "tags",
            required = false,
            schema = parameter.manyOf(t.string),
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

  lazy val feedingPath = swaggerPath("/feedingtimes")(
    operations = List(
      operation(GET)(
        parameters = List(
          bodyParameter(
            name = "Pets",
            required = true,
            schema = manyOf(t.string),
            description = "List of pets you want to feed",
            enum = List("cat", "bird")
          ),
          queryParameter(
            name = "filter",
            schema = t.string,
            required = false
          )
        ),

        responses = List(
          response(200)(
            schema = feedingPetList
          )
        )
      )
    )
  )

  lazy val feedingPetList = swaggerSchema("FeedingPetList")(
    property("petsToFeed")(
      schema = manyOf(feedingPetListItem),
      description = "Pets to feed"
    )
  )

  lazy val feedingPetListItem = swaggerSchema("FeedingPetListItem")(
    property("id")(
      schema = t.integer
    ),
    property("pet")(
      schema = petSchema,
      description = "Pet that has to be fed"
    ),
    property("foodBrand")(
      schema = t.string,
      description = "Brand the pet likes",
      enum = List("a", "b", "c")
    )
  )

  lazy val foodSchema = swaggerSchema("Food")(
    property("name")(
      schema = t.string
    ),

    property("likedBy")(
      schema = manyOf(petSchema)
    )
  )

  import rendering.render
  println(render(
    petstoreRoot,
    List(petsPath),
    List(petSchema)
  ))
}

object rendering {
  import internal.models._

  def render(root: ApiRoot, paths: Seq[ApiPathDefinition], schemata: Seq[ApiSchemaDefinition]): String = {
    s"""
       |$root
       |$paths
       |$schemata
     """.stripMargin
  }
}
