package view;

public class MenuPrincipal {
    public void menu() {
        String[] itens = {"Vendedor", "Venda", "Relatórios", "Finalizar"};
        String opcao;

        do {
            opcao = Tela.escolher("Menu Principal", itens);
            if (opcao == null) {
                break; // clicou em Cancelar ou fechou a janela
            }
            switch (opcao) {
                case "Vendedor" -> new MenuVendedor().menu();
                case "Venda" -> new MenuVenda().menu();
                case "Relatórios" -> new MenuRelatorio().menu();
                default -> { }
            }
        } while (!"Finalizar".equals(opcao));
    }
}
