package sezergemtsov.Pet.Clinic.with.JPA.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

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
    String name;
    @Column
    String phoneNumber;
    @Column(columnDefinition = "text(50) default 'example@mail.com'")
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
