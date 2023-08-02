package sezergemtsov.Pet.Clinic.with.JPA.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Objects;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "customers")
public class Owner implements PetShopEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    @Column
    @NotBlank(message = "Name is mandatory")
    String name;
    @Column
    @NotBlank(message = "Phone number is mandatory")
    String phoneNumber;
    @Column(nullable = true)
    String email;
    @OneToMany(mappedBy = "owner")
    List<Pet> pets;
    @OneToMany(mappedBy = "owner")
    List<Visit> visits;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Owner owner = (Owner) o;
        return id == owner.id && Objects.equals(name, owner.name) && Objects.equals(phoneNumber, owner.phoneNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, phoneNumber);
    }
}
