package sezergemtsov.Pet.Clinic.with.JPA.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "collars")
public class Collar implements PetShopEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    @Column
    String info;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "pet_id")
    Pet pet;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Collar collar = (Collar) o;
        return id == collar.id && Objects.equals(pet, collar.pet);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, pet);
    }
}
