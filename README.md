# play-json-union-formatter

A library for deriving a play-json formatter for a JSON-encoded [tagged union](https://en.wikipedia.org/wiki/Tagged_union).

The library is designed as a dependency for API Platform services and is subject to significant change without notice.

## Adding to your build

In your SBT build add:

```scala
libraryDependencies += "uk.gov.hmrc" %% "play-json-union-formatter" % "x.x.x"
```

## Versioning
Versions 2.0.0 and above require Scala 3 and support Play 3.

Versions 1.x.x support Scala 2

If your microservice is using Play 2.5, use version 1.7.0 or older. Versions > 1.7.0 only support Play 2.6 and above.  Versions > 1.15 support 2.8 and above.

1.15.0 and earlier support scala 2.12
1.16.0 supports Scala 2.13
1.17.0 and above are cross compiled for scala 2.12 and 2.13
1.22.0 is the final version that supports Scala 2

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
    