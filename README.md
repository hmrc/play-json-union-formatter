# play-json-union-formatter

[ ![Download](https://api.bintray.com/packages/hmrc/releases/play-json-union-formatter/images/download.svg) ](https://bintray.com/hmrc/releases/play-json-union-formatter/_latestVersion)

A library for deriving a play-json formatter for a JSON-encoded [tagged union](https://en.wikipedia.org/wiki/Tagged_union).

## Adding to your build

In your SBT build add:

```scala
resolvers += Resolver.bintrayRepo("hmrc", "releases")

libraryDependencies += "uk.gov.hmrc" %% "play-json-union-formatter" % "x.x.x"
```

If your microservice is using Play 2.5, use version 1.7.0 or older. Versions > 1.7.0 only support Play 2.6 and above.

## Usage

In a given JSON payload, you may want to capture a field or array that contains a set of hetrogeneous types. 

You might have created an object recognition system that recognises two types of object - a `car` and a `pet`:

```json
{
  "matchType": "car",
  "fuelType": "petrol"
}
```

```json
{
  "matchType": "pet",
  "species": "cat"
}
```

The natural way to represent this in scala is: 

```scala
sealed trait Match

case class Pet(species: String) extends Match
case class Car(fuelType: String) extends Match
```

`play-json-union-formatter` provides a way to derive a play formatter (compatible with play 2.3.x) for the above. 

```scala

import play.api.libs.json.Json
import uk.gov.hmrc.play.json.Union

object Match { 
  implicit val formatPet = Json.format[Pet]
  implicit val formatCar = Json.format[Car]
  implicit val format = Union.from[Match](typeField = "matchType")
    .and[Pet](typeTag = "pet")
    .and[Car](typeTag = "car")
    .format
}
```

## License

This code is open source software licensed under the [Apache 2.0 License]("http://www.apache.org/licenses/LICENSE-2.0.html").
    