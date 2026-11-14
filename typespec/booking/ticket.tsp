import "@typespec/http";
import "@typespec/openapi";

import "../common/errors.tsp";
import "../common/types.tsp";
import "./flight.tsp";

using Http;
using OpenAPI;

namespace Airlines.Core;

enum TicketStatus {
  PURCHASED,
  REFUNDED,
  CHECKED_IN,
  NO_SHOW,
  DENIED_BOARDING,
}

model PurchaseTicketRequest {
  flightId: uuid;
  passengerId: uuid;
  cabinClass: CabinClass;

  /** Цена билета в рублях, её рассчитывает движок. */
  @minValue(0)
  price: decimal;

  /** Время покупки в виртуальном времени прогона. */
  purchasedAt: utcDateTime;
}

model CheckInRequest {
  /** Компенсация в рублях, которая выплачивается при отказе в посадке. */
  @minValue(0)
  compensation: decimal;
}

model TicketResponse {
  id: uuid;
  flightId: uuid;
  passengerId: uuid;
  cabinClass: CabinClass;
  price: decimal;
  status: TicketStatus;

  /** Назначается при регистрации. */
  seatId?: uuid;

  seatNumber?: string;
  purchasedAt: utcDateTime;
}

@route("/ticket")
@tag("Ticket")
interface Ticket {
  /** Покупка билета: цена переводится со счёта пассажира на счёт авиакомпании. */
  @post
  @operationId("purchaseTicket")
  purchase(@body request: PurchaseTicketRequest):
    | {
        @statusCode _: 201;
        @body body: TicketResponse;
      }
    | BadRequestError
    | NotFoundError;

  @get
  @operationId("getTicket")
  get(@path ticketId: uuid): TicketResponse | NotFoundError;

  /** Возврат билета: цена переводится со счёта авиакомпании на счёт пассажира, билет переходит в REFUNDED. */
  @post
  @route("/{ticketId}/refund")
  @operationId("refundTicket")
  refund(@path ticketId: uuid): TicketResponse | NotFoundError | ConflictError;

  /**
   * Регистрация на рейс. Если в классе билета есть свободное место — оно назначается, билет переходит в CHECKED_IN.
   * Если мест нет — билет переходит в DENIED_BOARDING, пассажиру возвращается цена билета и выплачивается компенсация.
   * Оба исхода — успешный ответ, итог смотреть по статусу билета.
   */
  @post
  @route("/{ticketId}/check-in")
  @operationId("checkInTicket")
  checkIn(@path ticketId: uuid, @body request: CheckInRequest):
    | TicketResponse
    | BadRequestError
    | NotFoundError
    | ConflictError;
}
