package demo;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigInteger;
import java.util.Collection;

@Entity
public class Human {
    @Id
    @Column(name = "ID", nullable = false, updatable = false, precision = 20)
    @GeneratedValue
    private BigInteger id;

    @OneToMany(mappedBy = "human", cascade = CascadeType.ALL)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Collection<House> houses;


    /**
     * Sets {@link Collection< House>} houses to {@code houses}.
     */
    public void setHouses(Collection<House> houses) {
        this.houses = houses;
    }

}
