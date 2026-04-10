package org.aep.observacao.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class HistoricoStatus {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private final int id;
    private final int solicitacaoId;
    private final Status status;
    private final LocalDateTime data;
    private final String responsavel;
    private final String comentario;

    public HistoricoStatus(int id, int solicitacaoId, Status status, LocalDateTime data, String responsavel, String comentario) {
        this.id = id;
        this.solicitacaoId = solicitacaoId;
        this.status = status;
        this.data = data;
        this.responsavel = responsavel;
        this.comentario = comentario;
    }

    public int getId() {
        return id;
    }

    public int getSolicitacaoId() {
        return solicitacaoId;
    }

    public Status getStatus() {
        return status;
    }

    public LocalDateTime getData() {
        return data;
    }

    public String getResponsavel() {
        return responsavel;
    }

    public String getComentario() {
        return comentario;
    }

    @Override
    public String toString() {
        return FORMATTER.format(data) + " | " + status + " | " + responsavel + " | " + comentario;
    }
}
