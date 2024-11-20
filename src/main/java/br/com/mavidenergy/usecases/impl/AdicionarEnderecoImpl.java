package br.com.mavidenergy.usecases.impl;

import br.com.mavidenergy.domains.Cidade;
import br.com.mavidenergy.domains.Endereco;
import br.com.mavidenergy.domains.Pessoa;
import br.com.mavidenergy.gateways.controllers.EnderecoController;
import br.com.mavidenergy.gateways.repositories.EnderecoRepository;
import br.com.mavidenergy.gateways.repositories.CidadeRepository;
import br.com.mavidenergy.gateways.requests.EnderecoRequestDTO;
import br.com.mavidenergy.gateways.responses.EnderecoResponseDTO;
import br.com.mavidenergy.usecases.interfaces.AdicionarEndereco;
import br.com.mavidenergy.usecases.interfaces.BuscarCidade;
import br.com.mavidenergy.usecases.interfaces.BuscarPessoa;
import br.com.mavidenergy.usecases.interfaces.ConverteEnderecoEmDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Service;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Service
@RequiredArgsConstructor
public class AdicionarEnderecoImpl implements AdicionarEndereco {

    private final EnderecoRepository enderecoRepository;
    private final BuscarCidade buscarCidade;
    private final BuscarPessoa buscarPessoa;
    private final ConverteEnderecoEmDTO converteEnderecoEmDTO;

    @Override
    public EnderecoResponseDTO executa(EnderecoRequestDTO enderecoRequestDTO) {

        Cidade cidade = buscarCidade.buscarPorId(enderecoRequestDTO.getCidadeId());

        Pessoa pessoa = buscarPessoa.buscarPorId(enderecoRequestDTO.getPessoaId());

        Endereco novoEndereco = Endereco.builder()
                .cep(enderecoRequestDTO.getCep())
                .logradouro(enderecoRequestDTO.getLogradouro())
                .numero(enderecoRequestDTO.getNumero())
                .latitude(enderecoRequestDTO.getLatitude())
                .longitude(enderecoRequestDTO.getLongitude())
                .cidade(cidade)
                .pessoa(pessoa)
                .build();

        enderecoRepository.save(novoEndereco);

        EnderecoResponseDTO enderecoResponseDTO = converteEnderecoEmDTO.executa(novoEndereco);

        Link link = linkTo(EnderecoController.class).slash(novoEndereco.getEnderecoId()).withSelfRel();

        enderecoResponseDTO.add(link);

        return enderecoResponseDTO;
    }
}
