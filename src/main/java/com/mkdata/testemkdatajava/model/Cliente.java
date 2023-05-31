package com.mkdata.testemkdatajava.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String nome;

    @NotBlank
    private String tipo;

    @NotBlank
    @Column(unique = true)
    private String cpfCnpj;

    private String rgIe;

    @Temporal(TemporalType.DATE)
    private Date dataCadastro;

    private boolean ativo;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cliente", orphanRemoval = true)
    @JsonManagedReference
    private List<Telefone> telefones;

    public void adicionarTelefone(Telefone telefone) {
        telefones.add(telefone);
        telefone.setCliente(this);
    }

    public void removerTelefone(Telefone telefone) {
        telefones.remove(telefone);
        telefone.setCliente(null);
    }



}