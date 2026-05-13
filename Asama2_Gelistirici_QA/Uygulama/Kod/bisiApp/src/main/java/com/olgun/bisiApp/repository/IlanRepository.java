package com.olgun.bisiApp.repository;

import com.olgun.bisiApp.model.Ilan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface IlanRepository extends JpaRepository<Ilan, Long>, JpaSpecificationExecutor<Ilan> {
}
