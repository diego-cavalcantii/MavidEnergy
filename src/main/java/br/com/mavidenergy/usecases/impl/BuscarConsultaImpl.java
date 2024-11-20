package br.com.mavidenergy.usecases.impl;

import br.com.mavidenergy.domains.Consulta;
import br.com.mavidenergy.domains.Pessoa;
import br.com.mavidenergy.gateways.exceptions.ConsultaNotFoundException;
import br.com.mavidenergy.gateways.repositories.ConsultaRepository;
import br.com.mavidenergy.usecases.interfaces.BuscarConsulta;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class BuscarConsultaImpl implements BuscarConsulta {

    private final ConsultaRepository consultaRepository;

    @Override
    public Consulta buscarPorId(String id) {
        Consulta consulta = consultaRepository.findById(id)
                .orElseThrow(() -> new ConsultaNotFoundException("Consulta com id " + id + " não encontrada"));

        return consulta;
    }

    @Override
    public List<Consulta> buscarPorPessoa(Pessoa pessoa) {

        List<Consulta> consultasPorPessoa = consultaRepository.findConsultasByPessoaOrderByDataCriacaoDesc(pessoa);

        if (consultasPorPessoa.isEmpty()) {
            throw new ConsultaNotFoundException("Nenhuma consulta encontrada para a pessoa informada");
        }

        return consultasPorPessoa;
    }


}
