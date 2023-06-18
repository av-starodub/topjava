package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryUserRepository implements UserRepository {
    private static final Logger log = LoggerFactory.getLogger(InMemoryUserRepository.class);
    private final Map<Integer, User> users = new ConcurrentHashMap<>();
    private final Map<String, Integer> emailsToUserIdRelationTable = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    @Override
    public boolean delete(int id) {
        log.info("{} delete {}", LocalDateTime.now(), id);
        User removed = users.remove(id);
        if (Objects.nonNull(removed)) {
            emailsToUserIdRelationTable.remove(removed.getEmail());
            return true;
        }
        return false;
    }

    @Override
    public User save(User user) {
        log.info("{} save {}", LocalDateTime.now(), user);
        String email = user.getEmail();
        if (isEmailRegistered(email)) {
            return null;
        }
        if (user.isNew()) {
            user.setId(counter.incrementAndGet());
            emailsToUserIdRelationTable.put(email, user.getId());
            return users.put(user.getId(), user);
        }
        // handle case: update, but not present in storage
        return replace(user);
    }

    @Override
    public User get(int id) {
        log.info("{} get {}", LocalDateTime.now(), id);
        return users.get(id);
    }

    @Override
    public List<User> getAll() {
        log.info("{} getAll", LocalDateTime.now());
        return users.values()
                .stream()
                .sorted(Comparator.comparing(User::getName, String.CASE_INSENSITIVE_ORDER))
                .collect(Collectors.toList());
    }

    @Override
    public User getByEmail(String email) {
        log.info("{} getByEmail {}", LocalDateTime.now(), email);
        Integer id = emailsToUserIdRelationTable.get(email);
        return Objects.nonNull(id) ? users.get(id) : null;
    }

    private boolean isEmailRegistered(String email) {
        return Objects.nonNull(emailsToUserIdRelationTable.get(email));
    }

    private User replace(User user) {
        return users.computeIfPresent(user.getId(), (id, oldUser) -> {
            if (updateEmail(user)) {
                emailsToUserIdRelationTable.remove(oldUser.getEmail());
            }
            return user;
        });
    }

    private boolean updateEmail(User user) {
        return Objects.isNull(emailsToUserIdRelationTable.putIfAbsent(user.getEmail(), user.getId()));
    }
}
