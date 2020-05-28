package com.esme.amazon.application.rappels.Infrastructure;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MarcheRepository extends CrudRepository<AmazonEntity, Long> {

}
