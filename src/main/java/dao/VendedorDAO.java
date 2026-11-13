package dao;

import factory.ConnectionFactory;
import model.Vendedor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class VendedorDAO implements GenericDAO<Vendedor, Integer> {

    @Override
    public void inserir(Vendedor entidade) {
        String sql = "insert into java_vendedor(nome) values(?)";
        try (Connection connection = ConnectionFactory.obterConexao();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, entidade.getNome());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Erro ao inserir vendedor", e);
        }
    }

    @Override
    public List<Vendedor> listar() {
        List<Vendedor> lista = new ArrayList<>();
        String sql = "select id, nome from java_vendedor order by nome";
        try (Connection connection = ConnectionFactory.obterConexao();
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(montar(rs));
            }
        } catch (SQLException e) {
            throw new DAOException("Erro ao listar vendedores", e);
        }
        return lista;
    }

    @Override
    public Optional<Vendedor> pesquisar(Integer id) {
        String sql = "select id, nome from java_vendedor where id = ?";
        try (Connection connection = ConnectionFactory.obterConexao();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(montar(rs));
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Erro ao pesquisar vendedor", e);
        }
        return Optional.empty();
    }

    @Override
    public void atualizar(Vendedor vendedor) {
        String sql = "update java_vendedor set nome = ? where id = ?";
        try (Connection connection = ConnectionFactory.obterConexao();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, vendedor.getNome());
            ps.setInt(2, vendedor.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Erro ao atualizar vendedor", e);
        }
    }

    @Override
    public void excluir(Integer id) {
        String sql = "delete from java_vendedor where id = ?";
        try (Connection connection = ConnectionFactory.obterConexao();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            // ORA-02292: o vendedor ainda tem vendas ligadas a ele (chave estrangeira)
            throw new DAOException("Não foi possível excluir o vendedor (ele tem vendas cadastradas?)", e);
        }
    }

    private Vendedor montar(ResultSet rs) throws SQLException {
        Vendedor vendedor = new Vendedor();
        vendedor.setId(rs.getInt("id"));
        vendedor.setNome(rs.getString("nome"));
        return vendedor;
    }
}
