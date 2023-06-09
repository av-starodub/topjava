package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryUserRepository implements UserRepository {
    private static final Logger log = LoggerFactory.getLogger(InMemoryUserRepository.class);
    private final Map<Integer, User> users = new ConcurrentHashMap<>();
    private final Map<String, Integer> emails = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    @Override
    public boolean delete(int id) {
        log.info("delete {}", id);
        User removedUser = users.remove(id);
        return Objects.nonNull(emails.remove(removedUser.getEmail()));
    }

    @Override
    public User save(User user) {
        log.info("save {}", user);
        if (user.isNew()) {
            String email = user.getEmail();
            checkEmail(email);
            user.setId(counter.incrementAndGet());
            users.put(user.getId(), user);
            emails.put(email, user.getId());
            return user;
        }
        // handle case: update, but not present in storage
        return replace(user);
    }

    @Override
    public User get(int id) {
        log.info("get {}", id);
        return users.get(id);
    }

    @Override
    public List<User> getAll() {
        log.info("getAll");
        return users.values()
                .stream()
                .sorted(Comparator.comparing(User::getName, String.CASE_INSENSITIVE_ORDER))
                .collect(Collectors.toList());
    }

    @Override
    public User getByEmail(String email) {
        log.info("getByEmail {}", email);
        Integer id = emails.get(email);
        if (Objects.nonNull(id)) {
            return users.get(id);
        }
        return null;
    }

    private void checkEmail(String email) {
        log.info("checkEmail {}", email);
        if (Objects.nonNull((emails.get(email)))) {
            throw new IllegalArgumentException(email + " already registered");
        }
    }

    private User replace(User user) {
        return users.computeIfPresent(user.getId(), (id, oldUser) -> {
            if (Objects.isNull(emails.putIfAbsent(user.getEmail(), id))) {
                emails.remove(oldUser.getEmail());
            }
            return user;
        });
    }
}
