package com.taco.tacository.data._expr.mongodb.test_country;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface CountryRepository extends MongoRepository<Country, String> { }
