/*
 * Copyright 2020 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.gov.hmrc.play.json

import play.api.libs.json._
import scala.reflect.ClassTag

class Union[A](typeField: String, readWith: PartialFunction[String, JsValue => JsResult[A]], writeWith: PartialFunction[A, JsValue]) {

  def and[B](typeTag: String)(implicit ev: B <:< A, ct: ClassTag[B], f: Format[B]) = {
    val readCase: PartialFunction[String, JsValue => JsResult[A]] = {
      case `typeTag` => jsValue: JsValue => Json.fromJson(jsValue)(f).asInstanceOf[JsResult[A]]
    }

    val writeCase: PartialFunction[A, JsValue] = {
      case value: B => Json.toJson(value)(f).as[JsObject] ++ JsObject(Seq(typeField -> JsString(typeTag)))
    }

    new Union(typeField, readWith.orElse(readCase), writeWith.orElse(writeCase))
  }

  private val defaultReads: PartialFunction[String, JsValue => JsResult[A]] = {
    case attemptedType => jsValue => JsError(__ \ typeField, s"$attemptedType is not a recognised $typeField")
  }

  def format: Format[A] = {
    val reads = Reads[A] { json =>
      (json \ typeField).validate[String].flatMap { typeTag =>
        readWith.orElse(defaultReads)(typeTag)(json)
      }
    }
    val writes = Writes[A](writeWith)
    Format(reads, writes)
  }
}

object Union {
  def from[A](typeField: String) = new Union[A](typeField, PartialFunction.empty, PartialFunction.empty)
}
