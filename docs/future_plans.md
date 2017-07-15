# Future Plans

This document outlines a few ideas and plans I have for this library.

## Current maintenance status

I've switched companies and no longer have a need for this library personally. As I don't 
know of other people using this yet, it is best to lay it to rest now than to leave it in an 
unmaintained state when people are already using it, even if I feel sad about it.

## Migrate to OpenAPI-Specification 3.0 (aka Swagger 3.0)

I haven't looked too long at the new version, but it has some new stuff and might even clean up 
some things that bother me about the 2.0 version. In the long run this will be the new standard 
so I want to migrate towards this as soon as it is finalized and a swagger-ui is available for it.

My hope is that the new version is slightly easier to describe in a type-safe way, because 2.0 
has lots of cases that are fine in a dynamic or ill-typed language (e.g. Ruby or Java), but you 
wouldn't accept in something like Scala.

## Only use circe (and circe-yaml) as json dependency

After using circe, play-circe and circe-yaml in a production environment, I have to say that is 
miles ahead of both play-json and spray-json. It has a clearer api-surface, less boilerplate, is 
arguably faster, safer and quicker to adopt new compiler versions (play still is 2.11-only as of 
2017-04-07, which is ridiculous considering that 2.12 was released on 2016-11-03).

All in all this not only makes maintenance and development easier, it also makes it possible to use 
circe typeclasses like Decoder in the DSL to constrain things like "example" or "default". This in 
turn should make the DSL easier to understand (less implicit conversion magic) while enabling 
case class hierarchies (anything that circe and circe-generic give you!) in a type-safe way!

Also the main reason to support so many json libraries is that you don't have to include play-json 
in a spray-json project or vice-versa, but I adding circe if you don't have it already isn't too 
bad anyway (and you should really give it a try imo!).

## Do a clean rewrite/refactoring for 1.0

I've tried a lot of different things and as mentioned above found many pitfalls in the swagger 2.0 
spec that cannot easily be represented in a reasonably type-safe manner. I think that by now I have 
a solid understanding of the problem space (minus the additions of 3.0), so I should probably sit 
down and write down a clean-room version of the DSL and how it has to look like for 1.0