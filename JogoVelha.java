import java.util.Random;
import java.util.Scanner;

public class JogoVelha {
    // Estes caracteres são aceitos como caracteres para representarem
    // os jogadores. Utizado para evitar caracteres que não combinem com
    // o desenho do tabuleiro.
    final static String CARACTERES_IDENTIFICADORES_ACEITOS = "XO0UC";

    // Tamanho do tabuleiro 3x3. Para o primeiro nivel de dificuldade
    // considere que este valor não será alterado.
    final static int TAMANHO_TABULEIRO = 3;

    public static void main(String[] args) throws Exception {
        Scanner teclado = new Scanner(System.in);

        char[][] tabuleiro = new char[TAMANHO_TABULEIRO][TAMANHO_TABULEIRO];

        // TODO: Faça a inicialização do tabuleiro aqui
        tabuleiro = inicializarTabuleiro(tabuleiro);

        // Definimos aqui qual é o caractere que cada jogador irá utilizar no jogo.
        // TODO: chame as funções obterCaractereUsuario() e obterCaractereComputador
        // para definir quais caracteres da lista de caracteres aceitos que o jogador
        // quer configurar para ele e para o computador.
        char caractereUsuario = obterCaractereUsuario(teclado);
        char caractereComputador = obterCaractereComputador(teclado, caractereUsuario);

        // Esta variavel é utilizada para definir se o usuário começa a jogar ou não.
        // Valor true, usuario começa jogando, valor false computador começa.
        // TODO: obtenha o valor booleano sorteado

        boolean vezUsuarioJogar = sortearValorBooleano();

        boolean jogoContinua;

        do {
            // controla se o jogo terminou
            jogoContinua = true;
            // TODO: Exiba o tabuleiro aqui
            exibirTabuleiro(tabuleiro);

            if (vezUsuarioJogar) {
                // TODO: Execute processar vez do usuario
                tabuleiro = processarVezUsuario(teclado, tabuleiro, caractereUsuario);

                // Verifica se o usuario venceu
                // TODO: Este if deve executar apenas se teve ganhador
                if (teveGanhador(tabuleiro, caractereUsuario)) {
                    exibirVitoriaUsuario();
                    jogoContinua = false;
                }

                // TODO: defina qual o vaor a variavel abaixo deve possuir
                vezUsuarioJogar = false;
            } else {
                // TODO: Execute processar vez do computador
                tabuleiro = processarVezComputador(tabuleiro, caractereComputador);

                // Verifica se o computador venceu
                // TODO: Este if deve executar apenas se teve ganhador
                if (teveGanhador(tabuleiro, caractereComputador)) {
                    exibirVitoriaComputador();
                    jogoContinua = false;
                }

                // TODO: defina qual o vaor a variavel abaixo deve possuir
                vezUsuarioJogar = true;
            }

            if (teveEmpate(tabuleiro)) {
                // TODO: Exiba que ocorreu empate
                exibirEmpate();

                jogoContinua = false;
            }

        } while (jogoContinua);

        teclado.close();
    }

    // ===================== FIM MAIN =======================

    static char[][] inicializarTabuleiro(char[][] tabuleiro) {
        for (int i = 0; i < tabuleiro.length; i++) {
            for (int j = 0; j < tabuleiro.length; j++) {
                tabuleiro[i][j] = ' ';
            }
        }
        return tabuleiro;
    }

    static char obterCaractereUsuario(Scanner teclado) {
        while (true) {
            try {
                System.out.print(
                        "Digite os caracteres a serem utilizados (" + CARACTERES_IDENTIFICADORES_ACEITOS + "): ");
                char escolha = teclado.nextLine().toUpperCase().charAt(0);
                if (CARACTERES_IDENTIFICADORES_ACEITOS.contains((String.valueOf(escolha))) && escolha != ' ') {
                    return escolha;
                } else {
                    System.out.println("Esta escolha não é possivel! Tente novamente.");
                }
            } catch (StringIndexOutOfBoundsException e) {
                System.out.println("Esta escolha não é possivel! Tente novamente.");
            }

        }
    }

    static char obterCaractereComputador(Scanner teclado, char caractereUsuario) {
        final char[] CARACTERES_IDENTIFICADORES_ACEITOS = { 'X', 'O', '0', 'U', 'C' };
        char caractereComputador;

        while (true) {
            System.out.print("Escolha o caractere para o computador (XO0UC): ");
            caractereComputador = teclado.nextLine().toUpperCase().charAt(0);

            boolean valido = false;

            // percorre o array para validar
            for (char permitido : CARACTERES_IDENTIFICADORES_ACEITOS) {
                if (caractereComputador == permitido) {
                    valido = true;
                    break;
                }
            }

            // validacao
            if (!valido) {
                System.out.println("Caractere inválido. Por favor, escolha .");
            } else if (caractereComputador == caractereUsuario) {
                System.out.println("Caractere já escolhido pelo usuário. Escolha outro.");
            } else {
                break;
            }
        }

        return caractereComputador;
    }

    static boolean jogadaValida(String posicoesLivres, int linha, int coluna) {
        String posicao = linha + "" + coluna;

        if (posicoesLivres.contains(posicao)) {

            return true;

        } else {
            System.out.println("A jogada nesta posição não é permitida!");
            System.out.println("As possiveis posições são: " + posicoesLivres + "\n");

            return false;
        }
    }

    static int[] obterJogadaUsuario(String posicoesLivres, Scanner teclado) {
        boolean jogadaUsuarioValida = false;
        int[] posicaoJogador = new int[2];

        do {
            System.out.print("Digite dois valores separados por espaço LINHA COLUNA: ");
            String jogadaUsuario = teclado.nextLine();

            int indexEspaco = jogadaUsuario.indexOf(' ');
            jogadaUsuarioValida = (indexEspaco != -1); // Verifica se há espaco entre os valores

            if (jogadaUsuarioValida) {
                try {
                    posicaoJogador[0] = Integer.valueOf(jogadaUsuario.substring(0, indexEspaco)) - 1;
                    posicaoJogador[1] = Integer
                            .valueOf(jogadaUsuario.substring(indexEspaco + 1, jogadaUsuario.length())) - 1;
                } catch (NumberFormatException e) {
                    System.out.println("O valor inserido deve ser numérico. \n");
                    jogadaUsuarioValida = false;
                }

                if (jogadaUsuarioValida)
                    jogadaUsuarioValida = jogadaValida(posicoesLivres, posicaoJogador[0], posicaoJogador[1]);
            } else
                System.out.println("A jogada nesta posição não é permitida. \n");

        } while (!jogadaUsuarioValida);

        return posicaoJogador;
    }

    static int[] obterJogadaComputador(String posicoesLivres) {
        // alocando as posicoes nos vetor
        String[] vetorPosicoes = posicoesLivres.split(";");

        // numero aleatorio para a jogada
        Random random = new Random();
        int indiceSorteado = random.nextInt(vetorPosicoes.length);

        // posicao sorteada e no formato certo
        String posicaoSorteada = vetorPosicoes[indiceSorteado];

        return converterJogadaStringParaVetorInt(posicaoSorteada);
    }

    static int[] converterJogadaStringParaVetorInt(String jogada) {
        if (jogada == null || jogada.length() != 2 || !jogada.matches("[0-9]{2}")) {
            System.out.println("A jogada deve ser uma string com dois números.");
        }

        int[] posicao = new int[2];
        posicao[0] = Character.getNumericValue(jogada.charAt(0)); // Linha
        posicao[1] = Character.getNumericValue(jogada.charAt(1)); // Coluna

        return posicao;
    }

    static char[][] processarVezUsuario(Scanner teclado, char[][] tabuleiro, char caractereUsuario) {
        System.out.printf("Usuário %c faça sua jogada\n\n", caractereUsuario);
        int[] jogadaUsuario = obterJogadaUsuario(retornarPosicoesLivres(tabuleiro), teclado);

        char[][] tabuleiroAtualizado = retornarTabuleiroAtualizado(tabuleiro, jogadaUsuario, caractereUsuario);

        return tabuleiroAtualizado;
    }

    static char[][] processarVezComputador(char[][] tabuleiro, char caractereComputador) {
        int[] jogadaComputador = obterJogadaComputador(retornarPosicoesLivres(tabuleiro));

        char[][] tabuleiroAtualizado = retornarTabuleiroAtualizado(tabuleiro, jogadaComputador, caractereComputador);

        return tabuleiroAtualizado;
    }

    static String retornarPosicoesLivres(char[][] tabuleiro) {
        String posicoesLivres = "";

        for (int i = 0; i < tabuleiro.length; i++) {
            for (int j = 0; j < tabuleiro.length; j++) {
                if (tabuleiro[i][j] == ' ')
                    posicoesLivres += ("" + i + j + ";");
            }
        }

        return posicoesLivres;
    }

    static boolean teveGanhador(char[][] tabuleiro, char caractereJogador) {
        if (teveGanhadorLinha(tabuleiro, caractereJogador) ||
                teveGanhadorColuna(tabuleiro, caractereJogador) ||
                teveGanhadorDiagonalPrincipal(tabuleiro, caractereJogador) ||
                teveGanhadorDiagonalSecundaria(tabuleiro, caractereJogador)) {
            return true;
        } else {
            return false;
        }
    }

    static boolean teveGanhadorLinha(char[][] tabuleiro, char caractereJogador) {
        int TAMANHO_LINHA = tabuleiro.length;
        int TAMANHO_COLUNA = tabuleiro.length;

        for (int i = 0; i < TAMANHO_LINHA; i++) { // Verifica todas as linhas
            boolean ganhou = true; // Assume que a linha tem um vencedor
            for (int j = 0; j < TAMANHO_COLUNA; j++) {
                if (tabuleiro[i][j] != caractereJogador) {
                    ganhou = false; // Se um caractere não bate, descarta a linha
                    break; // Sai do loop da linha
                }
            }
            if (ganhou)
                return true; // Retorna verdadeiro se todos os caracteres baterem
        }
        return false; // Nenhuma linha tem todos os caracteres iguais
    }

    static boolean teveGanhadorColuna(char[][] tabuleiro, char caractereJogador) {
        int contadorCaractereJogador = 0;
        boolean ganhadorColuna = false;

        for (int coluna = 0; coluna < tabuleiro.length; coluna++) {
            // esse for serve para passar a coluna
            for (int linha = 0; linha < tabuleiro.length; linha++) {
                // esse for pra passar a linha
                if (tabuleiro[linha][coluna] == caractereJogador) {
                    contadorCaractereJogador++;
                }
            }
            if (contadorCaractereJogador == tabuleiro.length) {
                ganhadorColuna = true;
                break;
            } else {
                ganhadorColuna = false;
                contadorCaractereJogador = 0;
            }
        }

        if (ganhadorColuna)
            System.out.println("Teve ganhador pela coluna");

        return ganhadorColuna;
    }

    static boolean teveGanhadorDiagonalPrincipal(char[][] tabuleiro, char caractereJogador) {
        for (int i = 0; i < tabuleiro.length; i++) {
            if (tabuleiro[i][i] != caractereJogador) {
                return false;
            }
        }
        System.out.println("Vitória pela diagonal principal");
        return true;
    }

    static boolean teveGanhadorDiagonalSecundaria(char[][] tabuleiro, char caractereJogador) {
        int n = tabuleiro.length;
        for (int i = 0; i < n; i++) {
            if (tabuleiro[i][n - 1 - i] != caractereJogador) {
                return false;
            }
        }
        System.out.println("Vitória pela diagonal secundária");
        return true;
    }

    static void limparTela() {
        try {
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Limpar tela para Linux
    // static void limparTela(){
    // try {
    // new ProcessBuilder("clear").inheritIO().start().waitFor();
    // } catch(Exception e) {
    // e.printStackTrace();
    // }
    // }

    static void exibirTabuleiro(char[][] tabuleiro) {
        limparTela();
        System.out.print("Coluna:     ");

        for (int j = 0; j < tabuleiro.length; j++) {
            System.out.print(j + 1);
            if (j != tabuleiro.length - 1)
                System.out.print("      ");
        }
        System.out.println();

        System.out.println();
        for (int i = 0; i < tabuleiro.length; i++) {
            System.out.print("Linha " + (i + 1) + "   ");

            for (int j = 0; j < tabuleiro.length; j++) {
                System.out.print("  " + tabuleiro[i][j] + "   ");
                if (j != tabuleiro.length - 1)
                    System.out.print("|");
            }
            System.out.println();

            if (i != tabuleiro.length - 1) {
                System.out.print("          ");

                for (int j = 0; j < tabuleiro.length; j++) {
                    System.out.print("------");
                    if (j != tabuleiro.length - 1)
                        System.out.print("+");
                }
            }
            System.out.println();
        }
    }

    static char[][] retornarTabuleiroAtualizado(char[][] tabuleiro, int[] jogada, char caractereJogador) {
        tabuleiro[jogada[0]][jogada[1]] = caractereJogador;

        return tabuleiro;
    }

    static void exibirVitoriaComputador() {
        System.out.println("O computador venceu!");
        System.out.println("""
                ╔══════════════════════════╗
                ║ ┌──────────────────────┐ ║
                ║ │   ┌──┐         ┌──┐  │ ║
                ║ │   │ 0│         │ 0│  │ ║
                ║ │   └──┘    ¤    └──┘  │ ║
                ║ │                      │ ║
                ║ │    │             │   │ ║
                ║ │    └─────────────┘   │ ║
                ║ └──────────────────────┘ ║
                ╚══════════════════════════╝
                          |/     \\|
                     ────────────────────
                    / ****************** \\
                   / ******************** \\
                  / ********──────******** \\
                  ───────────────────────────
                """);
    }

    static void exibirVitoriaUsuario() {
        System.out.println("Parabéns, você venceu!");
        System.out.println(
                "        \\|///////\n" +
                        "          \\|//////\n" +
                        "         |       |\n" +
                        "         |--- ---|\n" +
                        "        (| O   O |)\n" +
                        "         |   +   |\n" +
                        "         \\ \\___/ /\n" +
                        "          \\______/\n" +
                        "            \\|//\n"

        );
    }

    static boolean teveEmpate(char[][] tabuleiro) {
        for (int linha = 0; linha < tabuleiro.length; linha++) {
            for (int coluna = 0; coluna < tabuleiro[linha].length; coluna++) {
                if (tabuleiro[linha][coluna] == ' ') {
                    return false;
                }
            }
        }
        return true;
    }

    static void exibirEmpate() {
        System.out.println("   Ocorreu um empate!");
        System.out.println();
        System.out.println("  +---------+                          +---------+");
        System.out.println("  | +-----+ |     **            **     | +-----+ |");
        System.out.println("  ||       ||       **        **       ||       ||");
        System.out.println("  ||       ||          **   **         ||       ||");
        System.out.println("  ||       ||             **           ||       ||");
        System.out.println("  ||       ||          **   **         ||       ||");
        System.out.println("  ||       ||        **       **       ||       ||");
        System.out.println("  | +-----+ |      **           **     | +-----+ |");
        System.out.println("  +---------+    **               **   +---------+");
    }

    static boolean sortearValorBooleano() {
        Random random = new Random();
        return random.nextBoolean();
    }

}