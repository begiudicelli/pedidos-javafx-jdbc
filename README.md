# Sistema de Gerenciamento de Pedidos (JavaFX + JDBC)

## Visão Geral

Este projeto consiste em um sistema de gerenciamento de pedidos desenvolvido em Java, com interface gráfica utilizando JavaFX. A aplicação simula um cenário real de controle de pedidos, clientes e produtos, aplicando conceitos fundamentais de programação orientada a objetos e organização de software.

O foco do projeto é demonstrar a construção de uma aplicação com persistência em banco de dados e separação clara de responsabilidades.

## Arquitetura

O projeto segue o padrão arquitetural MVC (Model-View-Controller):

- Model: representa as entidades e os dados da aplicação  
- View: interface gráfica desenvolvida com JavaFX  
- Controller: responsável por intermediar as interações entre a interface e a lógica da aplicação  

Também é utilizado o padrão DAO (Data Access Object) para abstração e organização do acesso aos dados.

## Tecnologias

- Java  
- JavaFX  
- JDBC  
- SQL  
- Padrão MVC  
- Padrão DAO  

## Funcionalidades

- Cadastro e gerenciamento de pedidos  
- Gerenciamento de clientes e produtos  
- Persistência de dados em banco relacional  
- Separação entre lógica de negócio e acesso a dados  
- Interface gráfica para interação com o usuário  

## Estrutura do Projeto

O projeto está organizado para refletir a separação de responsabilidades:

- model: classes que representam as entidades do domínio  
- dao: classes responsáveis pelo acesso ao banco de dados  
- controller: lógica da aplicação e controle de fluxo  
- view: componentes da interface gráfica  

## Banco de Dados

A aplicação utiliza um banco de dados relacional acessado via JDBC.  
As operações de persistência são encapsuladas nas classes DAO, promovendo organização e manutenção do código.

## Como Executar

1. Clone o repositório:
   git clone https://github.com/begiudicelli/order-management-system-java

2. Abra o projeto em uma IDE (Eclipse, IntelliJ, etc.)

3. Configure a conexão com o banco de dados no projeto

4. Execute a classe principal da aplicação

## Objetivo

Este projeto possui cunho educacional e foi desenvolvido com o objetivo de praticar:

- Programação orientada a objetos  
- Arquitetura em camadas (MVC)  
- Integração com banco de dados utilizando JDBC  
- Organização de código com o padrão DAO  

## Possíveis Melhorias

- Migração para Spring Boot  
- Implementação de API REST  
- Separação entre backend e frontend  
- Validações mais robustas e tratamento de exceções  
- Implementação de testes automatizados  
