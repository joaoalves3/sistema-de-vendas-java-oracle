package dao;

import factory.ConnectionFactory;
import model.TotalPorVendedor;
import model.Venda;
import model.Vendedor;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class VendaDAO implements GenericDAO<Venda, Integer> {

    // Busca a venda junto com o nome do vendedor (INNER JOIN)
    private static final String SELECT_VENDA =
            "select vd.id, vd.total, vd.data, v.id as id_vendedor, v.nome " +
            "from java_venda vd " +
            "inner join java_vendedor v on v.id = vd.id_vendedor ";

    @Override
    public void inserir(Venda entidade) {
        String sql = "insert into java_venda(id_vendedor, total, data) values(?, ?, ?)";
        try (Connection connection = ConnectionFactory.obterConexao();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, entidade.getVendedor().getId());
            ps.setDouble(2, entidade.getTotal());
            ps.setDate(3, Date.valueOf(entidade.getData()));
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Erro ao inserir venda", e);
        }
    }

    @Override
    public List<Venda> listar() {
        List<Venda> lista = new ArrayList<>();
        String sql = SELECT_VENDA + "order by vd.data desc, vd.id";
        try (Connection connection = ConnectionFactory.obterConexao();
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(montar(rs));
            }
        } catch (SQLException e) {
            throw new DAOException("Erro ao listar vendas", e);
        }
        return lista;
    }

    @Override
    public Optional<Venda> pesquisar(Integer id) {
        String sql = SELECT_VENDA + "where vd.id = ?";
        try (Connection connection = ConnectionFactory.obterConexao();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(montar(rs));
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Erro ao pesquisar venda", e);
        }
        return Optional.empty();
    }

    @Override
    public void atualizar(Venda venda) {
        String sql = "update java_venda set id_vendedor = ?, total = ?, data = ? where id = ?";
        try (Connection connection = ConnectionFactory.obterConexao();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, venda.getVendedor().getId());
            ps.setDouble(2, venda.getTotal());
            ps.setDate(3, Date.valueOf(venda.getData()));
            ps.setInt(4, venda.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Erro ao atualizar venda", e);
        }
    }

    @Override
    public void excluir(Integer id) {
        String sql = "delete from java_venda where id = ?";
        try (Connection connection = ConnectionFactory.obterConexao();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Erro ao excluir venda", e);
        }
    }

    /**
     * Relatório: quantidade de vendas e total vendido por vendedor (GROUP BY).
     * Usa LEFT JOIN para que vendedores sem nenhuma venda também apareçam, com zero.
     */
    public List<TotalPorVendedor> totalPorVendedor() {
        List<TotalPorVendedor> lista = new ArrayList<>();
        String sql = "select v.nome, count(vd.id) as qtd, coalesce(sum(vd.total), 0) as total " +
                "from java_vendedor v " +
                "left join java_venda vd on vd.id_vendedor = v.id " +
                "group by v.id, v.nome " +
                "order by total desc";
        try (Connection connection = ConnectionFactory.obterConexao();
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new TotalPorVendedor(
                        rs.getString("nome"), rs.getInt("qtd"), rs.getDouble("total")));
            }
        } catch (SQLException e) {
            throw new DAOException("Erro ao gerar relatório de total por vendedor", e);
        }
        return lista;
    }

    /**
     * Relatório: vendedores que ainda não fizeram nenhuma venda.
     * LEFT JOIN + IS NULL: tudo da tabela da esquerda que não tem par na da direita.
     */
    public List<Vendedor> vendedoresSemVenda() {
        List<Vendedor> lista = new ArrayList<>();
        String sql = "select v.id, v.nome " +
                "from java_vendedor v " +
                "left join java_venda vd on vd.id_vendedor = v.id " +
                "where vd.id is null " +
                "order by v.nome";
        try (Connection connection = ConnectionFactory.obterConexao();
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Vendedor vendedor = new Vendedor();
                vendedor.setId(rs.getInt("id"));
                vendedor.setNome(rs.getString("nome"));
                lista.add(vendedor);
            }
        } catch (SQLException e) {
            throw new DAOException("Erro ao gerar relatório de vendedores sem venda", e);
        }
        return lista;
    }

    private Venda montar(ResultSet rs) throws SQLException {
        Vendedor vendedor = new Vendedor();
        vendedor.setId(rs.getInt("id_vendedor"));
        vendedor.setNome(rs.getString("nome"));

        Venda venda = new Venda();
        venda.setId(rs.getInt("id"));
        venda.setTotal(rs.getDouble("total"));
        venda.setData(rs.getDate("data").toLocalDate());
        venda.setVendedor(vendedor);
        return venda;
    }
}
