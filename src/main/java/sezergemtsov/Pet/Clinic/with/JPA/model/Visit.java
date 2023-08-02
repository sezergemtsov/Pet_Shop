package sezergemtsov.Pet.Clinic.with.JPA.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "visits")
public class Visit implements PetShopEntity {
    @Id
    long id;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "owner_id")
    Owner owner;
    @Column
    Date time;
    @Column
    String remarks;
    @ManyToOne
    @JoinColumn(name = "collar_id")
    Collar collar;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Visit visit = (Visit) o;
        return id == visit.id && Objects.equals(owner, visit.owner);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, owner);
    }
}
