package br.com.mavidenergy.gateways.controllers;

import br.com.mavidenergy.domains.Endereco;
import br.com.mavidenergy.domains.Fornecedor;
import br.com.mavidenergy.gateways.repositories.FornecedorRepository;
import br.com.mavidenergy.gateways.responses.EnderecoResponseDTO;
import br.com.mavidenergy.gateways.responses.FornecedorResponseDTO;
import br.com.mavidenergy.usecases.interfaces.BuscarFornecedor;
import br.com.mavidenergy.usecases.interfaces.ConverteFornecedorEmDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/fornecedores")
@RequiredArgsConstructor
public class FornecedorController {

    private final BuscarFornecedor buscarFornecedor;
    private final FornecedorRepository fornecedorRepository;
    private final ConverteFornecedorEmDTO converteFornecedorEmDTO;

    @GetMapping
    public ResponseEntity<List<FornecedorResponseDTO>> buscarTodosOsFornecedores() {

        List<Fornecedor> fornecedores = buscarFornecedor.buscarFornecedores();

        List<FornecedorResponseDTO> fornecedorResponseDTOS = converteFornecedorEmDTO.executa(fornecedores);

        return ResponseEntity.ok(fornecedorResponseDTOS);
    }

    @GetMapping("/proximos")
    public ResponseEntity<Page<FornecedorResponseDTO>> buscarFornecedoresProximos(
            @RequestParam Double latitude,
            @RequestParam Double longitude,
            @PageableDefault(page = 0, size = 10) Pageable pageable) {

        // Carrega todos os fornecedores do banco
        List<Fornecedor> fornecedores = fornecedorRepository.buscarTodosComEndereco();

        // Aplica a lógica de proximidade, ordenação e paginação
        Page<FornecedorResponseDTO> fornecedoresPaginados = buscarFornecedor.buscarFornecedorMaisProximo(latitude, longitude, fornecedores, pageable);

        return ResponseEntity.ok(fornecedoresPaginados);
    }





}
