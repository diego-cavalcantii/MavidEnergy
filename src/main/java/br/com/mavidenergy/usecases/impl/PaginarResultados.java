package br.com.mavidenergy.usecases.impl;

import br.com.mavidenergy.gateways.responses.FornecedorPaginadoResponseDTO;
import br.com.mavidenergy.usecases.interfaces.PaginarResultado;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaginarResultados implements PaginarResultado {
    @Override
    public Page<FornecedorPaginadoResponseDTO> paginarResultados(List<FornecedorPaginadoResponseDTO> fornecedores, Pageable pageable) {
        int total = fornecedores.size(); // Total de fornecedores
        int start = (int) pageable.getOffset(); // Índice inicial da página
        int end = Math.min(start + pageable.getPageSize(), total); // Índice final da página

        List<FornecedorPaginadoResponseDTO> fornecedoresPaginados = fornecedores.subList(start, end); // Sublista da página atual

        return new PageImpl<>(fornecedoresPaginados, pageable, total);
    }
}
