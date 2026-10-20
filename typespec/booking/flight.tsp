import "@typespec/http";
import "@typespec/openapi";

import "../common/errors.tsp";
import "../common/types.tsp";

using Http;
using OpenAPI;

namespace Airlines.Core;

enum CabinClass {
  ECONOMY,
  BUSINESS,
}

model CreateFlightRequest {
  runId: uuid;

  @minValue(0)
  economySeats: int32;

  @minValue(0)
  businessSeats: int32;
}

model FlightResponse {
  id: uuid;
  runId: uuid;
  economySeats: int32;
  businessSeats: int32;

  /** Счёт авиакомпании, созданный вместе с рейсом. */
  airlineAccountId: uuid;
}

model SeatResponse {
  id: uuid;
  flightId: uuid;

  /** Номер места, уникален в пределах рейса, например 14C. */
  seatNumber: string;

  cabinClass: CabinClass;
}

model CloseCheckInResponse {
  /** Сколько билетов переведено из PURCHASED в NO_SHOW. */
  noShows: int32;
}

@route("/flight")
@tag("Flight")
interface Flight {
  /** Создаёт рейс прогона. Места и счёт авиакомпании создаются автоматически. */
  @post
  @operationId("createFlight")
  create(@body request: CreateFlightRequest):
    | {
        @statusCode _: 201;
        @body body: FlightResponse;
      }
    | BadRequestError
    | NotFoundError;

  @get
  @operationId("getFlight")
  get(@path flightId: uuid): FlightResponse | NotFoundError;

  @get
  @route("/{flightId}/seat")
  @operationId("listFlightSeats")
  listSeats(@path flightId: uuid, @query cabinClass?: CabinClass):
    | SeatResponse[]
    | NotFoundError;

  @get
  @route("/{flightId}/ticket")
  @operationId("listFlightTickets")
  listTickets(
    @path flightId: uuid,
    @query cabinClass?: CabinClass,
    @query status?: TicketStatus,
  ): TicketResponse[] | NotFoundError;

  /** Закрывает регистрацию: все билеты рейса в статусе PURCHASED переходят в NO_SHOW, деньги не возвращаются. */
  @post
  @route("/{flightId}/close-check-in")
  @operationId("closeFlightCheckIn")
  closeCheckIn(@path flightId: uuid): CloseCheckInResponse | NotFoundError;
}
