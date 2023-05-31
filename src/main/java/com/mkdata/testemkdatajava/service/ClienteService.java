package com.mkdata.testemkdatajava.service;

import com.mkdata.testemkdatajava.ResourceAlreadyExistsException;
import com.mkdata.testemkdatajava.ResourceNotFoundException;
import com.mkdata.testemkdatajava.model.Cliente;
import com.mkdata.testemkdatajava.model.Telefone;
import com.mkdata.testemkdatajava.repository.ClienteRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final TelefoneService telefoneService;

    public ClienteService(ClienteRepository clienteRepository, TelefoneService telefoneService) {
        this.clienteRepository = clienteRepository;
        this.telefoneService = telefoneService;
    }

    public Cliente adicionarCliente(Cliente cliente) {
        validarExistenciaCPFouCNPJ(cliente.getCpfCnpj());
        return clienteRepository.save(cliente);
    }

    public Cliente atualizarCliente(Long clienteId, Cliente cliente) {
        Cliente clienteExistente = clienteRepository.findById(clienteId).orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado com o ID: " + clienteId));
        validarExistenciaCPFouCNPJ(cliente.getCpfCnpj(), clienteId);
        clienteExistente.setNome(cliente.getNome());
        clienteExistente.setTipo(cliente.getTipo());
        clienteExistente.setCpfCnpj(cliente.getCpfCnpj());
        clienteExistente.setRgIe(cliente.getRgIe());
        clienteExistente.setDataCadastro(cliente.getDataCadastro());
        clienteExistente.setAtivo(cliente.isAtivo());
        return clienteRepository.save(clienteExistente);
    }

    public void excluirCliente(Long clienteId) {
        Cliente cliente = clienteRepository.findById(clienteId).orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado com o ID: " + clienteId));
        clienteRepository.delete(cliente);
    }

    public Cliente buscarClientePorId(Long clienteId) {
        return clienteRepository.findById(clienteId).orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado com o ID: " + clienteId));
    }

    public List<Cliente> buscarTodosClientes() {
        return clienteRepository.findAll();
    }

    public List<Cliente> filtrarClientes(String nome, boolean apenasAtivos) {
        if (apenasAtivos) {
            return clienteRepository.findByNomeContainingIgnoreCaseAndAtivoTrue(nome);
        } else {
            return clienteRepository.findByNomeContainingIgnoreCase(nome);
        }
    }

    private void validarExistenciaCPFouCNPJ(String cpfCnpj) {
        if (clienteRepository.existsByCpfCnpj(cpfCnpj)) {
            throw new ResourceAlreadyExistsException("Já existe um cliente cadastrado com o CPF/CNPJ informado: " + cpfCnpj);
        }
    }

    private void validarExistenciaCPFouCNPJ(String cpfCnpj, Long clienteId) {
        if (clienteRepository.existsByCpfCnpjAndIdNot(cpfCnpj, clienteId)) {
            throw new ResourceAlreadyExistsException("Já existe um cliente cadastrado com o CPF/CNPJ informado: " + cpfCnpj);
        }
    }
}
