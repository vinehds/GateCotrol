package com.viniciusdalaqua.GateControl.repositories;

import com.viniciusdalaqua.GateControl.entities.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DriverRepository extends JpaRepository<Driver, Long> {

    @Query("SELECT v FROM Driver v WHERE v.cnh = :cnh")
    Optional<Driver> findByCnh(@Param("cnh") String cnh);

}
