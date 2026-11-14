import "@typespec/http";
import "@typespec/openapi";

import "../common/errors.tsp";
import "../common/types.tsp";

using Http;
using OpenAPI;

namespace Airlines.Core;

enum AccountOwnerType {
  AIRLINE,
  PASSENGER,
}

model AccountResponse {
  id: uuid;
  runId: uuid;
  ownerType: AccountOwnerType;

  /** Текущий баланс в рублях, начальное значение 0. */
  balance: decimal;
}

@route("/account")
@tag("Account")
interface Account {
  @get
  @operationId("getAccount")
  get(@path accountId: uuid): AccountResponse | NotFoundError;
}
