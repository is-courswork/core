package ru.itmo.airlines.core.dao.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.type.SqlTypes;
import ru.itmo.airlines.core.model.CabinClass;

import java.util.UUID;

@Entity
@Table(name = "seat")
@Setter
@Getter
public class SeatEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "flight_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private FlightEntity flight;

    @Column(name = "seat_number", nullable = false, length = 12)
    private String seatNumber;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(nullable = false, name = "cabin_class", columnDefinition = "cabin_class")
    private CabinClass cabinClass;
}
