package view;

import dao.VendedorDAO;
import model.Vendedor;

import java.util.Optional;

public class MenuVendedor {
    private final VendedorDAO dao = new VendedorDAO();

    public void menu() {
        String[] itens = {"Inserir", "Listar", "Pesquisar", "Atualizar", "Excluir", "Voltar"};
        String opcao;

        do {
            opcao = Tela.escolher("Menu Vendedor", itens);
            if (opcao == null) {
                break;
            }
            switch (opcao) {
                case "Inserir" -> Tela.executar(this::inserir);
                case "Listar" -> Tela.executar(this::listar);
                case "Pesquisar" -> Tela.executar(this::pesquisar);
                case "Atualizar" -> Tela.executar(this::atualizar);
                case "Excluir" -> Tela.executar(this::excluir);
                default -> { }
            }
        } while (!"Voltar".equals(opcao));
    }

    private void inserir() {
        String nome = Tela.lerTexto("Nome do vendedor");
        if (nome == null) {
            return;
        }
        Vendedor vendedor = new Vendedor();
        vendedor.setNome(nome);
        dao.inserir(vendedor);
        Tela.mostrar("Vendedor cadastrado!");
    }

    private void listar() {
        Tela.mostrar("Vendedores (ID | Nome)", dao.listar());
    }

    private void pesquisar() {
        buscarPorId().ifPresent(v -> Tela.mostrar("Encontrado: " + v));
    }

    private void atualizar() {
        Optional<Vendedor> encontrado = buscarPorId();
        if (encontrado.isEmpty()) {
            return;
        }
        Vendedor vendedor = encontrado.get();
        String nome = Tela.lerTexto("Novo nome (atual: " + vendedor.getNome() + ")");
        if (nome == null) {
            return;
        }
        vendedor.setNome(nome);
        dao.atualizar(vendedor);
        Tela.mostrar("Vendedor atualizado!");
    }

    private void excluir() {
        Optional<Vendedor> encontrado = buscarPorId();
        if (encontrado.isPresent()
                && Tela.confirmar("Excluir o vendedor " + encontrado.get().getNome() + "?")) {
            dao.excluir(encontrado.get().getId());
            Tela.mostrar("Vendedor excluído!");
        }
    }

    /** Pede o ID e busca o vendedor. Avisa se não encontrar. */
    private Optional<Vendedor> buscarPorId() {
        Integer id = Tela.lerInteiro("ID do vendedor");
        if (id == null) {
            return Optional.empty();
        }
        Optional<Vendedor> vendedor = dao.pesquisar(id);
        if (vendedor.isEmpty()) {
            Tela.mostrar("Nenhum vendedor com o ID " + id + ".");
        }
        return vendedor;
    }
}
