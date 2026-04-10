package org.aep.observacao.model;

import java.util.Objects;

public class Categoria {
    private final String nome;
    private final int slaDias;

    public Categoria(String nome, int slaDias) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("O nome da categoria é obrigatório.");
        }
        if (slaDias <= 0) {
            throw new IllegalArgumentException("O SLA da categoria deve ser maior que zero.");
        }
        this.nome = nome.trim();
        this.slaDias = slaDias;
    }

    public String getNome() {
        return nome;
    }

    public int getSlaDias() {
        return slaDias;
    }

    @Override
    public String toString() {
        return nome + " (SLA: " + slaDias + " dia" + (slaDias > 1 ? "s" : "") + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Categoria categoria)) return false;
        return slaDias == categoria.slaDias && nome.equalsIgnoreCase(categoria.nome);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nome.toLowerCase(), slaDias);
    }
}
