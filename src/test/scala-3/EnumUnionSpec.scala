/*
 * Copyright 2026 HM Revenue & Customs
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

import org.scalatest.OptionValues
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

import play.api.libs.json.*

import uk.gov.hmrc.play.json.Union

class EnumUnionSpec extends AnyWordSpec with Matchers with OptionValues {

  enum AnEnum {
    case Enum1, Enum2
    case Enum3(x: String)
  }

  object AnEnum {
    import play.api.libs.json.*

    // An enum with arguments needs a normal OFormat for an 'object'
    given OFormat[Enum3] = Json.format[Enum3]

    given OFormat[AnEnum] = Union.from[AnEnum]("typeField")
      .andValue("descriminator1", Enum1)
      .andValue("descriminator2", Enum2)
      .and[Enum3]("descriminator3")
      .format
  }

  "serialise a member of the enum type with the correct type field" in {
    Json.toJson[AnEnum](AnEnum.Enum1) \ "typeField" shouldBe JsDefined(JsString("descriminator1"))
    Json.toJson[AnEnum](AnEnum.Enum2) \ "typeField" shouldBe JsDefined(JsString("descriminator2"))
    Json.toJson[AnEnum](AnEnum.Enum3("abc")) \ "typeField" shouldBe JsDefined(JsString("descriminator3"))
    Json.toJson[AnEnum](AnEnum.Enum3("abc")) \ "x" shouldBe JsDefined(JsString("abc"))
  }

  "deserialise an enum from json" in {
    val json = Json.parse(
      """
        |{
        |  "typeField": "descriminator2"
        |}
        """.stripMargin
    )

    json.asOpt[AnEnum].value shouldBe AnEnum.Enum2

    val json2 = Json.parse(
      """
        |{
        |  "typeField": "descriminator3",
        |  "x": "text here"
        |
        |}
        """.stripMargin
    )

    json2.asOpt[AnEnum].value shouldBe AnEnum.Enum3("text here")
  }

  "deserialise handles and error in the json" in {
    val json = Json.parse(
      """
        |{
        |  "typeField": "unknown"
        |}
      """.stripMargin
    )

    Json.fromJson[AnEnum](json) shouldBe JsError(__ \ "typeField", "unknown is not a recognised typeField")
  }

}
