package view;

import dao.VendaDAO;

public class MenuRelatorio {
    private final VendaDAO dao = new VendaDAO();

    public void menu() {
        String[] itens = {"Total por vendedor", "Vendedores sem venda", "Voltar"};
        String opcao;

        do {
            opcao = Tela.escolher("Relatórios", itens);
            if (opcao == null) {
                break;
            }
            switch (opcao) {
                case "Total por vendedor" -> Tela.executar(() ->
                        Tela.mostrar("Total por vendedor (Vendedor | Vendas | Total)",
                                dao.totalPorVendedor()));
                case "Vendedores sem venda" -> Tela.executar(() ->
                        Tela.mostrar("Vendedores sem nenhuma venda (ID | Nome)",
                                dao.vendedoresSemVenda()));
                default -> { }
            }
        } while (!"Voltar".equals(opcao));
    }
}
