package io.github.arasdenizhan.bts.management.repository;

import io.github.arasdenizhan.bts.management.model.entity.Baggage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BaggageRepository extends JpaRepository<Baggage, Long> {
    Optional<Baggage> findByTag(String tag);
}
