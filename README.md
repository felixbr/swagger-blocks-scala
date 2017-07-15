# swagger-blocks-scala [![Build Status](https://travis-ci.org/felixbr/swagger-blocks-scala.svg?branch=master)](https://travis-ci.org/felixbr/swagger-blocks-scala)

A library to express swagger specifications using a Scala DSL.

It is inspired mainly by [fotinakis/swagger-blocks](https://github.com/fotinakis/swagger-blocks) 
for ruby.

Currently this only supports a part of the full swagger-spec.

## Note about current maintenance status

I currently have no personal use for the library anymore, so don't expect frequent updates.

For 0.5.x onward the scope of the project was reduced, so this will hopefully help me
keep up with updating dependencies and the like.

## Goals

* Express swagger specifications in a reasonably type-safe DSL.
* Don't clutter models and logic with annotations
* Provide serialization for json and yaml using circe
* Avoid reflection
* Be reasonable to use in IDEs (e.g. IntelliJ)

## Getting Started

### Dependencies

The library is published on Sonatype for both `Scala 2.11.11` and `Scala 2.12.2`:
```scala
resolvers += Resolver.sonatypeRepo("releases")
```
There is a core modul available for the DSL and core data types:
```scala
"io.github.felixbr" %% "swagger-blocks-scala" % "0.4.0"
```
There are serializers for json and yaml. The extensions also include the core lib, so
you only need to specify the extension you want to use:

#### moultingyaml (0.4.0)
```scala
"io.github.felixbr" %% "swagger-blocks-yaml" % "0.4.0
```
#### circe (0.7.0)
```scala
"io.github.felixbr" %% "swagger-blocks-circe" % "0.4.0"
```
### Writing Swagger Path and Schema specifications
    
Write a specification for your endpoint (e.g. in your controller's companion 
object):

```Scala
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

```

### Rendering the output for swagger-ui

Create a new endpoint which serves the json needed for the swagger-ui. I 
recommend to use the same controller which serves the ui. You also have to 
write the required root metadata for swagger:

```Scala
import swaggerblocks._
import swaggerblocks.Implicits._
import swaggerblocks.rendering.circe.renderPretty

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
```

Now you only have to include the new action in your `routes` file and point 
a swagger-ui (possibly rendered by a standard html-view) at the url.

## Usage guide

### Providing a schema example

Since the example has to be embedded in the final json/yaml rendering this feature depends 
on the rendering library you want to use. The extension method is located at 
`swaggerblocks.extensions.<renderinglib>.ExampleExtension`. When is is imported, you can 
call `.withExample` on the schema definition like shown below:

```scala
import swaggerblocks._
import swaggerblocks.Implicits._
import swaggerblocks.extensions.circe.ExampleExtension

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
    "id"   -> 123.asJson,     // if the values in a map are not uniform, you have to be
    "name" -> "Bello".asJson  // explicit about it being serializable (or use a case class)
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
```

## Caveats

While for the most part the library API tries to stick to the swagger spec, 
there are some exceptions made either for technical reasons, type-safety or 
usability:

* Since `type` is a restricted keyword you'll have to use `schema` in its place. 
You can use primitive types (e.g. `schema = t.string`) as well as references to schemas 
(e.g. `schema = petSchema`) and it will be sorted out behind the scenes.
* Many values in swagger can either be a primitive type, an object or a list of 
the former two. In order to make this more type-safe 
and usable, there is a `manyOf` helper for lists, which accepts primitive types 
or schemas.
Because of type erasure you have to use `parameter.manyOf` instead of just `manyOf` for 
all non-body parameters.
* Types are currently namespaced by `t` (e.g. `t.string`, `t.integer`, `t.number`). 
Some types should only be used in certain places according to the swagger spec. 
For example `t.file` is not allowed in path-parameters, but currently this is not enforced. 
There are namespaces for the different parameters, so you can easily find the allowed 
ones using autocompletion (e.g. `t.parameter.path.string` or `t.parameter.formData.file`).
* The DSL uses a few implicit conversions to provide an easier and more uniform way to write things.
For example most optional text fields like `description` still accept a `String` literal 
due to an implicit `String => Option[String]`. The other conversions deal with differences 
in `schema` fields.
