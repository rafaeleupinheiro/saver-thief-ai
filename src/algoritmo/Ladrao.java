package algoritmo;

import java.util.ArrayList;
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

  private int[] listaPosicoesCimaEsquerda = {0, 1, 5, 6};
  private int[] listaPosicoesCimaDireita = {3, 4, 8, 9};
  private int[] listaPosicoesBaixoEsquerda = {14, 15, 19, 20};
  private int[] listaPosicoesBaixoDireita = {17, 18, 22, 23};

  private int numerodeMoedas = 0;
  private boolean roubou = false;

  private int x = 0;
  private int y = 0;
  private boolean mesmaPosicao = false;


  public int acao() {
    visitarCelula();

    this.roubou = numerodeMoedas < sensor.getNumeroDeMoedas();
    numerodeMoedas = sensor.getNumeroDeMoedas();

    if (existe()) {
      System.out.println();
    }

    Integer posicaoPoupador = existePoupadorVisao();
    Integer posicaoOlfato = existePoupadorOlfato();
    if ((this.roubou && posicaoPoupador != null) || this.mesmaPosicao || posicaoPoupador == null || posicaoOlfato == null) {
      return explorar();
    } else if (posicaoPoupador != null) {
      return perseguirPoupador(posicaoPoupador);
    } else {
      return perseguirOlfato(posicaoPoupador);
    }

//    return random();
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

  public Integer existePoupadorOlfato() {
    Integer posicao = null;
    Integer valor = 0;
    int[] visao = sensor.getAmbienteOlfatoPoupador();
    for (int i = 0; i < visao.length; i++) {
      if (visao[i] > 0 && visao[i] > valor && ehVizinhoOlfato(i) != null) {
        posicao = i;
        valor = visao[i];
      }
    }

    if (posicao == null) {
      valor = 0;
      for (int i = 0; i < visao.length; i++) {
        if (visao[i] > 0 && visao[i] > valor) {
          valor = visao[i];
          posicao = i;
        }
      }
    }

    return posicao;
  }

  private int perseguirOlfato(int posicaoPoupador) {
    Integer ehVizinhoOlfato = ehVizinhoOlfato(posicaoPoupador);
    if (ehVizinhoOlfato != null) {
      return ehVizinhoOlfato;
    }

    Integer posicaoCimaEsquerdaOlfato = posicaoCimaEsquerdaOlfato(posicaoPoupador);
    if (posicaoCimaEsquerdaOlfato != null) {
      return ehVizinhoOlfato(posicaoCimaEsquerdaOlfato);
    }

    Integer posicaoCimaDireitaOlfato = posicaoCimaDireitaOlfato(posicaoPoupador);
    if (posicaoCimaDireitaOlfato != null) {
      return ehVizinhoOlfato(posicaoCimaDireitaOlfato);
    }

    Integer posicaoBaixoEsquerdaOlfato = posicaoBaixoEsquerdaOlfato(posicaoPoupador);
    if (posicaoBaixoEsquerdaOlfato != null) {
      return ehVizinhoOlfato(posicaoBaixoEsquerdaOlfato);
    }

    Integer posicaoBaixoDireitaOlfato = posicaoBaixoDireitaOlfato(posicaoPoupador);
    if (posicaoBaixoDireitaOlfato != null) {
      return ehVizinhoOlfato(posicaoBaixoDireitaOlfato);
    }

    return 0;
  }

  /*private boolean ehVizinhoOlfato(int posicao) {
    return posicao == posicaoCimaOlfato || posicao == posicaoBaixoOlfato || posicao == posicaoEsquerdaOlfato || posicao == posicaoDireitaOlfato;
  }*/

  private Integer ehVizinhoOlfato(int posicao) {
    switch (posicao) {
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

  private Integer posicaoCimaEsquerdaOlfato(int posicaoOlfato) {
    List<Integer> aux = new ArrayList<>();
    if (posicaoOlfato == posicaoCimaEsquerdaOlfato) {
      aux.add(posicaoCimaOlfato);
      aux.add(posicaoEsquerdaOlfato);
    }

    Collections.shuffle(aux); // Embaralhar
    return !aux.isEmpty() ? aux.get(0) : null;
  }

  private Integer posicaoCimaDireitaOlfato(int posicaoOlfato) {
    List<Integer> aux = new ArrayList<>();
    if (posicaoOlfato == posicaoCimaDireitaOlfato) {
      aux.add(posicaoCimaOlfato);
      aux.add(posicaoDireitaOlfato);
    }

    Collections.shuffle(aux); // Embaralhar
    return !aux.isEmpty() ? aux.get(0) : null;
  }

  private Integer posicaoBaixoEsquerdaOlfato(int posicaoOlfato) {
    List<Integer> aux = new ArrayList<>();
    if (posicaoOlfato == posicaoBaixoEsquerdaOlfato) {
      aux.add(posicaoBaixoOlfato);
      aux.add(posicaoEsquerdaOlfato);
    }

    Collections.shuffle(aux); // Embaralhar
    return !aux.isEmpty() ? aux.get(0) : null;
  }

  private Integer posicaoBaixoDireitaOlfato(int posicaoOlfato) {
    List<Integer> aux = new ArrayList<>();
    if (posicaoOlfato == posicaoBaixoDireitaOlfato) {
      aux.add(posicaoBaixoOlfato);
      aux.add(posicaoDireitaOlfato);
    }

    Collections.shuffle(aux); // Embaralhar
    return !aux.isEmpty() ? aux.get(0) : null;
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

  private int random() {
    return (int) (Math.random() * 5);
  }

  private boolean existe() {
    int[] visao = sensor.getAmbienteOlfatoPoupador();
    for (int i = 0; i < visao.length; i++) {
      if (visao[i] > 0) {
        return true;
      }
    }
    return false;
  }
}