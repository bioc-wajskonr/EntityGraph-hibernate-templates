package demo;

import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.math.BigInteger;
import java.util.List;

public class HumanRepository {
    private SessionFactory sessionFactory;
    private EntityManagerFactory entityManagerFactory;

    public HumanRepository() {
        // Create registry
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();

        try {
            // Create MetadataSources
            MetadataSources sources = new MetadataSources(registry);

            // Create Metadata
            Metadata metadata = sources.getMetadataBuilder().build();

            // Create SessionFactory
            sessionFactory = metadata.getSessionFactoryBuilder().build();
        } catch (Exception e) {
            e.printStackTrace();
            if (registry != null) {
                StandardServiceRegistryBuilder.destroy(registry);
            }
        }

        entityManagerFactory = Persistence.createEntityManagerFactory("templatePU");
    }

    public HumanRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public Human findByIdHousesEagerlyByQuery(BigInteger id) {
        try (Session session = sessionFactory.openSession()) {
            String query = "SELECT DISTINCT human " +
                    "FROM Human human " +
                    "LEFT OUTER JOIN FETCH human.houses houses " +
                    "LEFT OUTER JOIN FETCH houses.address " +
                    "WHERE human.id = :id";

            List results = session.createQuery(query)
                                  .setParameter("id", id)
                                  .getResultList();

            return (results.isEmpty()) ? null : (Human) results.get(0);
        }
    }

    public Human findByIdWithEntityGraph(BigInteger id) {
        List results;
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {

            EntityGraph<?> entityGraph = entityManager.createEntityGraph(Human.class);
            entityGraph.addSubgraph("houses").addAttributeNodes("address");

            results = entityManager.createQuery("SELECT h FROM Human h WHERE h.id = ?1")
                                   .setParameter(1, 1234)
                                   .setHint("javax.persistence.fetchgraph", entityGraph)
                                   .getResultList();
        }

        return (results.isEmpty()) ? null : (Human) results.get(0);
    }

    public Human findByIdHousesEagerlyByQueryWithJoinOn(BigInteger id) {
        try (Session session = sessionFactory.openSession()) {
            String query = "SELECT DISTINCT human " +
                    "FROM Human human " +
                    "LEFT OUTER JOIN FETCH human.houses houses " +
                    "LEFT OUTER JOIN FETCH Address a on a.id = houses.address.id " + //different from
                    // findByIdHousesEagerlyByQuery
                    "WHERE human.id = :id";

            List results = session.createQuery(query)
                                  .setParameter("id", id)
                                  .getResultList();

            return (results.isEmpty()) ? null : (Human) results.get(0);
        }
    }

    public BigInteger save(Human human) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            BigInteger id = (BigInteger) session.save(human);

            session.getTransaction().commit();

            return id;
        } catch (Exception e) {
            if (sessionFactory != null) {
                sessionFactory.getCurrentSession().getTransaction().rollback();
            }
            throw e;
        }
    }
}
