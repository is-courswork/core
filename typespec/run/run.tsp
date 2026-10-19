import "@typespec/http";
import "@typespec/openapi";

import "../common/errors.tsp";
import "../common/types.tsp";

using Http;
using OpenAPI;

namespace Airlines.Core;

enum RunStatus {
  DRAFT,
  RUNNING,
  PAUSED,
  FINISHED,
  CANCELLED,
  FAILED,
}

model CreateRunRequest {
  @minLength(1)
  @maxLength(255)
  name: string;

  @maxLength(10000)
  description?: string;
}

model RunResponse {
  id: uuid;
  name: string;
  description?: string;
  status: RunStatus;
  createdAt: utcDateTime;
  startedAt?: utcDateTime;
  finishedAt?: utcDateTime;
}

@route("/run")
@tag("Run")
interface Run {
  @post
  @operationId("createRun")
  create(@body request: CreateRunRequest): {
    @statusCode _: 201;
    @body body: RunResponse;
  } | BadRequestError;

  @get
  @operationId("getRun")
  get(@path runId: uuid): RunResponse | NotFoundError;
}
