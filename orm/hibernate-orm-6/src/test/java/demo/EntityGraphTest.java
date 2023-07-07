package demo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class EntityGraphTest {
    private HumanRepository humanRepo;

    @BeforeEach
    public void setup() {
        humanRepo = new HumanRepository();
    }

    @Test
    public void shouldReturnAlwaysSameNumberOfHuman() {
        // given
        Human human = new Human();
        human.setHouses(Collections.emptyList());

        // when
        humanRepo.save(human);
        humanRepo.save(new Human());

        System.out.println("findAllHousesEagerlyByQuery");
        Human human1 = humanRepo.findByIdHousesEagerlyByQuery(BigInteger.ONE);
        System.out.println("findAllHousesEagerlyByQueryWithJoinOn");
        Human human2 = humanRepo.findByIdHousesEagerlyByQueryWithJoinOn(BigInteger.ONE);
        System.out.println("findByIdWithEntityGraph");
        Human human3 = humanRepo.findByIdWithEntityGraph(BigInteger.ONE);

        // then
        assertNotNull(human1);
        assertNotNull(human2);
        assertNotNull(human3);
    }
}

