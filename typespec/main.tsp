import "@typespec/http";
import "@typespec/openapi";

import "./common/types.tsp";
import "./common/errors.tsp";
import "./run/run.tsp";
import "./airport/airport.tsp";
import "./booking/flight.tsp";
import "./booking/passenger.tsp";
import "./booking/account.tsp";
import "./booking/ticket.tsp";

using Http;
using OpenAPI;

@service(#{ title: "Airlines Core API" })
@info(#{ version: "0.1.0" })
@server("/api/core")
namespace Airlines.Core;
