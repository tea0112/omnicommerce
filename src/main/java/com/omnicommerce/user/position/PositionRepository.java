package com.omnicommerce.user.position;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PositionRepository extends JpaRepository<Position, Long> {
    List<Position> findByPositionKeyUserId(Long userId);
}
