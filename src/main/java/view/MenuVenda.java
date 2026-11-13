package view;

import dao.VendaDAO;
import dao.VendedorDAO;
import model.Venda;
import model.Vendedor;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class MenuVenda {
    private final VendaDAO dao = new VendaDAO();

    public void menu() {
        String[] itens = {"Inserir", "Listar", "Pesquisar", "Atualizar", "Excluir", "Voltar"};
        String opcao;

        do {
            opcao = Tela.escolher("Menu Venda", itens);
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
        Venda venda = new Venda();
        if (preencher(venda)) {
            dao.inserir(venda);
            Tela.mostrar("Venda cadastrada!");
        }
    }

    private void listar() {
        Tela.mostrar("Vendas (ID | Vendedor | Total | Data)", dao.listar());
    }

    private void pesquisar() {
        buscarPorId().ifPresent(v -> Tela.mostrar("Encontrada: " + v));
    }

    private void atualizar() {
        Optional<Venda> encontrada = buscarPorId();
        if (encontrada.isPresent() && preencher(encontrada.get())) {
            dao.atualizar(encontrada.get());
            Tela.mostrar("Venda atualizada!");
        }
    }

    private void excluir() {
        Optional<Venda> encontrada = buscarPorId();
        if (encontrada.isPresent() && Tela.confirmar("Excluir a venda " + encontrada.get() + "?")) {
            dao.excluir(encontrada.get().getId());
            Tela.mostrar("Venda excluída!");
        }
    }

    /** Pede vendedor, total e data. Devolve false se a pessoa cancelar no meio. */
    private boolean preencher(Venda venda) {
        List<Vendedor> vendedores = new VendedorDAO().listar();
        if (vendedores.isEmpty()) {
            Tela.mostrar("Cadastre um vendedor antes de lançar vendas.");
            return false;
        }
        Vendedor vendedor = Tela.escolherItem("Vendedor", vendedores);
        if (vendedor == null) {
            return false;
        }
        Double total = Tela.lerValor("Total da venda (R$)");
        if (total == null) {
            return false;
        }
        LocalDate data = Tela.lerData("Data da venda");
        if (data == null) {
            return false;
        }
        venda.setVendedor(vendedor);
        venda.setTotal(total);
        venda.setData(data);
        return true;
    }

    private Optional<Venda> buscarPorId() {
        Integer id = Tela.lerInteiro("ID da venda");
        if (id == null) {
            return Optional.empty();
        }
        Optional<Venda> venda = dao.pesquisar(id);
        if (venda.isEmpty()) {
            Tela.mostrar("Nenhuma venda com o ID " + id + ".");
        }
        return venda;
    }
}
