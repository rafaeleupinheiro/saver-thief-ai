package algoritmo;

import controle.Constantes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Ladrao extends ProgramaLadrao {
  private int[][] exploracao = new int[30][30];

  private int posicaoCimaVisao = 7;
  private int posicaoLogoAcimaVisao = 2;
  private int posicaoBaixoVisao = 16;
  private int posicaoLogoAbaixoVisao = 21;
  private int posicaoEsquerdaVisao = 11;
  private int posicaoLogoAEsquerdaVisao = 10;
  private int posicaoDireitaVisao = 12;
  private int posicaoLogoADireitaVisao = 13;

  private int parado = 0;
  private int cima = 1;
  private int baixo = 2;
  private int direita = 3;
  private int esquerda = 4;

  private static int[] experiencia = new int[]{0, 0, 0, 0};

  private int[] listaPosicoesCimaEsquerda = {0, 1, 5, 6};
  private int[] listaPosicoesCimaDireita = {3, 4, 8, 9};
  private int[] listaPosicoesBaixoEsquerda = {14, 15, 19, 20};
  private int[] listaPosicoesBaixoDireita = {17, 18, 22, 23};


  public int acao() {
    visitarCelula();

    Integer posicaoPoupador = existePoupadorVisao();
    if (posicaoPoupador == null) {
      return explorar();
    } else {
      return perseguirPoupador(posicaoPoupador);
    }

//    return random();
  }

  public void visitarCelula() {
    int x = (int) sensor.getPosicao().getX();
    int y = (int) sensor.getPosicao().getY();

    exploracao[x][y] += 1;
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
    int celulaVazia = 0;

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
    }

    Integer posicaoPoupadorCimaEsquerdaVisao = posicaoPoupadorCimaEsquerdaVisao(posicaoPoupador);
    if (posicaoPoupadorCimaEsquerdaVisao != null) {
      return ehVizinhoPoupador(posicaoPoupadorCimaEsquerdaVisao);
    }

    Integer posicaoPoupadorCimaDireitaVisao = posicaoPoupadorCimaDireitaVisao(posicaoPoupador);
    if (posicaoPoupadorCimaDireitaVisao != null) {
      return ehVizinhoPoupador(posicaoPoupadorCimaDireitaVisao);
    }

    Integer posicaoPoupadorBaixoEsquerdaVisao = posicaoPoupadorBaixoEsquerdaVisao(posicaoPoupador);
    if (posicaoPoupadorBaixoEsquerdaVisao != null) {
      return ehVizinhoPoupador(posicaoPoupadorBaixoEsquerdaVisao);
    }

    Integer posicaoPoupadorBaixoDireitaVisao = posicaoPoupadorBaixoDireitaVisao(posicaoPoupador);
    if (posicaoPoupadorBaixoDireitaVisao != null) {
      return ehVizinhoPoupador(posicaoPoupadorBaixoDireitaVisao);
    }

    Integer posicaoPoupadorLogoAcimaVisao = posicaoPoupadorLogoAcimaVisao(posicaoPoupador);
    if (posicaoPoupadorLogoAcimaVisao != null) {
      return ehVizinhoPoupador(posicaoPoupadorLogoAcimaVisao);
    }

    Integer posicaoPoupadorLogoAbaixoVisao = posicaoPoupadorLogoAbaixoVisao(posicaoPoupador);
    if (posicaoPoupadorLogoAbaixoVisao != null) {
      return ehVizinhoPoupador(posicaoPoupadorLogoAbaixoVisao);
    }

    Integer posicaoPoupadorLogoAEsquerdaVisao = posicaoPoupadorLogoAEsquerdaVisao(posicaoPoupador);
    if (posicaoPoupadorLogoAEsquerdaVisao != null) {
      return ehVizinhoPoupador(posicaoPoupadorLogoAEsquerdaVisao);
    }

    Integer posicaoPoupadorLogoADireitaVisao = posicaoPoupadorLogoADireitaVisao(posicaoPoupador);
    if (posicaoPoupadorLogoADireitaVisao != null) {
      return ehVizinhoPoupador(posicaoPoupadorLogoADireitaVisao);
    }

    return 0;
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

  private Integer posicaoPoupadorCimaEsquerdaVisao(int posicaoPoupador) {
    List<Integer> aux = new ArrayList<>();
    for (int i = 0; i < listaPosicoesCimaEsquerda.length; i++) {
      if (posicaoPoupador == listaPosicoesCimaEsquerda[i]) {
        aux.add(posicaoCimaVisao);
        aux.add(posicaoEsquerdaVisao);
        break;
      }
    }

    Collections.shuffle(aux); // Embaralhar
    return !aux.isEmpty() ? aux.get(0) : null;
  }

  private Integer posicaoPoupadorCimaDireitaVisao(int posicaoPoupador) {
    List<Integer> aux = new ArrayList<>();
    for (int i = 0; i < listaPosicoesCimaDireita.length; i++) {
      if (posicaoPoupador == listaPosicoesCimaDireita[i]) {
        aux.add(posicaoCimaVisao);
        aux.add(posicaoDireitaVisao);
        break;
      }
    }

    Collections.shuffle(aux); // Embaralhar
    return !aux.isEmpty() ? aux.get(0) : null;
  }

  private Integer posicaoPoupadorBaixoEsquerdaVisao(int posicaoPoupador) {
    List<Integer> aux = new ArrayList<>();
    for (int i = 0; i < listaPosicoesBaixoEsquerda.length; i++) {
      if (posicaoPoupador == listaPosicoesBaixoEsquerda[i]) {
        aux.add(posicaoBaixoVisao);
        aux.add(posicaoEsquerdaVisao);
        break;
      }
    }

    Collections.shuffle(aux); // Embaralhar
    return !aux.isEmpty() ? aux.get(0) : null;
  }

  private Integer posicaoPoupadorBaixoDireitaVisao(int posicaoPoupador) {
    List<Integer> aux = new ArrayList<>();
    for (int i = 0; i < listaPosicoesBaixoDireita.length; i++) {
      if (posicaoPoupador == listaPosicoesBaixoDireita[i]) {
        aux.add(posicaoBaixoVisao);
        aux.add(posicaoDireitaVisao);
        break;
      }
    }

    Collections.shuffle(aux); // Embaralhar
    return !aux.isEmpty() ? aux.get(0) : null;
  }

  private Integer posicaoPoupadorLogoAcimaVisao(int posicaoPoupador) {
    if (posicaoPoupador == posicaoLogoAcimaVisao) {
      return posicaoCimaVisao;
    }
    return null;
  }

  private Integer posicaoPoupadorLogoAbaixoVisao(int posicaoPoupador) {
    if (posicaoPoupador == posicaoLogoAbaixoVisao) {
      return posicaoBaixoVisao;
    }
    return null;
  }

  private Integer posicaoPoupadorLogoAEsquerdaVisao(int posicaoPoupador) {
    if (posicaoPoupador == posicaoLogoAEsquerdaVisao) {
      return posicaoEsquerdaVisao;
    }
    return null;
  }

  private Integer posicaoPoupadorLogoADireitaVisao(int posicaoPoupador) {
    if (posicaoPoupador == posicaoLogoADireitaVisao) {
      return posicaoDireitaVisao;
    }
    return null;
  }

  private boolean ehCelularVazia(int valor) {
    if (valor == 0) {
      return true;
    }
    return false;
  }

  private int roubar(int valor) {
    switch (valor) {
      case 7:
        return cima;
      case 11:
        return esquerda;
      case 12:
        return direita;
      case 16:
        return baixo;
    }
    return random();
  }

  private Integer ehVizinhoVisao() {
    if (ehPoupador(sensor.getVisaoIdentificacao()[7])) {
      return cima;
    } else if (ehPoupador(sensor.getVisaoIdentificacao()[12])) {
      return direita;
    } else if (ehPoupador(sensor.getVisaoIdentificacao()[16])) {
      return baixo;
    } else if (ehPoupador(sensor.getVisaoIdentificacao()[11])) {
      return esquerda;
    }
    return null;
  }

  private Integer ehVizinhoOlfato(int posicaoFaroPoupador) {
    switch (posicaoFaroPoupador) {
      case 1:
        return cima;
      case 3:
        return esquerda;
      case 4:
        return direita;
      case 6:
        return baixo;
    }
    return null;
  }

  private int random() {
    return (int) (Math.random() * 5);
  }


  private int faro() {
    int valorFaroPoupador = 0;
    Integer index = null;
    for (int i = 0; i < sensor.getAmbienteOlfatoPoupador().length; i++) {
      int valor = sensor.getAmbienteOlfatoPoupador()[i];
      if (valorFaroPoupador == 0 && valor > 0) {
        valorFaroPoupador = valor;
        index = i;
      } else if (valor > 0 && valor < valorFaroPoupador) {
        valorFaroPoupador = valor;
        index = i;
      }
    }
    return index;
  }

  /*
   * private int segueFaro(int posicaoFaroPoupador) {
   *
   *
   * }
   */
}