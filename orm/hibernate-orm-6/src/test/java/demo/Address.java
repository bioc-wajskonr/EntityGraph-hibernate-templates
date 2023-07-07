package demo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.math.BigInteger;

@Entity
public class Address {

    @Id
    @Column(name = "ID", nullable = false, updatable = false, precision = 20)
    @GeneratedValue
    private BigInteger id;

}
