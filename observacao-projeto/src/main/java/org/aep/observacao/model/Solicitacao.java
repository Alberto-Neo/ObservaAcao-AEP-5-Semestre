package org.aep.observacao.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Solicitacao {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private final int id;
    private final String protocolo;
    private final Categoria categoria;
    private final String descricao;
    private final String localizacao;
    private final Prioridade prioridade;
    private Status status;
    private final LocalDateTime dataCriacao;
    private final Usuario usuario;
    private final boolean anonimo;

    public Solicitacao(int id, String protocolo, Categoria categoria, String descricao, String localizacao,
                       Prioridade prioridade, Status status, LocalDateTime dataCriacao, Usuario usuario,
                       boolean anonimo) {
        this.id = id;
        this.protocolo = protocolo;
        this.categoria = categoria;
        this.descricao = descricao;
        this.localizacao = localizacao;
        this.prioridade = prioridade;
        this.status = status;
        this.dataCriacao = dataCriacao;
        this.usuario = usuario;
        this.anonimo = anonimo;
    }

    public int getId() {
        return id;
    }

    public String getProtocolo() {
        return protocolo;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getLocalizacao() {
        return localizacao;
    }

    public Prioridade getPrioridade() {
        return prioridade;
    }

    public Status getStatus() {
        return status;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public boolean isAnonimo() {
        return anonimo;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public LocalDateTime getPrazoEstimado() {
        return dataCriacao.plusDays(categoria.getSlaDias());
    }

    public boolean estaAtrasada() {
        return LocalDateTime.now().isAfter(getPrazoEstimado())
                && status != Status.RESOLVIDO
                && status != Status.ENCERRADO;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("Protocolo: ").append(protocolo)
                .append("\nCategoria: ").append(categoria.getNome())
                .append("\nDescrição: ").append(descricao)
                .append("\nLocalização: ").append(localizacao)
                .append("\nPrioridade: ").append(prioridade)
                .append("\nStatus: ").append(status)
                .append("\nData de criação: ").append(FORMATTER.format(dataCriacao))
                .append("\nPrazo estimado: ").append(FORMATTER.format(getPrazoEstimado()))
                .append("\nAnonimato: ").append(anonimo ? "Sim" : "Não");

        if (estaAtrasada()) {
            result.append("\nAtenção: solicitação em atraso.");
        }

        if (!anonimo && usuario != null) {
            result.append("\nSolicitante: ").append(usuario.getNome())
                    .append("\nEmail: ").append(usuario.getEmail())
                    .append("\nTelefone: ").append(usuario.getTelefone());
        }
        return result.toString();
    }
}
