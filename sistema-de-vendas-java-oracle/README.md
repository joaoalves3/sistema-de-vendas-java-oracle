# Sistema de Vendas — Java + Oracle

Sistema desktop para cadastrar vendedores e vendas em um banco **Oracle**, com relatórios em **SQL**. Projeto desenvolvido na disciplina de Programação Orientada a Objetos do curso de Sistemas de Informação da **FIAP** e ampliado por mim.

## O que o sistema faz

- **Vendedores:** inserir, listar, pesquisar por ID, atualizar e excluir.
- **Vendas:** inserir, listar, pesquisar por ID, atualizar e excluir. Cada venda é ligada a um vendedor.
- **Relatórios:**
  - **Total por vendedor:** quantidade de vendas e valor total de cada vendedor (`LEFT JOIN` + `GROUP BY`).
  - **Vendedores sem venda:** quem ainda não vendeu nada (`LEFT JOIN` + `IS NULL`).
- Validação do que é digitado (números, valores e datas) e tratamento do botão **Cancelar**, sem travar o programa.

## Tecnologias

- Java 17+
- Oracle Database e JDBC (`ojdbc11`)
- Maven
- Swing (`JOptionPane`) para as telas

## Estrutura

```
src/main/java
├── Main.java                 # ponto de entrada
├── factory/ConnectionFactory # abre a conexão com o Oracle
├── model/                    # Vendedor, Venda, TotalPorVendedor
├── dao/                      # acesso ao banco (padrão DAO): CRUD e relatórios
└── view/                     # menus e telas
sql/script.sql                # criação das tabelas, dados de exemplo e consultas
```

## Consultas SQL de destaque

Total vendido por vendedor, incluindo quem não vendeu nada:

```sql
SELECT v.nome, COUNT(vd.id) AS qtd, COALESCE(SUM(vd.total), 0) AS total
FROM java_vendedor v
LEFT JOIN java_venda vd ON vd.id_vendedor = v.id
GROUP BY v.id, v.nome
ORDER BY total DESC;
```

Vendedores sem nenhuma venda:

```sql
SELECT v.id, v.nome
FROM java_vendedor v
LEFT JOIN java_venda vd ON vd.id_vendedor = v.id
WHERE vd.id IS NULL;
```

## Como rodar

1. Rode o arquivo `sql/script.sql` no SQL Developer para criar as tabelas. Se elas já existirem, pule a parte do `CREATE TABLE`.
2. Copie `src/main/resources/database.properties.example` para `src/main/resources/database.properties` e preencha seu usuário e sua senha do Oracle. Esse arquivo fica fora do GitHub (está no `.gitignore`).
3. Abra o projeto no IntelliJ e rode a classe `Main`.
