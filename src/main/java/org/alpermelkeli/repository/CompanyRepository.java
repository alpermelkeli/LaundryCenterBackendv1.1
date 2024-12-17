package org.alpermelkeli.repository;

import org.alpermelkeli.model.Company;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CompanyRepository extends MongoRepository<Company,String> {


}
