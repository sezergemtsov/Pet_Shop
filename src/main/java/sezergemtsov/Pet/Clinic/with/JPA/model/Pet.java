package sezergemtsov.Pet.Clinic.with.JPA.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "pets")
public class Pet implements PetShopEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    @Column
    @NotBlank(message = "name is mandatory")
    String name;
    @OneToOne(mappedBy = "pet")
    Collar collar;
    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    Owner owner;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pet pet = (Pet) o;
        return id == pet.id && Objects.equals(name, pet.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
