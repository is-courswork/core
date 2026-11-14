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

/** Действие недопустимо в текущем состоянии объекта, например возврат уже возвращённого билета. */
@error
model ConflictError {
  @statusCode _: 409;
  @body body: ErrorResponse;
}
