package sezergemtsov.Pet.Clinic.with.JPA.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sezergemtsov.Pet.Clinic.with.JPA.model.Owner;

@Repository
public interface OwnersRepo extends JpaRepository<Owner, Long> {

}
