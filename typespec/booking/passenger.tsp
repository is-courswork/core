import "@typespec/http";
import "@typespec/openapi";

import "../common/errors.tsp";
import "../common/types.tsp";

using Http;
using OpenAPI;

namespace Airlines.Core;

model CreatePassengerRequest {
  runId: uuid;

  @minLength(1)
  @maxLength(255)
  fullName: string;
}

model PassengerResponse {
  id: uuid;
  runId: uuid;
  fullName: string;
  accountId: uuid;
}

@route("/passenger")
@tag("Passenger")
interface Passenger {
  /** Создаёт пассажира прогона. Счёт пассажира создаётся автоматически. */
  @post
  @operationId("createPassenger")
  create(@body request: CreatePassengerRequest):
    | {
        @statusCode _: 201;
        @body body: PassengerResponse;
      }
    | BadRequestError
    | NotFoundError;

  @get
  @operationId("getPassenger")
  get(@path passengerId: uuid): PassengerResponse | NotFoundError;
}
