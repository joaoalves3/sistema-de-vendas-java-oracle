package dao;

import java.util.List;
import java.util.Optional;

/**
 * Operações básicas de banco de dados (CRUD) que todo DAO do projeto implementa.
 */
public interface GenericDAO<T, ID> {
    void inserir(T entidade);
    List<T> listar();
    Optional<T> pesquisar(ID id);
    void atualizar(T entidade);
    void excluir(ID id);
}
