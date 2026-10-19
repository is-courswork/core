import "@typespec/http";

using Http;

namespace Airlines.Core;

@error
model ErrorResponse {
  code: string;
  message: string;
  fieldErrors?: FieldError[];
}

model FieldError {
  field: string;
  message: string;
}

@error
model BadRequestError {
  @statusCode _: 400;
  @body body: ErrorResponse;
}

@error
model NotFoundError {
  @statusCode _: 404;
  @body body: ErrorResponse;
}
