package com.mkdata.testemkdatajava.controller;

import com.mkdata.testemkdatajava.ResourceNotFoundException;
import com.mkdata.testemkdatajava.model.Cliente;
import com.mkdata.testemkdatajava.model.Telefone;
import com.mkdata.testemkdatajava.repository.ClienteRepository;
import com.mkdata.testemkdatajava.repository.TelefoneRepository;
import com.mkdata.testemkdatajava.service.ClienteService;
import com.mkdata.testemkdatajava.service.TelefoneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    private final ClienteService clienteService;
    private final TelefoneService telefoneService;

    @Autowired
    ClienteRepository clienteRepository;
    TelefoneRepository telefoneRepository;

    public ClienteController(ClienteService clienteService, TelefoneService telefoneService) {
        this.clienteService = clienteService;
        this.telefoneService = telefoneService;
    }

    @GetMapping
    public ResponseEntity<List<Cliente>> listarClientes() {
        List<Cliente> clientes = clienteService.buscarTodosClientes();
        return ResponseEntity.ok(clientes);
    }

    @GetMapping("/{clienteId}")
    public ResponseEntity<Cliente> buscarClientePorId(@PathVariable Long clienteId) {
        Cliente cliente = clienteService.buscarClientePorId(clienteId);
        return ResponseEntity.ok(cliente);
    }

    @PostMapping
    public ResponseEntity<Cliente> adicionarCliente(@RequestBody Cliente cliente) {
        Cliente novoCliente = clienteService.adicionarCliente(cliente);
        List<Telefone> telefones = cliente.getTelefones();
        if (telefones != null && !telefones.isEmpty()) {
            telefones.forEach(telefone -> telefoneService.adicionarTelefone(cliente.getId(), telefone));
            cliente.setTelefones(telefones);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(novoCliente);
    }

    @PutMapping("/{clienteId}")
    public ResponseEntity<Cliente> atualizarCliente(@PathVariable Long clienteId, @RequestBody Cliente cliente) {
        Cliente clienteExistente = clienteRepository.findById(clienteId).orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado com o ID: " + clienteId));

        // Atualizar os campos do cliente existente com os dados enviados no corpo da requisição
        clienteExistente.setNome(cliente.getNome());
        clienteExistente.setTipo(cliente.getTipo());
        clienteExistente.setCpfCnpj(cliente.getCpfCnpj());
        clienteExistente.setRgIe(cliente.getRgIe());
        clienteExistente.setDataCadastro(cliente.getDataCadastro());
        clienteExistente.setAtivo(cliente.isAtivo());

        // Atualizar os telefones do cliente existente
        List<Telefone> telefonesAtualizados = new ArrayList<>();
        for (Telefone telefone : cliente.getTelefones()) {
            if (telefone.getId() != null) {
                // Se o telefone já possui um ID, é um telefone existente a ser atualizado
                Telefone telefoneExistente = telefoneRepository.findById(telefone.getId()).orElseThrow(() -> new ResourceNotFoundException("Telefone não encontrado com o ID: " + telefone.getId()));
//                telefoneExistente.setCliente(cliente);
                telefoneExistente.setDdd(telefone.getDdd());
                telefoneExistente.setNumero(telefone.getNumero());
                telefonesAtualizados.add(telefoneExistente);
            } else {
                // Se o telefone não possui um ID, é um novo telefone a ser adicionado
                Telefone novoTelefone = new Telefone();
//                novoTelefone.setCliente(cliente);
                novoTelefone.setDdd(telefone.getDdd());
                novoTelefone.setNumero(telefone.getNumero());
                novoTelefone.setCliente(clienteExistente);
                telefonesAtualizados.add(novoTelefone);
            }
        }

        // Remover os telefones que foram removidos da lista de telefones atualizados
        List<Telefone> telefonesRemover = new ArrayList<>();
        for (Telefone telefoneExistente : clienteExistente.getTelefones()) {
            if (!telefonesAtualizados.contains(telefoneExistente)) {
                telefonesRemover.add(telefoneExistente);
            }
        }

        for (Telefone telefoneRemover : telefonesRemover) {
            clienteExistente.removerTelefone(telefoneRemover);
        }

//        clienteExistente.setTelefones(telefonesAtualizados);

        // Salvar o cliente atualizado no banco de dados
        if (!telefonesAtualizados.isEmpty()) {
            telefonesAtualizados.forEach(telefone -> telefoneService.adicionarTelefone(cliente.getId(), telefone));
            clienteExistente.setTelefones(telefonesAtualizados);
        }
        Cliente clienteAtualizado = clienteRepository.save(clienteExistente);

        return ResponseEntity.status(HttpStatus.CREATED).body(clienteAtualizado);
    }


    @DeleteMapping("/{clienteId}")
    public ResponseEntity<Void> excluirCliente(@PathVariable Long clienteId) {
        clienteService.excluirCliente(clienteId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/filtrar")
    public ResponseEntity<List<Cliente>> filtrarClientes(@RequestParam(required = false) String nome, @RequestParam(required = false, defaultValue = "true") boolean ativos) {
        List<Cliente> clientes = clienteService.filtrarClientes(nome, ativos);
        return ResponseEntity.ok(clientes);
    }
}
