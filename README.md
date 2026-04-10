# ObservAção - Sistema de Solicitações Públicas

Sistema desenvolvido em Java para registro e acompanhamento de solicitações de serviços públicos, como iluminação, buracos, limpeza e saúde.

## Funcionalidades

- Cadastro de solicitações (com opção anônima)
- Geração de protocolo automático
- Acompanhamento de status
- Histórico de movimentações
- Filtros por prioridade, bairro e categoria
- Atualização de status com comentário obrigatório
- Persistência em banco de dados H2

---

##  Tecnologias utilizadas

- Java (POO)
- JDBC
- Banco de dados H2
- CLI (interface em console)

---

## ▶️ Como executar o projeto

### 1. Clonar o repositório
git clone https://github.com/seu-usuario/observacao.git

## 2. Abrir no IntelliJ (ou outra IDE)
Abra a pasta do projeto
Aguarde a IDE reconhecer o projeto

## 3. Rodar o sistema

Execute a classe:
org.aep.observacao.ui.Main

## Banco de Dados
O sistema utiliza banco H2 local.

Os dados são salvos automaticamente na pasta persistence/
Não é necessário configurar nada manualmente

## Estrutura do projeto
model/    → entidades do sistema
service/  → regras de negócio e banco de dados
ui/       → interface (menu em console)

## Objetivo
O objetivo do sistema é reduzir barreiras no acesso a serviços públicos, aumentar a transparência e permitir acompanhamento claro das demandas da população.
