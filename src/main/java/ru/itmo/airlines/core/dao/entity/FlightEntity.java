package ru.itmo.airlines.core.dao.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "flight")
@Setter
@Getter
public class FlightEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "run_id", nullable = false, unique = true)
    private RunEntity run;

    @Column(name = "economy_seats", nullable = false)
    @Min(value = 0)
    private int economySeats;

    @Column(name = "business_seats", nullable = false)
    @Min(value = 0)
    private int businessSeats;
}
