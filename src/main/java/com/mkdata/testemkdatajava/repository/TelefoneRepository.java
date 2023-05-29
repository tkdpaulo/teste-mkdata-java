package com.mkdata.testemkdatajava.repository;

import com.mkdata.testemkdatajava.model.Telefone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TelefoneRepository extends JpaRepository<Telefone, Long> {}