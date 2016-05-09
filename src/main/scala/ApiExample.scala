import swaggerblocks._

object ApiExample extends App {
  swaggerRoot("2.0") { r =>
    r.host = "petstore.swagger.wordnik.com"
    r.basePath = "/api"
    r.consumes = List("application/json")
    r.produces = List("application/json")

    r.info { i =>
      i.version = "1.0.0"
      i.title = "Swagger Petstore"
      i.description = "A sample API that uses a petstore as an example"

      i.contact { c =>
        c.name = "Wordnik API Team"
      }

      i.license { l =>
        l.name = "MIT"
      }
    }
  }

  swaggerPath("/pets") { p =>
    p.operation(GET) { o =>
      o.description = "Bla"

      o.parameter { p =>
        p.name = "tags"
        p.in = "query"
        p.description = "tags to filter by"
        p.required = "false"

        // TODO items, schema
      }

      o.response(200) { r =>
        // TODO
      }
    }
  }

  swaggerSchema("Pet") { s =>
    s.property("id") { p =>
      p.typ = "integer"
    }

    s.property("name") { p =>
      p.typ = "string"
      p.description = "Name of the pet"
    }

    s.property("tag", required = false) { p =>
      p.typ = "string"
    }
  }
}
