package ru.astondevs.bankapp.account.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "accounts")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "owner", length = 512, nullable = false)
    private String owner;

    @Column(name = "pin", nullable = false)
    private Integer pin;

    @Column(name = "balance", nullable = false)
    private Double balance;
}
