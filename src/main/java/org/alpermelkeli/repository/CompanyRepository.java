package org.alpermelkeli.repository;

import org.alpermelkeli.model.CompanyEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CompanyRepository extends MongoRepository<CompanyEntity,String> {


}
