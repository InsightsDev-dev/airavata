/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * Autogenerated by Thrift Compiler (0.9.3)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
#ifndef airavata_commons_TYPES_H
#define airavata_commons_TYPES_H

#include <iosfwd>

#include <thrift/Thrift.h>
#include <thrift/TApplicationException.h>
#include <thrift/protocol/TProtocol.h>
#include <thrift/transport/TTransport.h>

#include <thrift/cxxfunctional.h>


namespace apache { namespace airavata { namespace model { namespace commons {

class ErrorModel;

class ValidatorResult;

class ValidationResults;

typedef struct _ErrorModel__isset {
  _ErrorModel__isset() : creationTime(false), actualErrorMessage(false), userFriendlyMessage(false), transientOrPersistent(true), rootCauseErrorIdList(false) {}
  bool creationTime :1;
  bool actualErrorMessage :1;
  bool userFriendlyMessage :1;
  bool transientOrPersistent :1;
  bool rootCauseErrorIdList :1;
} _ErrorModel__isset;

class ErrorModel {
 public:

  ErrorModel(const ErrorModel&);
  ErrorModel& operator=(const ErrorModel&);
  ErrorModel() : errorId("DO_NOT_SET_AT_CLIENTS"), creationTime(0), actualErrorMessage(), userFriendlyMessage(), transientOrPersistent(false) {
  }

  virtual ~ErrorModel() throw();
  std::string errorId;
  int64_t creationTime;
  std::string actualErrorMessage;
  std::string userFriendlyMessage;
  bool transientOrPersistent;
  std::vector<std::string>  rootCauseErrorIdList;

  _ErrorModel__isset __isset;

  void __set_errorId(const std::string& val);

  void __set_creationTime(const int64_t val);

  void __set_actualErrorMessage(const std::string& val);

  void __set_userFriendlyMessage(const std::string& val);

  void __set_transientOrPersistent(const bool val);

  void __set_rootCauseErrorIdList(const std::vector<std::string> & val);

  bool operator == (const ErrorModel & rhs) const
  {
    if (!(errorId == rhs.errorId))
      return false;
    if (__isset.creationTime != rhs.__isset.creationTime)
      return false;
    else if (__isset.creationTime && !(creationTime == rhs.creationTime))
      return false;
    if (__isset.actualErrorMessage != rhs.__isset.actualErrorMessage)
      return false;
    else if (__isset.actualErrorMessage && !(actualErrorMessage == rhs.actualErrorMessage))
      return false;
    if (__isset.userFriendlyMessage != rhs.__isset.userFriendlyMessage)
      return false;
    else if (__isset.userFriendlyMessage && !(userFriendlyMessage == rhs.userFriendlyMessage))
      return false;
    if (__isset.transientOrPersistent != rhs.__isset.transientOrPersistent)
      return false;
    else if (__isset.transientOrPersistent && !(transientOrPersistent == rhs.transientOrPersistent))
      return false;
    if (__isset.rootCauseErrorIdList != rhs.__isset.rootCauseErrorIdList)
      return false;
    else if (__isset.rootCauseErrorIdList && !(rootCauseErrorIdList == rhs.rootCauseErrorIdList))
      return false;
    return true;
  }
  bool operator != (const ErrorModel &rhs) const {
    return !(*this == rhs);
  }

  bool operator < (const ErrorModel & ) const;

  uint32_t read(::apache::thrift::protocol::TProtocol* iprot);
  uint32_t write(::apache::thrift::protocol::TProtocol* oprot) const;

  virtual void printTo(std::ostream& out) const;
};

void swap(ErrorModel &a, ErrorModel &b);

inline std::ostream& operator<<(std::ostream& out, const ErrorModel& obj)
{
  obj.printTo(out);
  return out;
}

typedef struct _ValidatorResult__isset {
  _ValidatorResult__isset() : errorDetails(false) {}
  bool errorDetails :1;
} _ValidatorResult__isset;

class ValidatorResult {
 public:

  ValidatorResult(const ValidatorResult&);
  ValidatorResult& operator=(const ValidatorResult&);
  ValidatorResult() : result(0), errorDetails() {
  }

  virtual ~ValidatorResult() throw();
  bool result;
  std::string errorDetails;

  _ValidatorResult__isset __isset;

  void __set_result(const bool val);

  void __set_errorDetails(const std::string& val);

  bool operator == (const ValidatorResult & rhs) const
  {
    if (!(result == rhs.result))
      return false;
    if (__isset.errorDetails != rhs.__isset.errorDetails)
      return false;
    else if (__isset.errorDetails && !(errorDetails == rhs.errorDetails))
      return false;
    return true;
  }
  bool operator != (const ValidatorResult &rhs) const {
    return !(*this == rhs);
  }

  bool operator < (const ValidatorResult & ) const;

  uint32_t read(::apache::thrift::protocol::TProtocol* iprot);
  uint32_t write(::apache::thrift::protocol::TProtocol* oprot) const;

  virtual void printTo(std::ostream& out) const;
};

void swap(ValidatorResult &a, ValidatorResult &b);

inline std::ostream& operator<<(std::ostream& out, const ValidatorResult& obj)
{
  obj.printTo(out);
  return out;
}


class ValidationResults {
 public:

  ValidationResults(const ValidationResults&);
  ValidationResults& operator=(const ValidationResults&);
  ValidationResults() : validationState(0) {
  }

  virtual ~ValidationResults() throw();
  bool validationState;
  std::vector<ValidatorResult>  validationResultList;

  void __set_validationState(const bool val);

  void __set_validationResultList(const std::vector<ValidatorResult> & val);

  bool operator == (const ValidationResults & rhs) const
  {
    if (!(validationState == rhs.validationState))
      return false;
    if (!(validationResultList == rhs.validationResultList))
      return false;
    return true;
  }
  bool operator != (const ValidationResults &rhs) const {
    return !(*this == rhs);
  }

  bool operator < (const ValidationResults & ) const;

  uint32_t read(::apache::thrift::protocol::TProtocol* iprot);
  uint32_t write(::apache::thrift::protocol::TProtocol* oprot) const;

  virtual void printTo(std::ostream& out) const;
};

void swap(ValidationResults &a, ValidationResults &b);

inline std::ostream& operator<<(std::ostream& out, const ValidationResults& obj)
{
  obj.printTo(out);
  return out;
}

}}}} // namespace

#endif