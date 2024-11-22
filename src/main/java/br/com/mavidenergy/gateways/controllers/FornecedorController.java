package br.com.mavidenergy.gateways.controllers;

import br.com.mavidenergy.domains.Endereco;
import br.com.mavidenergy.domains.Fornecedor;
import br.com.mavidenergy.gateways.repositories.FornecedorRepository;
import br.com.mavidenergy.gateways.responses.EnderecoResponseDTO;
import br.com.mavidenergy.gateways.responses.FornecedorPaginadoResponseDTO;
import br.com.mavidenergy.gateways.responses.FornecedorResponseDTO;
import br.com.mavidenergy.usecases.interfaces.BuscarFornecedor;
import br.com.mavidenergy.usecases.interfaces.ConverteEnderecoEmDTO;
import br.com.mavidenergy.usecases.interfaces.ConverteFornecedorEmDTO;
import br.com.mavidenergy.usecases.interfaces.ConverteFornecedorPaginadoEmDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @GetMapping("/proximos")
    @Operation(summary = "Busca fornecedores próximos", description = "Retorna uma lista de fornecedores próximos baseada nas coordenadas fornecidas.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Fornecedores próximos encontrados", content = @Content(mediaType = "application/json", schema = @Schema(implementation = FornecedorResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Parâmetros inválidos", content = @Content),
    })
    public ResponseEntity<List<FornecedorResponseDTO>> buscarTodosOsFornecedoresProximos(
            @Parameter(description = "Latitude para busca de proximidade", required = true) @RequestParam Double latitude,
            @Parameter(description = "Longitude para busca de proximidade", required = true) @RequestParam Double longitude
    ) {
        // Busca fornecedores
        List<Fornecedor> fornecedores = buscarFornecedor.buscarFornecedores();

        // Converte fornecedores para DTOs usando o use case
        List<FornecedorResponseDTO> fornecedorResponseDTOS = buscarFornecedor.buscarFornecedorMaisProximo(latitude, longitude, fornecedores);

        return ResponseEntity.ok(fornecedorResponseDTOS);
    }


    @GetMapping("/proximos-paginado")
    @Operation(summary = "Busca paginada de fornecedores próximos", description = "Retorna uma página de fornecedores próximos baseada nas coordenadas fornecidas.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Página de fornecedores próximos encontrada", content = @Content(mediaType = "application/json", schema = @Schema(implementation = FornecedorPaginadoResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Parâmetros inválidos", content = @Content),
    })
    public ResponseEntity<Page<FornecedorPaginadoResponseDTO>> buscarFornecedoresProximosPaginados(
            @Parameter(description = "Latitude para busca de proximidade", required = true) @RequestParam Double latitude,
            @Parameter(description = "Longitude para busca de proximidade", required = true) @RequestParam Double longitude,
            @Parameter(description = "Detalhes de paginação", required = true) @PageableDefault(page = 0, size = 10) Pageable pageable) {

        // Carrega todos os fornecedores do banco
        List<Fornecedor> fornecedores = fornecedorRepository.buscarTodosComEndereco();

        // Aplica a lógica de proximidade, ordenação e paginação
        Page<FornecedorPaginadoResponseDTO> fornecedoresPaginados = buscarFornecedor.buscarFornecedorMaisProximoPaginado(latitude, longitude, fornecedores, pageable);

        return ResponseEntity.ok(fornecedoresPaginados);
    }





}
