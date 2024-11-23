package com.staygo.repository.transport_repo;

import com.staygo.enity.transport.ArmoredTransport;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ArmoredTransportRepository extends CrudRepository<ArmoredTransport, Long> {
}
