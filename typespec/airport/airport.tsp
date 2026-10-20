import "@typespec/http";
import "@typespec/openapi";

import "../common/errors.tsp";

using Http;
using OpenAPI;

namespace Airlines.Core;

/** Трёхбуквенный код аэропорта IATA, например SVO. */
@pattern("^[A-Z]{3}$")
scalar iataCode extends string;

model AirportResponse {
  iataCode: iataCode;
  name: string;
  city: string;
  country: string;
  latitude: float64;
  longitude: float64;

  /** Часовой пояс в формате IANA, например Europe/Moscow. */
  timezone: string;
}

@route("/airport")
@tag("Airport")
interface Airport {
  /** Список аэропортов справочника. Поиск — по коду IATA, названию или городу без учёта регистра. */
  @get
  @operationId("listAirports")
  list(@query search?: string): AirportResponse[];

  @get
  @operationId("getAirport")
  get(@path iataCode: iataCode): AirportResponse | NotFoundError;
}
