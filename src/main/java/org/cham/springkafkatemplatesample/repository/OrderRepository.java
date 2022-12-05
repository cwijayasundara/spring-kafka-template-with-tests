package org.cham.springkafkatemplatesample.repository;

import org.cham.springkafkatemplatesample.domain.Order;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends CrudRepository<Order, Long> {
    List<Order> findAll();
    Optional<Order> findById(Long id);
    @Override
    <S extends Order> S save(S entity);
}
