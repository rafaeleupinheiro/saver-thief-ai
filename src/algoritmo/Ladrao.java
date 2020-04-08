package algoritmo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Ladrao extends ProgramaLadrao {
  private int[][] exploracao = new int[30][30];

  private int posicaoCimaVisao = 7;
  private int posicaoCimaEsquerdaVisao = 6;
  private int posicaoCimaDireitaVisao = 8;
  private int posicaoLogoAcimaVisao = 2;
  private int posicaoBaixoVisao = 16;
  private int posicaoBaixoEsquerdaVisao = 15;
  private int posicaoBaixoDireitaVisao = 17;
  private int posicaoLogoAbaixoVisao = 21;
  private int posicaoEsquerdaVisao = 11;
  private int posicaoLogoAEsquerdaVisao = 10;
  private int posicaoDireitaVisao = 12;
  private int posicaoLogoADireitaVisao = 13;

  private int posicaoCimaOlfato = 1;
  private int posicaoBaixoOlfato = 6;
  private int posicaoEsquerdaOlfato = 3;
  private int posicaoDireitaOlfato = 4;
  private int posicaoCimaEsquerdaOlfato = 0;
  private int posicaoBaixoEsquerdaOlfato = 5;
  private int posicaoCimaDireitaOlfato = 2;
  private int posicaoBaixoDireitaOlfato = 7;

  private int[] listaPosicoesVisinhosOlfato = {1, 3, 4, 6};
  private int[] listaPosicoesVisinhosOlfato2 = {0, 2, 5, 7};

  private int parado = 0;
  private int cima = 1;
  private int baixo = 2;
  private int direita = 3;
  private int esquerda = 4;

  private static int[] experiencia = new int[]{0, 0, 0, 0};

  private int[] listaPosicoesCimaEsquerda = {0, 1, 5};
  private int[] listaPosicoesCimaDireita = {3, 4, 9};
  private int[] listaPosicoesBaixoEsquerda = {14, 19, 20};
  private int[] listaPosicoesBaixoDireita = {18, 22, 23};

  private int numerodeMoedas = 0;
  private boolean roubou = false;

  private int x = 0;
  private int y = 0;
  private boolean mesmaPosicao = false;

  int tempoRoubo = 0;

  public int acao() {
    visitarCelula();

    this.roubou = numerodeMoedas < sensor.getNumeroDeMoedas();
    numerodeMoedas = sensor.getNumeroDeMoedas();
    if (this.roubou) {
      tempoRoubo = 3;
    }

    Integer posicaoPoupador = existePoupadorVisao();
    if (tempoRoubo > 0 || this.mesmaPosicao || posicaoPoupador == null) {
      if (tempoRoubo > 0) {
        tempoRoubo--;
      }
      return explorar();
    } else {
      return perseguirPoupador(posicaoPoupador);
    }
  }

  public void visitarCelula() {
    this.mesmaPosicao = this.x == (int) sensor.getPosicao().getX() && this.y == (int) sensor.getPosicao().getY();

    this.x = (int) sensor.getPosicao().getX();
    this.y = (int) sensor.getPosicao().getY();

    exploracao[this.x][this.y] += 1;
  }

  public int explorar() {
    int[] posicaoVisaoExploracao = preencheVisaoExploracao();

    List<Integer> listIndices = new ArrayList<>();
    int menorValor = 999;

    for (int i = 0; i < posicaoVisaoExploracao.length; i++) {
      if (posicaoVisaoExploracao[i] < menorValor) {
        listIndices.clear();
        menorValor = posicaoVisaoExploracao[i];
        listIndices.add(i);
      } else if (posicaoVisaoExploracao[i] == menorValor) {
        listIndices.add(i);
      }
    }

    Collections.shuffle(listIndices); // Embaralhar
    switch (listIndices.get(0)) {
      case 0:
        return cima;
      case 1:
        return baixo;
      case 2:
        return esquerda;
      case 3:
        return direita;
    }
    return 0;
  }

  public int[] preencheVisaoExploracao() {
    int[] posicaoVisaoExploracao = new int[4]; //Cima, Baixo, Esquerda, Direita.
    int[] visao = sensor.getVisaoIdentificacao();

    if (visao[posicaoCimaVisao] != 0) {
      posicaoVisaoExploracao[0] = 999;
    }
    if (visao[posicaoBaixoVisao] != 0) {
      posicaoVisaoExploracao[1] = 999;
    }
    if (visao[posicaoEsquerdaVisao] != 0) {
      posicaoVisaoExploracao[2] = 999;
    }
    if (visao[posicaoDireitaVisao] != 0) {
      posicaoVisaoExploracao[3] = 999;
    }

    for (int i = 0; i < posicaoVisaoExploracao.length; i++) {
      if (posicaoVisaoExploracao[i] == 0) {
        posicaoVisaoExploracao[i] = getValorExploracao(i);
      }
    }
    return posicaoVisaoExploracao;
  }

  public int getValorExploracao(int posicao) {
    int x = (int) sensor.getPosicao().getX();
    int y = (int) sensor.getPosicao().getY();

    switch (posicao) {
      case 0:
        return exploracao[x][y - 1];
      case 1:
        return exploracao[x][y + 1];
      case 2:
        return exploracao[x - 1][y];
      case 3:
        return exploracao[x + 1][y];
    }
    return 0;
  }

  public Integer existePoupadorVisao() {
    int[] visao = sensor.getVisaoIdentificacao();
    for (int i = 0; i < visao.length; i++) {
      if (ehPoupador(visao[i])) {
        return i;
      }
    }
    return null;
  }

  private boolean ehPoupador(int valor) {
    if (valor == 100 || valor == 110) {
      return true;
    }
    return false;
  }

  private int perseguirPoupador(int posicaoPoupador) {
    Integer ehVizinhoPoupador = ehVizinhoPoupador(posicaoPoupador);
    if (ehVizinhoPoupador != null) {
      return ehVizinhoPoupador;
    } else if (ehPoupador(sensor.getVisaoIdentificacao()[posicaoLogoAcimaVisao])) {
      return posicaoPoupadorLogoAcimaVisao();
    } else if (ehPoupador(sensor.getVisaoIdentificacao()[posicaoLogoAbaixoVisao])) {
      return posicaoPoupadorLogoAbaixoVisao();
    } else if (ehPoupador(sensor.getVisaoIdentificacao()[posicaoLogoADireitaVisao])) {
      return posicaoPoupadorLogoADireitaVisao();
    } else if (ehPoupador(sensor.getVisaoIdentificacao()[posicaoLogoAEsquerdaVisao])) {
      return posicaoPoupadorLogoAEsquerdaVisao();
    } else if (ehPoupador(sensor.getVisaoIdentificacao()[0]) || ehPoupador(sensor.getVisaoIdentificacao()[1])
        || ehPoupador(sensor.getVisaoIdentificacao()[5]) || ehPoupador(sensor.getVisaoIdentificacao()[6])) {
      return posicaoPoupadorCimaEsquerdaVisao();
    } else if (ehPoupador(sensor.getVisaoIdentificacao()[14]) || ehPoupador(sensor.getVisaoIdentificacao()[15])
        || ehPoupador(sensor.getVisaoIdentificacao()[19]) || ehPoupador(sensor.getVisaoIdentificacao()[20])) {
      return posicaoPoupadorBaixoEsquerdaVisao();
    } else if (ehPoupador(sensor.getVisaoIdentificacao()[3]) || ehPoupador(sensor.getVisaoIdentificacao()[4])
        || ehPoupador(sensor.getVisaoIdentificacao()[8]) || ehPoupador(sensor.getVisaoIdentificacao()[9])) {
      return posicaoPoupadorCimaDireitaVisao();
    } else if (ehPoupador(sensor.getVisaoIdentificacao()[17]) || ehPoupador(sensor.getVisaoIdentificacao()[18])
        || ehPoupador(sensor.getVisaoIdentificacao()[22]) || ehPoupador(sensor.getVisaoIdentificacao()[23])) {
      return posicaoPoupadorBaixoDireitaVisao();
    }
    return parado;
  }

  private int posicaoPoupadorLogoAcimaVisao() {
    if (ehCelularVazia(sensor.getVisaoIdentificacao()[posicaoCimaVisao])) {
      return cima;
    } else {
      int valorOlfatoEsquerda = 0;
      int valorOlfatoDireita = 0;
      if (ehCelularVazia(sensor.getVisaoIdentificacao()[posicaoEsquerdaVisao])) {
        valorOlfatoEsquerda = sensor.getAmbienteOlfatoPoupador()[posicaoEsquerdaOlfato];
      } else if (ehCelularVazia(sensor.getVisaoIdentificacao()[posicaoDireitaVisao])) {
        valorOlfatoDireita = sensor.getAmbienteOlfatoPoupador()[posicaoDireitaOlfato];
      }

      if (valorOlfatoEsquerda != 0 && valorOlfatoEsquerda <= valorOlfatoDireita) {
        return esquerda;
      } else if (valorOlfatoDireita != 0 && valorOlfatoDireita <= valorOlfatoEsquerda) {
        return direita;
      } else if (ehCelularVazia(sensor.getVisaoIdentificacao()[posicaoEsquerdaVisao])) {
        return esquerda;
      } else if (ehCelularVazia(sensor.getVisaoIdentificacao()[posicaoDireitaVisao])) {
        return direita;
      } else {
        return parado;
      }
    }
  }

  private int posicaoPoupadorLogoAbaixoVisao() {
    if (ehCelularVazia(sensor.getVisaoIdentificacao()[posicaoBaixoVisao])) {
      return baixo;
    } else {
      int valorOlfatoEsquerda = 0;
      int valorOlfatoDireita = 0;
      if (ehCelularVazia(sensor.getVisaoIdentificacao()[posicaoEsquerdaVisao])) {
        valorOlfatoEsquerda = sensor.getAmbienteOlfatoPoupador()[posicaoEsquerdaOlfato];
      } else if (ehCelularVazia(sensor.getVisaoIdentificacao()[posicaoDireitaVisao])) {
        valorOlfatoDireita = sensor.getAmbienteOlfatoPoupador()[posicaoDireitaOlfato];
      }

      if (valorOlfatoEsquerda != 0 && valorOlfatoEsquerda <= valorOlfatoDireita) {
        return esquerda;
      } else if (valorOlfatoDireita != 0 && valorOlfatoDireita <= valorOlfatoEsquerda) {
        return direita;
      } else if (ehCelularVazia(sensor.getVisaoIdentificacao()[posicaoEsquerdaVisao])) {
        return esquerda;
      } else if (ehCelularVazia(sensor.getVisaoIdentificacao()[posicaoDireitaVisao])) {
        return direita;
      } else {
        return parado;
      }
    }
  }

  private int posicaoPoupadorLogoAEsquerdaVisao() {
    if (ehCelularVazia(sensor.getVisaoIdentificacao()[posicaoEsquerdaVisao])) {
      return esquerda;
    } else {
      int valorOlfatoCima = 0;
      int valorOlfatoBaixo = 0;
      if (ehCelularVazia(sensor.getVisaoIdentificacao()[posicaoCimaVisao])) {
        valorOlfatoCima = sensor.getAmbienteOlfatoPoupador()[posicaoCimaOlfato];
      } else if (ehCelularVazia(sensor.getVisaoIdentificacao()[posicaoBaixoVisao])) {
        valorOlfatoBaixo = sensor.getAmbienteOlfatoPoupador()[posicaoBaixoOlfato];
      }

      if (valorOlfatoCima != 0 && valorOlfatoCima <= valorOlfatoBaixo) {
        return cima;
      } else if (valorOlfatoBaixo != 0 && valorOlfatoBaixo <= valorOlfatoCima) {
        return baixo;
      } else if (ehCelularVazia(sensor.getVisaoIdentificacao()[posicaoCimaVisao])) {
        return cima;
      } else if (ehCelularVazia(sensor.getVisaoIdentificacao()[posicaoBaixoVisao])) {
        return baixo;
      } else {
        return parado;
      }
    }
  }

  private int posicaoPoupadorLogoADireitaVisao() {
    if (ehCelularVazia(sensor.getVisaoIdentificacao()[posicaoDireitaVisao])) {
      return direita;
    } else {
      int valorOlfatoCima = 0;
      int valorOlfatoBaixo = 0;
      if (ehCelularVazia(sensor.getVisaoIdentificacao()[posicaoCimaVisao])) {
        valorOlfatoCima = sensor.getAmbienteOlfatoPoupador()[posicaoCimaOlfato];
      } else if (ehCelularVazia(sensor.getVisaoIdentificacao()[posicaoBaixoVisao])) {
        valorOlfatoBaixo = sensor.getAmbienteOlfatoPoupador()[posicaoBaixoOlfato];
      }
      if (valorOlfatoCima != 0 && valorOlfatoCima <= valorOlfatoBaixo) {
        return cima;
      } else if (valorOlfatoBaixo != 0 && valorOlfatoBaixo <= valorOlfatoCima) {
        return baixo;
      } else if (ehCelularVazia(sensor.getVisaoIdentificacao()[posicaoCimaVisao])) {
        return cima;
      } else if (ehCelularVazia(sensor.getVisaoIdentificacao()[posicaoBaixoVisao])) {
        return baixo;
      } else {
        return parado;
      }
    }
  }

  private int posicaoPoupadorCimaEsquerdaVisao() {
    int valorOlfatoCima = 0;
    int valorOlfatoEsquerda = 0;
    if (ehCelularVazia(sensor.getVisaoIdentificacao()[posicaoCimaVisao])) {
      valorOlfatoCima = sensor.getAmbienteOlfatoPoupador()[posicaoCimaOlfato];
    } else if (ehCelularVazia(sensor.getVisaoIdentificacao()[posicaoEsquerdaVisao])) {
      valorOlfatoEsquerda = sensor.getAmbienteOlfatoPoupador()[posicaoEsquerdaOlfato];
    }

    if (valorOlfatoCima != 0 && valorOlfatoCima <= valorOlfatoEsquerda) {
      return cima;
    } else if (valorOlfatoEsquerda != 0 && valorOlfatoEsquerda <= valorOlfatoCima) {
      return esquerda;
    } else if (ehCelularVazia(sensor.getVisaoIdentificacao()[posicaoCimaVisao])) {
      return cima;
    } else if (ehCelularVazia(sensor.getVisaoIdentificacao()[posicaoEsquerdaVisao])) {
      return esquerda;
    } else {
      return parado;
    }
  }


  private int posicaoPoupadorCimaDireitaVisao() {
    int valorOlfatoCima = 0;
    int valorOlfatoDireita = 0;
    if (ehCelularVazia(sensor.getVisaoIdentificacao()[posicaoCimaVisao])) {
      valorOlfatoCima = sensor.getAmbienteOlfatoPoupador()[posicaoCimaOlfato];
    } else if (ehCelularVazia(sensor.getVisaoIdentificacao()[posicaoDireitaVisao])) {
      valorOlfatoDireita = sensor.getAmbienteOlfatoPoupador()[posicaoDireitaOlfato];
    }

    if (valorOlfatoCima != 0 && valorOlfatoCima <= valorOlfatoDireita) {
      return cima;
    } else if (valorOlfatoDireita != 0 && valorOlfatoDireita <= valorOlfatoCima) {
      return direita;
    } else if (ehCelularVazia(sensor.getVisaoIdentificacao()[posicaoCimaVisao])) {
      return cima;
    } else if (ehCelularVazia(sensor.getVisaoIdentificacao()[posicaoDireitaVisao])) {
      return direita;
    } else {
      return parado;
    }
  }

  private int posicaoPoupadorBaixoEsquerdaVisao() {
    int valorOlfatoBaixo = 0;
    int valorOlfatoEsquerda = 0;
    if (ehCelularVazia(sensor.getVisaoIdentificacao()[posicaoBaixoVisao])) {
      valorOlfatoBaixo = sensor.getAmbienteOlfatoPoupador()[posicaoBaixoOlfato];
    } else if (ehCelularVazia(sensor.getVisaoIdentificacao()[posicaoEsquerdaVisao])) {
      valorOlfatoEsquerda = sensor.getAmbienteOlfatoPoupador()[posicaoEsquerdaOlfato];
    }

    if (valorOlfatoBaixo != 0 && valorOlfatoBaixo <= valorOlfatoEsquerda) {
      return baixo;
    } else if (valorOlfatoEsquerda != 0 && valorOlfatoEsquerda <= valorOlfatoBaixo) {
      return esquerda;
    } else if (ehCelularVazia(sensor.getVisaoIdentificacao()[posicaoBaixoVisao])) {
      return baixo;
    } else if (ehCelularVazia(sensor.getVisaoIdentificacao()[posicaoEsquerdaVisao])) {
      return esquerda;
    } else {
      return parado;
    }
  }

  private int posicaoPoupadorBaixoDireitaVisao() {
    int valorOlfatoBaixo = 0;
    int valorOlfatoDireita = 0;
    if (ehCelularVazia(sensor.getVisaoIdentificacao()[posicaoBaixoVisao])) {
      valorOlfatoBaixo = sensor.getAmbienteOlfatoPoupador()[posicaoBaixoOlfato];
    } else if (ehCelularVazia(sensor.getVisaoIdentificacao()[posicaoDireitaVisao])) {
      valorOlfatoDireita = sensor.getAmbienteOlfatoPoupador()[posicaoDireitaOlfato];
    }

    if (valorOlfatoBaixo != 0 && valorOlfatoBaixo <= valorOlfatoDireita) {
      return baixo;
    } else if (valorOlfatoDireita != 0 && valorOlfatoDireita <= valorOlfatoBaixo) {
      return direita;
    } else if (ehCelularVazia(sensor.getVisaoIdentificacao()[posicaoBaixoVisao])) {
      return baixo;
    } else if (ehCelularVazia(sensor.getVisaoIdentificacao()[posicaoDireitaVisao])) {
      return direita;
    } else {
      return parado;
    }
  }

  private Integer ehVizinhoPoupador(int posicaoPoupador) {
    if (posicaoPoupador == posicaoCimaVisao) {
      return cima;
    } else if (posicaoPoupador == posicaoBaixoVisao) {
      return baixo;
    } else if (posicaoPoupador == posicaoEsquerdaVisao) {
      return esquerda;
    } else if (posicaoPoupador == posicaoDireitaVisao) {
      return direita;
    }
    return null;
  }

  private boolean ehCelularVazia(int valor) {
    if (valor == 0) {
      return true;
    }
    return false;
  }
}