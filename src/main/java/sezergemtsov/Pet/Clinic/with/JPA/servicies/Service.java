package sezergemtsov.Pet.Clinic.with.JPA.servicies;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.metamodel.EntityType;
import jakarta.persistence.metamodel.Metamodel;
import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import sezergemtsov.Pet.Clinic.with.JPA.model.*;
import sezergemtsov.Pet.Clinic.with.JPA.repositories.CollarsRepo;
import sezergemtsov.Pet.Clinic.with.JPA.repositories.OwnersRepo;
import sezergemtsov.Pet.Clinic.with.JPA.repositories.PetRepo;
import sezergemtsov.Pet.Clinic.with.JPA.repositories.VisitRepo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

@org.springframework.stereotype.Service
@AllArgsConstructor
@SuppressWarnings("unused")
public class Service {

    private final CollarsRepo collars;
    private final OwnersRepo owners;
    private final PetRepo pets;
    private final VisitRepo visits;
    private final EntityManager entityManager;
    private final SessionFactory sessionFactory;

    /**
     * Method to build and save new Owner entity in DB from name and phone number params
     *
     * @param name        - name of customer
     * @param phoneNumber - contact information
     */
    public void newOwner(String name, String phoneNumber) {
        Owner owner = new Owner();
        Class<Owner> ownerClass = Owner.class;
        owner.setId(computeId(ownerClass));
        owner.setName(name);
        owner.setPhoneNumber(phoneNumber);
        owners.save(owner);
    }
    public void newOwner1(String name, String phoneNumber){
        Owner owner = new Owner();
        owner.setName(name);
        owner.setPhoneNumber(phoneNumber);
        owners.save(owner);
    }

    /**
     * Method to build and save new Pet in DB. To create new pet the owner should be created first.
     * When new Pet is created, new collar also created automatically
     *
     * @param name    - name of pet
     * @param ownerId - id of owner
     */
    public void newPet(String name, long ownerId) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            Owner owner = session.get(Owner.class, ownerId);
            Pet pet = new Pet();
            pet.setName(name);
            pet.setOwner(owner);
            Collar collar = newCollar(pet);
            session.persist(collar);
            tx.commit();
        }

    }

    /**
     * Method to build new collar. Collar can't be created without existed pet,
     * so this method don't save this entity in DB
     *
     * @param pet - entity of pet that this collar belongs
     * @return - Collar entity
     */
    public Collar newCollar(Pet pet) {
        Collar collar = new Collar();
        collar.setInfo("smart pet");
        collar.setPet(pet);
        return collar;
    }


    /**
     * Sample of saving object with session and transaction. Method build and save Visit entity to DB.
     *
     * @param ownerId  - id of owner who will be attended
     * @param collarId - id of collar of pet that will be attended
     */
    public void newVisit(long ownerId, long collarId) {

        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            Owner owner = session.get(Owner.class, ownerId);
            Collar collar = session.get(Collar.class, collarId);
            Visit visit = new Visit();
            visit.setOwner(owner);
            visit.setCollar(collar);
            session.persist(visit);
            tx.commit();
        }
    }

    /**
     * Method for testing hibernate query
     * Prototype - select pet.name from visits left join collars on visits.collar = collars.id left join pets on collars.pet = pet.id where visit.id = ${id}
     *
     * @param id - id of visit
     * @return - name of pet
     */
    public String getName(long id) {
        String name;
        try (Session session = sessionFactory.openSession()) {
            name = session.get(Visit.class, id).getCollar().getPet().getName();
        }
        return name;
    }


    /**
     * Method to fill DB for testing
     */
    public void fill() {
        newOwner("Alex", "1001");
        newPet("Kumar", 1);
        newVisit(1, 1);
    }

    /**
     * Sample of using Criteria Api for generating id by request to DB
     *
     * @param c   - Entity class
     * @param <T> - Entity class
     * @return - new generated id of long type
     */
    private <T extends PetShopEntity> long computeId(Class<T> c) {
        long id;
        Metamodel metamodel = entityManager.getMetamodel();
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> query = criteriaBuilder.createQuery(c);
        EntityType<T> Type_ = metamodel.entity(c);
        Root<T> root = query.from(Type_);
        query.select(root).orderBy(criteriaBuilder.desc(root));
        TypedQuery<T> typedQuery = entityManager.createQuery(query).setMaxResults(1);
        List<T> resultList = typedQuery.getResultList();
        if (resultList.isEmpty()) {
            id = 1;
        } else {
            id = resultList.get(0).getId() + 1;
        }
        return id;
    }

    /**
     * SQL prototype: select * from ${Entity}
     *
     * @param c   - Entity class
     * @param <T> - Entity class
     * @return - List of Entities<T>
     */
    private <T extends PetShopEntity> List<T> select(Class<T> c) {
        Metamodel metamodel = entityManager.getMetamodel();
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(c);
        EntityType<T> Class_ = metamodel.entity(c);
        Root<T> root = criteriaQuery.from(Class_);
        criteriaQuery.select(root);
        TypedQuery<T> typedQuery = entityManager.createQuery(criteriaQuery);
        return typedQuery.getResultList();
    }

    /**
     * Prototype SQL: select id from ${Entity.class}
     *
     * @param <T> - Entity class from where we search
     * @param <K> - Primary key class of this table/entity
     * @return - List of id
     */
    private <T extends PetShopEntity, K> List<K> selectById(Class<T> c, Class<K> k) {
        Class<?> keyType;
        Metamodel metamodel = entityManager.getMetamodel();
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<K> criteriaQuery = criteriaBuilder.createQuery(k);
        EntityType<T> Class_ = metamodel.entity(c);
        Root<T> root = criteriaQuery.from(Class_);
        criteriaQuery.select(root.get(Class_.getSingularAttribute("id", k)));
        TypedQuery<K> typedQuery = entityManager.createQuery(criteriaQuery);
        return typedQuery.getResultList();
    }

    /**
     * Sample to work with joins on base level. There is SQL:
     * select pet.name from visits left join collars on visits.collar = collars.id left join pets on collars.pet = pet.id where visit.id = ${id}
     */
    public String testName() {

        Class<? extends PetShopEntity> visit = Visit.class;
        Class<? extends PetShopEntity> collar = Collar.class;
        Class<? extends PetShopEntity> pet = Pet.class;
        Class<?> result = String.class;

        Metamodel metamodel = entityManager.getMetamodel();
        EntityType<? extends PetShopEntity> Visit_ = metamodel.entity(visit);
        EntityType<? extends PetShopEntity> Collar_ = metamodel.entity(collar);


        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<String> criteriaQuery = criteriaBuilder.createQuery(String.class);

        String str1 = Collar_.getSingularAttribute(pet.getSimpleName().toLowerCase()).getName();

        Root<? extends PetShopEntity> root = criteriaQuery.from(visit);
        Join<? extends PetShopEntity, ? extends PetShopEntity> fjoin = root.join(Visit_.getSingularAttribute(collar.getSimpleName().toLowerCase()).getName());
        Join<? extends PetShopEntity, ? extends PetShopEntity> sjoin = fjoin.join("pet");

        criteriaQuery.select(sjoin.get("name"));

        TypedQuery<String> typedQuery = entityManager.createQuery(criteriaQuery);

        return typedQuery.getSingleResult();
    }

    /**
     * Sample to build chain of joins.
     *
     * @param tables                 - list of tables which supposed to use to build chain of joins from root to required table
     * @param searchingAttributeName - name of attribute we are searching
     * @param <T>                    - bounding of required class as base entity of DB
     * @return - list of entities named as searched attribute
     */
    private <T extends PetShopEntity> List<?> buildJoins(Collection<Class<? extends T>> tables, String searchingAttributeName) {

        Metamodel metamodel = entityManager.getMetamodel();
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<?> criteriaQuery = criteriaBuilder.createQuery();

        Iterator<Class<? extends T>> iterator = tables.iterator();
        if (tables.isEmpty() | tables.size() < 2) {
            throw new NoResultException("there are no arguments to operate");
        }
        Class<? extends T> currentTable = iterator.next();
        Class<? extends T> joiningTable = iterator.next();

        Root<? extends T> root = criteriaQuery.from(currentTable);

        EntityType<? extends T> entity = metamodel.entity(currentTable);

        Join<? extends T, ? extends T> join = root.join(entity.getSingularAttribute(joiningTable.getSimpleName().toLowerCase()).getName());

        while (iterator.hasNext()) {
            currentTable = joiningTable;
            joiningTable = iterator.next();
            entity = metamodel.entity(currentTable);
            join = join.join(entity.getSingularAttribute(joiningTable.getSimpleName().toLowerCase()).getName());
        }

        criteriaQuery.select(join.get(searchingAttributeName));

        TypedQuery<?> typedQuery = entityManager.createQuery(criteriaQuery);
        return typedQuery.getResultList();
    }

    public List<?> joinTest() {
        List<Class<? extends PetShopEntity>> requestTables = new ArrayList<>();
        String attribute = "name";
        requestTables.add(Visit.class);
        requestTables.add(Collar.class);
        requestTables.add(Pet.class);
        return buildJoins(requestTables, attribute);
    }

}
