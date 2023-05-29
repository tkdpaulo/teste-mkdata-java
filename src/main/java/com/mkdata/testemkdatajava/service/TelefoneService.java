package com.mkdata.testemkdatajava.service;

import com.mkdata.testemkdatajava.ResourceNotFoundException;
import com.mkdata.testemkdatajava.model.Cliente;
import com.mkdata.testemkdatajava.model.Telefone;
import com.mkdata.testemkdatajava.repository.ClienteRepository;
import com.mkdata.testemkdatajava.repository.TelefoneRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TelefoneService {

    private final TelefoneRepository telefoneRepository;
    private final ClienteRepository clienteRepository;

    public TelefoneService(TelefoneRepository telefoneRepository, ClienteRepository clienteRepository) {
        this.telefoneRepository = telefoneRepository;
        this.clienteRepository = clienteRepository;
    }

    public Telefone adicionarTelefone(Long clienteId, Telefone telefone) {
        Cliente cliente = clienteRepository.findById(clienteId).orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado com o ID: " + clienteId));
        telefone.setCliente(cliente);
        return telefoneRepository.save(telefone);
    }

    public Telefone atualizarTelefone(Long telefoneId, Telefone telefone) {
        Telefone telefoneExistente = telefoneRepository.findById(telefoneId).orElseThrow(() -> new ResourceNotFoundException("Telefone não encontrado com o ID: " + telefoneId));
        telefoneExistente.setDdd(telefone.getDdd());
        telefoneExistente.setNumero(telefone.getNumero());
        return telefoneRepository.save(telefoneExistente);
    }

    public void excluirTelefone(Long telefoneId) {
        Telefone telefone = telefoneRepository.findById(telefoneId).orElseThrow(() -> new ResourceNotFoundException("Telefone não encontrado com o ID: " + telefoneId));
        telefoneRepository.delete(telefone);
    }

    public List<Telefone> listarTelefonesPorCliente(Long clienteId) {
        Cliente cliente = clienteRepository.findById(clienteId).orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado com o ID: " + clienteId));
        return cliente.getTelefones();
    }
}
