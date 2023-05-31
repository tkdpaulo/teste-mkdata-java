package com.mkdata.testemkdatajava.controller;

import com.mkdata.testemkdatajava.model.Cliente;
import com.mkdata.testemkdatajava.model.Telefone;
import com.mkdata.testemkdatajava.repository.ClienteRepository;
import com.mkdata.testemkdatajava.service.ClienteService;
import com.mkdata.testemkdatajava.service.TelefoneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    private final ClienteService clienteService;
    private final TelefoneService telefoneService;

    @Autowired
    ClienteRepository clienteRepository;

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
        Cliente clienteAtualizado = clienteService.atualizarCliente(clienteId, cliente);
        return ResponseEntity.ok(clienteAtualizado);
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
