/*
 * Copyright 2018 Comcast Cable Communications Management, LLC
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

package vinyldns.api.domain.batch

import vinyldns.core.domain.DomainHelpers.ensureTrailingDot
import vinyldns.core.domain.record.RecordData
import vinyldns.core.domain.record.RecordType._

final case class BatchChangeInput(
    comments: Option[String],
    changes: List[ChangeInput],
    ownerGroupId: Option[String] = None)

sealed trait ChangeInput {
  val inputName: String
  val typ: RecordType
}

final case class AddChangeInput(inputName: String, typ: RecordType, ttl: Long, record: RecordData)
    extends ChangeInput

final case class DeleteChangeInput(inputName: String, typ: RecordType) extends ChangeInput

object AddChangeInput {
  def apply(inputName: String, typ: RecordType, ttl: Long, record: RecordData): AddChangeInput = {
    val transformName = typ match {
      case PTR => inputName
      case _ => ensureTrailingDot(inputName)
    }
    new AddChangeInput(transformName, typ, ttl, record)
  }
}

object DeleteChangeInput {
  def apply(inputName: String, typ: RecordType): DeleteChangeInput = {
    val transformName = typ match {
      case PTR => inputName
      case _ => ensureTrailingDot(inputName)
    }
    new DeleteChangeInput(transformName, typ)
  }
}

object ChangeInputType extends Enumeration {
  type ChangeInputType = Value
  val Add, DeleteRecordSet = Value
}
