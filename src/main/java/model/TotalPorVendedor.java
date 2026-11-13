package model;

/**
 * Uma linha do relatório "total vendido por vendedor".
 */
public record TotalPorVendedor(String vendedor, int quantidadeVendas, double totalVendido) {
    @Override
    public String toString() {
        return String.format("%s  |  %d venda(s)  |  R$ %.2f", vendedor, quantidadeVendas, totalVendido);
    }
}
