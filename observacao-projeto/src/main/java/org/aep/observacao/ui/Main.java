package org.aep.observacao.ui;

import org.aep.observacao.model.*;
import org.aep.observacao.service.FilaAtendimento;
import org.aep.observacao.service.ServicoSolicitacoes;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Main {
    private static final ServicoSolicitacoes servico = new ServicoSolicitacoes();
    private static final FilaAtendimento fila = new FilaAtendimento(servico);
    private static final List<Categoria> categorias = Arrays.asList(
            new Categoria("Iluminação", 7),
            new Categoria("Buraco", 5),
            new Categoria("Limpeza", 3),
            new Categoria("Saúde", 1),
            new Categoria("Segurança Escolar", 2)
    );
    private static final Scanner scanner = new Scanner(System.in);
    private static int nextUsuarioId = 1;

    public static void main(String[] args) {
        while (true) {
            exibirCabecalho("ObservAção");
            System.out.println("1. Área do cidadão");
            System.out.println("2. Área do servidor público");
            System.out.println("0. Sair");

            int opcao = lerOpcao("Escolha uma opção: ", 0, 2);
            switch (opcao) {
                case 1 -> menuCliente();
                case 2 -> menuServidor();
                case 0 -> {
                    System.out.println("Encerrando o sistema...");
                    return;
                }
            }
        }
    }

    private static void menuCliente() {
        while (true) {
            exibirCabecalho("Área do cidadão");
            System.out.println("1. Registrar nova solicitação");
            System.out.println("2. Consultar minhas solicitações identificadas");
            System.out.println("3. Consultar solicitação por protocolo");
            System.out.println("0. Voltar");

            int opcao = lerOpcao("Escolha uma opção: ", 0, 3);
            switch (opcao) {
                case 1 -> cadastrarSolicitacao();
                case 2 -> consultarSolicitacoesIdentificadas();
                case 3 -> consultarSolicitacaoPorCodigo();
                case 0 -> {
                    return;
                }
            }
        }
    }

    private static void cadastrarSolicitacao() {
        exibirCabecalho("Cadastro de solicitação");

        Categoria categoria = lerCategoria();
        String descricao = lerTextoObrigatorio("Descreva o problema: ", 10);
        String localizacao = lerTextoObrigatorio("Informe a localização ou bairro: ", 3);
        Prioridade prioridade = lerPrioridade();
        boolean anonimo = lerSimOuNao("Deseja registrar como anônimo? (S/N): ");

        Usuario usuario = null;
        if (!anonimo) {
            System.out.println("Informe seus dados para acompanhamento identificado:");
            String nome = lerTextoObrigatorio("Nome: ", 3);
            String email = lerTextoObrigatorio("Email: ", 5);
            String telefone = lerTextoObrigatorio("Telefone: ", 8);
            usuario = new Usuario(nextUsuarioId++, nome, email, telefone);
        }

        try {
            Solicitacao solicitacao = servico.criarSolicitacao(categoria, descricao, localizacao, prioridade, usuario, anonimo);
            System.out.println("\nSolicitação registrada com sucesso.");
            System.out.println("Protocolo gerado: " + solicitacao.getProtocolo());
            System.out.println("Guarde esse protocolo para consultar o andamento.");
        } catch (IllegalArgumentException e) {
            System.out.println("Erro ao cadastrar: " + e.getMessage());
        }

        pausar();
    }

    private static void consultarSolicitacoesIdentificadas() {
        exibirCabecalho("Consulta de solicitações identificadas");
        String email = lerTextoObrigatorio("Digite o email usado no cadastro: ", 5);

        List<Solicitacao> minhasSolicitacoes = servico.listarSolicitacoes(null, null, null).stream()
                .filter(s -> !s.isAnonimo())
                .filter(s -> s.getUsuario() != null && s.getUsuario().getEmail().equalsIgnoreCase(email.trim()))
                .collect(Collectors.toList());

        if (minhasSolicitacoes.isEmpty()) {
            System.out.println("Nenhuma solicitação identificada foi encontrada para esse email.");
            pausar();
            return;
        }

        System.out.println("Solicitações encontradas:");
        for (int i = 0; i < minhasSolicitacoes.size(); i++) {
            Solicitacao s = minhasSolicitacoes.get(i);
            System.out.printf("%d. %s | %s | %s%n", i + 1, s.getProtocolo(), s.getStatus(), s.getCategoria().getNome());
        }

        int escolha = lerOpcao("Escolha uma solicitação para ver os detalhes (0 para voltar): ", 0, minhasSolicitacoes.size());
        if (escolha == 0) {
            return;
        }

        Solicitacao solicitacao = minhasSolicitacoes.get(escolha - 1);
        mostrarDetalhesSolicitacao(solicitacao);
        pausar();
    }

    private static void consultarSolicitacaoPorCodigo() {
        exibirCabecalho("Consulta por protocolo");
        String protocolo = lerTextoObrigatorio("Informe o protocolo da solicitação: ", 4);

        Solicitacao solicitacao = servico.buscarPorProtocolo(protocolo);
        if (solicitacao == null) {
            System.out.println("Nenhuma solicitação foi encontrada com esse protocolo.");
            pausar();
            return;
        }

        mostrarDetalhesSolicitacao(solicitacao);
        pausar();
    }

    private static void menuServidor() {
        while (true) {
            exibirCabecalho("Área do servidor público");
            System.out.println("1. Listar solicitações");
            System.out.println("2. Atualizar status de uma solicitação");
            System.out.println("3. Ver detalhes de uma solicitação");
            System.out.println("0. Voltar");

            int opcao = lerOpcao("Escolha uma opção: ", 0, 3);
            switch (opcao) {
                case 1 -> listarSolicitacoes();
                case 2 -> atualizarStatus();
                case 3 -> verDetalhes();
                case 0 -> {
                    return;
                }
            }
        }
    }

    private static void listarSolicitacoes() {
        exibirCabecalho("Listagem de solicitações");
        System.out.println("1. Todas");
        System.out.println("2. Filtrar por prioridade");
        System.out.println("3. Filtrar por bairro");
        System.out.println("4. Filtrar por categoria");

        int filtro = lerOpcao("Escolha um filtro: ", 1, 4);
        List<Solicitacao> lista;

        switch (filtro) {
            case 1 -> lista = servico.listarSolicitacoes(null, null, null);
            case 2 -> lista = fila.getFilaPorPrioridade(lerPrioridade());
            case 3 -> {
                String bairro = lerTextoObrigatorio("Digite o bairro ou parte da localização: ", 2);
                lista = fila.getFilaPorBairro(bairro);
            }
            case 4 -> lista = fila.getFilaPorCategoria(lerCategoria());
            default -> throw new IllegalStateException("Filtro inesperado.");
        }

        if (lista.isEmpty()) {
            System.out.println("Nenhuma solicitação encontrada para esse filtro.");
            pausar();
            return;
        }

        System.out.println("\nResultados encontrados:");
        for (Solicitacao s : lista) {
            System.out.printf("%s | %s | %s | %s%n",
                    s.getProtocolo(),
                    s.getStatus(),
                    s.getPrioridade(),
                    s.getCategoria().getNome());
        }
        pausar();
    }

    private static void atualizarStatus() {
        exibirCabecalho("Atualização de status");
        String protocolo = lerTextoObrigatorio("Informe o protocolo: ", 4);
        Solicitacao solicitacao = servico.buscarPorProtocolo(protocolo);

        if (solicitacao == null) {
            System.out.println("Solicitação não encontrada.");
            pausar();
            return;
        }

        System.out.println("Status atual: " + solicitacao.getStatus());
        Status novoStatus = lerNovoStatus();
        String responsavel = lerTextoObrigatorio("Nome do responsável pela atualização: ", 3);
        String comentario = lerTextoObrigatorio("Comentário obrigatório da movimentação: ", 5);

        try {
            boolean sucesso = servico.atualizarStatus(solicitacao.getId(), novoStatus, responsavel, comentario);
            System.out.println(sucesso ? "Status atualizado com sucesso." : "Não foi possível atualizar o status.");
        } catch (IllegalArgumentException e) {
            System.out.println("Erro ao atualizar: " + e.getMessage());
        }
        pausar();
    }

    private static void verDetalhes() {
        exibirCabecalho("Detalhes da solicitação");
        String protocolo = lerTextoObrigatorio("Informe o protocolo: ", 4);
        Solicitacao solicitacao = servico.buscarPorProtocolo(protocolo);

        if (solicitacao == null) {
            System.out.println("Solicitação não encontrada.");
            pausar();
            return;
        }

        mostrarDetalhesSolicitacao(solicitacao);
        pausar();
    }

    private static void mostrarDetalhesSolicitacao(Solicitacao solicitacao) {
        System.out.println("\n--- Detalhes da solicitação ---");
        System.out.println(solicitacao);
        System.out.println("\nHistórico de movimentações:");
        List<HistoricoStatus> historico = servico.getHistorico(solicitacao.getId());
        if (historico.isEmpty()) {
            System.out.println("Nenhuma movimentação registrada.");
        } else {
            historico.forEach(System.out::println);
        }
    }

    private static Categoria lerCategoria() {
        while (true) {
            System.out.println("Categorias disponíveis:");
            for (int i = 0; i < categorias.size(); i++) {
                System.out.printf("%d. %s%n", i + 1, categorias.get(i));
            }
            int indice = lerOpcao("Escolha a categoria: ", 1, categorias.size());
            return categorias.get(indice - 1);
        }
    }

    private static Prioridade lerPrioridade() {
        System.out.println("Prioridades disponíveis:");
        System.out.println("1. BAIXA");
        System.out.println("2. MEDIA");
        System.out.println("3. ALTA");
        int opcao = lerOpcao("Escolha a prioridade: ", 1, 3);
        return Prioridade.values()[opcao - 1];
    }

    private static Status lerNovoStatus() {
        System.out.println("Novos status possíveis:");
        System.out.println("1. TRIAGEM");
        System.out.println("2. EM_EXECUCAO");
        System.out.println("3. RESOLVIDO");
        System.out.println("4. ENCERRADO");
        int opcao = lerOpcao("Escolha o novo status: ", 1, 4);
        return switch (opcao) {
            case 1 -> Status.TRIAGEM;
            case 2 -> Status.EM_EXECUCAO;
            case 3 -> Status.RESOLVIDO;
            case 4 -> Status.ENCERRADO;
            default -> throw new IllegalStateException("Status inesperado.");
        };
    }

    private static int lerOpcao(String mensagem, int minimo, int maximo) {
        while (true) {
            System.out.print(mensagem);
            String entrada = scanner.nextLine().trim();
            try {
                int valor = Integer.parseInt(entrada);
                if (valor < minimo || valor > maximo) {
                    System.out.println("Digite um número entre " + minimo + " e " + maximo + ".");
                    continue;
                }
                return valor;
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida. Digite apenas números.");
            }
        }
    }

    private static String lerTextoObrigatorio(String mensagem, int tamanhoMinimo) {
        while (true) {
            System.out.print(mensagem);
            String texto = scanner.nextLine().trim();
            if (texto.length() < tamanhoMinimo) {
                System.out.println("O texto precisa ter pelo menos " + tamanhoMinimo + " caracteres.");
                continue;
            }
            return texto;
        }
    }

    private static boolean lerSimOuNao(String mensagem) {
        while (true) {
            System.out.print(mensagem);
            String resposta = scanner.nextLine().trim().toUpperCase();
            if (resposta.equals("S")) {
                return true;
            }
            if (resposta.equals("N")) {
                return false;
            }
            System.out.println("Resposta inválida. Digite S para sim ou N para não.");
        }
    }

    private static void exibirCabecalho(String titulo) {
        System.out.println("\n========================================");
        System.out.println(titulo);
        System.out.println("========================================");
    }

    private static void pausar() {
        System.out.println("\nPressione ENTER para continuar...");
        scanner.nextLine();
    }
}
