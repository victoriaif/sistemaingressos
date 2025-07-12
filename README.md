# sistemaingressos

Sistema de Ingressos: Aplicação web para compra e gerenciamento de ingressos para eventos, desenvolvida com Spring Boot, JPA, Thymeleaf, htmx e Tailwind CSS. O projeto foi criado para a disciplina de Desenvolvimento de Aplicações Web 1.
# 🎟️ Show Go - Sistema de Venda de Ingressos

Sistema web para gerenciamento de venda de ingressos para shows, com controle completo de eventos, compra, relatórios e autenticação.

## Tecnologias Utilizadas

- Java 17  
- Spring Boot 3  
- Spring Security  
- Spring Data JPA  
- Thymeleaf  
- HTMX  
- TailwindCSS  
- PostgreSQL  
- JasperReports (relatórios PDF)  
- HTTPS com certificado autoassinado (ambiente local)  

## Pré-requisitos

- Java 17+  
- Maven 3.8+  
- PostgreSQL rodando  
- Node.js (para build do TailwindCSS)  
- Navegador moderno (Chrome, Firefox, Edge etc.)  

## Configuração do Banco de Dados

Configure o arquivo `application.properties` com suas credenciais PostgreSQL:

properties
- spring.datasource.url=jdbc:postgresql://localhost:5432/sistemaingressos
- spring.datasource.username=seu_usuario
- spring.datasource.password=sua_senha
- spring.jpa.hibernate.ddl-auto=update

## Segurança e Perfis de Usuário

O sistema utiliza autenticação baseada em papéis (roles) para controle de acesso às funcionalidades:

| Papel    | Permissões principais                           |
| -------- | ---------------------------------------------- |
| ADMIN   | Gerencia usuários, eventos, vendas e relatórios |
| USUARIO | Compra ingressos, viualiza ingressos e eventos |

## Funcionalidades
- Login com controle de acesso baseado em papéis

- Cadastro e gerenciamento de eventos

- Compra de ingressos por usuários autenticados

- Visualização de eventos disponíveis

- Listagem de ingressos comprados

- Geração de relatórios em PDF com JasperReports

- Interface dinâmica com HTMX

- Layout responsivo com TailwindCSS

## Criadores
@victoriaif @madicr


