/*
 * Copyright © 2011-2015 the spray project <http://spray.io>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package spray.http
package parser

import org.parboiled.scala._
import BasicRules._
import LanguageRanges._

private[parser] trait AcceptLanguageHeader {
  this: Parser with ProtocolParameterRules ⇒

  def `*Accept-Language` = rule(
    oneOrMore(LanguageRangeDef, separator = ListSep) ~ EOI ~~> (HttpHeaders.`Accept-Language`(_)))

  def LanguageRangeDef = rule {
    (LanguageTag | "*" ~ push(`*`)) ~ optional(LanguageQuality) ~~> { (range, optQ) ⇒
      optQ match {
        case None    ⇒ range
        case Some(q) ⇒ range withQValue q
      }
    }
  }

  def LanguageQuality = rule {
    ";" ~ "q" ~ "=" ~ QValue
  }

}