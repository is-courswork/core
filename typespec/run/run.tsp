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
  @pattern("[\\s\\S]*\\S[\\s\\S]*")
  @minLength(1)
  @maxLength(120)
  name: string;

  @maxLength(2000)
  description?: string;
}

@doc("Only supplied fields are updated. A null description clears it; name cannot be null.")
model UpdateRunRequest {
  @minLength(1)
  @maxLength(120)
  @pattern("[\\s\\S]*\\S[\\s\\S]*")
  name?: string;

  @maxLength(2000)
  description?: string | null;
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
  @get
  @route("/all")
  @operationId("getAllRuns")
  @doc("All runs, newest first. No pagination.")
  all(): RunResponse[];

  @post
  @operationId("createRun")
  create(@body request: CreateRunRequest): {
    @statusCode _: 201;
    @body body: RunResponse;
  } | BadRequestError;

  @get
  @operationId("getRun")
  get(@path runId: uuid): RunResponse | NotFoundError;

  @patch
  @operationId("updateRun")
  update(@path runId: uuid, @body request: UpdateRunRequest): RunResponse | BadRequestError | NotFoundError;

  @delete
  @operationId("deleteRun")
  delete(@path runId: uuid): { @statusCode _: 204; } | NotFoundError;
}
