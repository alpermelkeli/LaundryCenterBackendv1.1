package org.alpermelkeli.repository;

import org.alpermelkeli.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {
    /**
     * Mongo db create query for email automatically
     * @param email
     * @return
     */
    User findByEmail(String email);

}
