package sezergemtsov.Pet.Clinic.with.JPA.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sezergemtsov.Pet.Clinic.with.JPA.model.Visit;

@Repository
public interface VisitRepo extends JpaRepository<Visit, Long> {
}
