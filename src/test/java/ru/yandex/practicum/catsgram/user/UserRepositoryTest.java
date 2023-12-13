package ru.yandex.practicum.catsgram.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.catsgram.Constants;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class UserRepositoryTest {
    private final UserRepository userRepository;

    private final User user1 = User.builder()
            .id(1L)
            .name("test name 1")
            .birthday(LocalDate.now().minusYears(10))
            .email("test1@yandex.ru")
            .build();
    private final User user2 = User.builder()
            .id(2L)
            .name("test name 2")
            .birthday(LocalDate.now().minusYears(5))
            .email("test2@yandex.ru")
            .build();
    private final User user3 = User.builder()
            .id(3L)
            .name("test name 3")
            .birthday(LocalDate.now().minusYears(2))
            .email("test3@yandex.ru")
            .build();
    private final User user4 = User.builder()
            .id(4L)
            .name("test name 4")
            .birthday(LocalDate.now().minusYears(1))
            .email(user1.getEmail())
            .build();
    private final Integer from = 0;
    private final Integer size = Integer.parseInt(Constants.PAGE_DEFAULT_SIZE);
    private final Pageable pageable = PageRequest.of(from, size);

    @BeforeEach
    public void beforeEach() {
        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);
    }

    @Nested
    class FindAllByIdIn {
        @Test
        void shouldGetThree() {
            List<User> usersFromRepository = userRepository.findAllByIdIn(List.of(user1.getId(), user2.getId(),
                    user3.getId()), pageable);

            assertEquals(3, usersFromRepository.size());

            User userFromRepository1 = usersFromRepository.get(0);
            User userFromRepository2 = usersFromRepository.get(1);
            User userFromRepository3 = usersFromRepository.get(2);

            assertEquals(user1, userFromRepository1);
            assertEquals(user2, userFromRepository2);
            assertEquals(user3, userFromRepository3);
        }

        @Test
        void shouldGetOne() {
            List<User> usersFromRepository = userRepository.findAllByIdIn(List.of(user3.getId()), pageable);

            assertEquals(1, usersFromRepository.size());

            User userFromRepository1 = usersFromRepository.get(0);

            assertEquals(user3, userFromRepository1);
        }

        @Test
        void shouldGetEmpty() {
            List<User> usersFromRepository = userRepository.findAllByIdIn(List.of(99L), pageable);

            assertTrue(usersFromRepository.isEmpty());
        }

        @Test
        void shouldGetOneByPageable() {
            List<User> usersFromRepository = userRepository.findAllByIdIn(List.of(user1.getId(), user2.getId(),
                    user3.getId()), PageRequest.of(1, 2));

            assertEquals(1, usersFromRepository.size());

            User userFromRepository1 = usersFromRepository.get(0);

            assertEquals(user3, userFromRepository1);
        }
    }

    @Nested
    class Save {
        @Test
        void shouldThrowExceptionIfEmailExist() {
            assertThrows(DataIntegrityViolationException.class, () -> userRepository.save(user4));

            assertTrue(userRepository.findById(user4.getId()).isEmpty());
        }
    }
}
