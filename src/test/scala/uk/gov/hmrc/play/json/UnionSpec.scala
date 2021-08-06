/*
 * Copyright 2021 HM Revenue & Customs
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

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import play.api.libs.json._


class UnionSpec extends AnyWordSpec with Matchers {

  sealed trait UnionType

  case class MemberOne(fieldOne: Int) extends UnionType

  object MemberOne {
    implicit val format = Json.format[MemberOne]
  }

  case class MemberTwo(fieldTwo: String) extends UnionType

  object MemberTwo {
    implicit val format = Json.format[MemberTwo]
  }

  implicit val format =
    Union.from[UnionType]("typeField")
      .and[MemberOne]("ONE")
      .and[MemberTwo]("TWO")
      .format

  "Union.format w/ toJson" should {

    "serialise a member of the union type with the correct type field" in {
      Json.toJson[UnionType](MemberOne(10)) \ "typeField" shouldBe JsDefined(JsString("ONE"))
      Json.toJson[UnionType](MemberTwo("10")) \ "typeField" shouldBe JsDefined(JsString("TWO"))
    }

    "serialise a member of the union type with the right fields" in {
      Json.toJson[UnionType](MemberOne(10)) \ "fieldOne" shouldBe JsDefined(JsNumber(10))
      Json.toJson[UnionType](MemberTwo("10")) \ "fieldTwo" shouldBe JsDefined(JsString("10"))
    }

  }

  "Union.format w/ fromJson" should {

    "parse union member types successfully" in {

      val jsonOne = Json.parse(
        """
          |{
          |  "typeField": "ONE",
          |  "fieldOne": 999
          |}
        """.stripMargin
      )

      val jsonTwo = Json.parse(
        """
          |{
          |  "typeField": "TWO",
          |  "fieldTwo": "String"
          |}
        """.stripMargin
      )

      Json.fromJson[UnionType](jsonOne).get shouldBe MemberOne(999)
      Json.fromJson[UnionType](jsonTwo).get shouldBe MemberTwo("String")
    }

    "refuse to parse a field without a typeField" in {
      val json = Json.parse(
        """
          |{
          |  "fieldTwo": "String"
          |}
        """.stripMargin
      )

      Json.fromJson[UnionType](json) shouldBe an[JsError]
    }

    "refuse to parse a typeField that isn't known" in {
      val json = Json.parse(
        """
          |{
          |  "typeField": "THREE"
          |}
        """.stripMargin
      )

      val typePath = __ \ "typeField"

      Json.fromJson[UnionType](json) shouldBe JsError(typePath, "THREE is not a recognised typeField")
    }
  }
}
