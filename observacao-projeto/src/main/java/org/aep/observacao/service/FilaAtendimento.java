package org.aep.observacao.service;

import org.aep.observacao.model.Categoria;
import org.aep.observacao.model.Prioridade;
import org.aep.observacao.model.Solicitacao;

import java.util.List;

public class FilaAtendimento {
    private final ServicoSolicitacoes servico;

    public FilaAtendimento(ServicoSolicitacoes servico) {
        this.servico = servico;
    }

    public List<Solicitacao> getFilaPorPrioridade(Prioridade prioridade) {
        return servico.listarSolicitacoes(prioridade, null, null);
    }

    public List<Solicitacao> getFilaPorBairro(String bairro) {
        return servico.listarSolicitacoes(null, bairro, null);
    }

    public List<Solicitacao> getFilaPorCategoria(Categoria categoria) {
        return servico.listarSolicitacoes(null, null, categoria);
    }
}
