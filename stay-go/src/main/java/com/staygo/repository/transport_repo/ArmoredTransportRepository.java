package com.staygo.repository.transport_repo;

import com.staygo.enity.transport.ArmoredTransport;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArmoredTransportRepository extends CrudRepository<ArmoredTransport, Long> {
}
