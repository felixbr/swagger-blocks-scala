# swagger-blocks-scala [![Build Status](https://travis-ci.org/felixbr/swagger-blocks-scala.svg?branch=master)](https://travis-ci.org/felixbr/swagger-blocks-scala)

A library to express swagger specifications using a Scala DSL.

It is inspired mainly by [fotinakis/swagger-blocks](https://github.com/fotinakis/swagger-blocks) 
for ruby.

Currently this only supports a part of the full swagger-spec

## Goals

* Express swagger specifications in a reasonably type-safe DSL.
* Don't clutter your models and logic with annotations
* Provide bindings for common json-libs
* Avoid unnecessary magic (e.g. reflection)
* Be reasonable to use in IDEs (e.g. IntelliJ)

## Getting Started

### Dependencies

There is a core modul available for the DSL and core data types:

    "io.github.felixbr" %% "swagger-blocks-scala" % "0.1.0-SNAPSHOT"

The plan is to provide bindings for the most popular json libs, but right now 
only play-json is supported. The json-extensions also include the core lib, so 
you don't need to specify both:

#### play-json (2.5.4)

    "io.github.felixbr" %% "swagger-blocks-play" % "0.1.0-SNAPSHOT"
    
### Writing Swagger Path and Schema specifications
    
Write a specification for your endpoint (e.g. in your controller's companion 
object):

```Scala
object PetsController {
  import swaggerblocks._
  import swaggerblocks.Implicits.stringToSomeString

  lazy val path = swaggerPath("/pets")(
    operations = List(
      operation(GET)(
        description = "Returns a list of pet objects",
  
        tags = List("pet"),
  
        parameters = List(
          parameter(
            name = "tags",
            in = Query,
            required = false,
            typ = s.String,
            description = "tags to filter by",
            schema = None
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
}
```

**Note:** The import `import swaggerblocks.Implicits.stringToSomeString` is 
just convenience which turns the `String` literals into a `Option[String]` for 
you.


Create a new endpoint which serves the json needed for the swagger-ui. I 
recommend to use the same controller which serves the ui. You also have to 
write the required root metadata for swagger:

```Scala
import javax.inject._
import play.api.mvc._

import swaggerblocks._
import swaggerblocks.rendering.playJson._

@Singleton
class SwaggerController @Inject() extends Controller {

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
    HomeController.path
  )
  val schemas = List(
    HomeController.petSchema
  )

  def json = Action {
    val swaggerJson = renderPretty(root, paths, schemas)

    Ok(swaggerJson)
  }
}
```

Now you only have to include the new action in your `routes` file and point 
a swagger-ui (possibly rendered by a standard html-view) at the url.


## Caveats

While for the most part the library API tries to stick to the swagger spec, 
there are some exceptions made either for technical reasons, type-safety or 
usability:

* Since `type` is a restricted keyword you have to use `typ` in its place
* Many values in swagger can either be a primitive type, an object or a list of 
values depending on the corresponding `type`. In order to make this more type-safe 
and usable, there are `oneOf` and `manyOf` helpers, which accept primitive types 
or schemas.
* Types are currently namespaced by `s` (e.g. `s.String`, `s.Int`, `s.Number`)
