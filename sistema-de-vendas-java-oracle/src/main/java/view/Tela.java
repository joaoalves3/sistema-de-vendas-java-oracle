package view;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

import static javax.swing.JOptionPane.*;

/**
 * Funções de tela reaproveitadas pelos menus: escolher opção, ler dados e mostrar mensagens.
 * Todas tratam o botão "Cancelar" (que devolve null) sem deixar o programa quebrar.
 */
public final class Tela {
    private static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private Tela() { }

    /** Mostra uma lista de opções. Devolve null se a pessoa cancelar. */
    public static String escolher(String titulo, String[] opcoes) {
        return (String) showInputDialog(null, "Selecione uma opção", titulo,
                QUESTION_MESSAGE, null, opcoes, opcoes[0]);
    }

    /** Mostra uma lista de objetos (ex.: vendedores). Devolve null se cancelar. */
    public static <T> T escolherItem(String titulo, List<T> itens) {
        @SuppressWarnings("unchecked")
        T escolhido = (T) showInputDialog(null, "Selecione", titulo,
                QUESTION_MESSAGE, null, itens.toArray(), itens.get(0));
        return escolhido;
    }

    public static String lerTexto(String pergunta) {
        String texto = showInputDialog(pergunta);
        if (texto == null || texto.isBlank()) {
            return null;
        }
        return texto.trim();
    }

    public static Integer lerInteiro(String pergunta) {
        while (true) {
            String texto = lerTexto(pergunta);
            if (texto == null) {
                return null;
            }
            try {
                return Integer.parseInt(texto);
            } catch (NumberFormatException e) {
                erro("Digite um número inteiro.");
            }
        }
    }

    public static Double lerValor(String pergunta) {
        while (true) {
            String texto = lerTexto(pergunta);
            if (texto == null) {
                return null;
            }
            try {
                double valor = Double.parseDouble(texto.replace(",", "."));
                if (valor >= 0) {
                    return valor;
                }
                erro("O valor não pode ser negativo.");
            } catch (NumberFormatException e) {
                erro("Digite um valor válido. Exemplo: 150,90");
            }
        }
    }

    public static LocalDate lerData(String pergunta) {
        while (true) {
            String texto = lerTexto(pergunta + " (dd/mm/aaaa)");
            if (texto == null) {
                return null;
            }
            try {
                return LocalDate.parse(texto, FORMATO);
            } catch (DateTimeParseException e) {
                erro("Data inválida. Use o formato dd/mm/aaaa.");
            }
        }
    }

    public static void mostrar(String titulo, List<?> linhas) {
        if (linhas.isEmpty()) {
            mostrar("Nenhum registro encontrado.");
            return;
        }
        StringBuilder texto = new StringBuilder();
        for (Object linha : linhas) {
            texto.append(linha).append("\n");
        }
        showMessageDialog(null, texto.toString(), titulo, INFORMATION_MESSAGE);
    }

    public static void mostrar(String mensagem) {
        showMessageDialog(null, mensagem);
    }

    public static boolean confirmar(String pergunta) {
        return showConfirmDialog(null, pergunta, "Confirmar", YES_NO_OPTION) == YES_OPTION;
    }

    public static void erro(String mensagem) {
        showMessageDialog(null, mensagem, "Erro", ERROR_MESSAGE);
    }

    /** Executa uma ação e mostra o erro na tela, em vez de derrubar o programa. */
    public static void executar(Runnable acao) {
        try {
            acao.run();
        } catch (RuntimeException e) {
            erro(e.getMessage());
        }
    }
}
