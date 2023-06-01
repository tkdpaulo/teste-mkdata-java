package com.mkdata.testemkdatajava.repository;

import com.mkdata.testemkdatajava.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    List<Cliente> findByNomeContainingIgnoreCaseAndAtivoTrue(String nome);
    List<Cliente> findByNomeContainingIgnoreCase(String nome);
    boolean existsByCpfCnpj(String cpfCnpj);
    boolean existsByCpfCnpjAndIdNot(String cpfCnpj, Long id);

}