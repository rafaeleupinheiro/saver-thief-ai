package algoritmo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LadraoTeste extends ProgramaLadrao {
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


  public int acao() {
    visitarCelula();

    this.roubou = numerodeMoedas < sensor.getNumeroDeMoedas();
    numerodeMoedas = sensor.getNumeroDeMoedas();

    if (existe()) {
      System.out.println();
    }

    Integer posicaoPoupador = existePoupadorVisao();
    Integer posicaoOlfato = existePoupadorOlfato();
    if (posicaoPoupador == null) {
      return explorar();
    } else {
      return perseguirPoupador(posicaoPoupador);
    }/* else {
      return perseguirOlfato(posicaoPoupador);
    }*/

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
    } else if (ehPoupador(sensor.getVisaoIdentificacao()[2])) {
      if (ehCelularVazia(sensor.getVisaoIdentificacao()[7])) {
        return cima;
      } else if (ehCelularVazia(sensor.getVisaoIdentificacao()[11])) {
        return esquerda;
      } else if (ehCelularVazia(sensor.getVisaoIdentificacao()[12])) {
        return direita;
      } else {
        return parado;
      }
    } else if (ehPoupador(sensor.getVisaoIdentificacao()[21])) {
      if (ehCelularVazia(sensor.getVisaoIdentificacao()[16])) {
        return baixo;
      } else if (ehCelularVazia(sensor.getVisaoIdentificacao()[11])) {
        return esquerda;
      } else if (ehCelularVazia(sensor.getVisaoIdentificacao()[12])) {
        return direita;
      } else {
        return parado;
      }
    } else if (ehPoupador(sensor.getVisaoIdentificacao()[13])) {
      if (ehCelularVazia(sensor.getVisaoIdentificacao()[12])) {
        return direita;
      } else if (ehCelularVazia(sensor.getVisaoIdentificacao()[7])) {
        return cima;
      } else if (ehCelularVazia(sensor.getVisaoIdentificacao()[16])) {
        return baixo;
      } else {
        return parado;
      }
    } else if (ehPoupador(sensor.getVisaoIdentificacao()[10])) {
      if (ehCelularVazia(sensor.getVisaoIdentificacao()[11])) {
        return esquerda;
      } else if (ehCelularVazia(sensor.getVisaoIdentificacao()[7])) {
        return cima;
      } else if (ehCelularVazia(sensor.getVisaoIdentificacao()[16])) {
        return baixo;
      } else {
        return parado;
      }
    } else if (ehPoupador(sensor.getVisaoIdentificacao()[0]) || ehPoupador(sensor.getVisaoIdentificacao()[1])
        || ehPoupador(sensor.getVisaoIdentificacao()[5]) || ehPoupador(sensor.getVisaoIdentificacao()[6])) {
      if (ehCelularVazia(sensor.getVisaoIdentificacao()[11])) {
        return esquerda;
      } else if (ehCelularVazia(sensor.getVisaoIdentificacao()[7])) {
        return cima;
      } else {
        return parado;
      }
    } else if (ehPoupador(sensor.getVisaoIdentificacao()[14]) || ehPoupador(sensor.getVisaoIdentificacao()[15])
        || ehPoupador(sensor.getVisaoIdentificacao()[19]) || ehPoupador(sensor.getVisaoIdentificacao()[20])) {
      if (ehCelularVazia(sensor.getVisaoIdentificacao()[11])) {
        return esquerda;
      } else if (ehCelularVazia(sensor.getVisaoIdentificacao()[16])) {
        return baixo;
      } else {
        return parado;
      }
    } else if (ehPoupador(sensor.getVisaoIdentificacao()[3]) || ehPoupador(sensor.getVisaoIdentificacao()[4])
        || ehPoupador(sensor.getVisaoIdentificacao()[8]) || ehPoupador(sensor.getVisaoIdentificacao()[9])) {
      if (ehCelularVazia(sensor.getVisaoIdentificacao()[7])) {
        return cima;
      } else if (ehCelularVazia(sensor.getVisaoIdentificacao()[12])) {
        return direita;
      } else {
        return parado;
      }
    } else if (ehPoupador(sensor.getVisaoIdentificacao()[17]) || ehPoupador(sensor.getVisaoIdentificacao()[18])
        || ehPoupador(sensor.getVisaoIdentificacao()[22]) || ehPoupador(sensor.getVisaoIdentificacao()[23])) {
      if (ehCelularVazia(sensor.getVisaoIdentificacao()[12])) {
        return direita;
      } else if (ehCelularVazia(sensor.getVisaoIdentificacao()[16])) {
        return baixo;
      } else {
        return parado;
      }
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

    Integer posicaoPoupadorCimaEsquerdaVisaoOlfato = posicaoPoupadorCimaEsquerdaVisaoOlfato(posicaoPoupador);
    if (posicaoPoupadorCimaEsquerdaVisaoOlfato != null) {
      return ehVizinhoPoupador(posicaoPoupadorCimaEsquerdaVisaoOlfato);
    }

    Integer posicaoPoupadorCimaDireitaVisaoOlfato = posicaoPoupadorCimaDireitaVisaoOlfato(posicaoPoupador);
    if (posicaoPoupadorCimaDireitaVisaoOlfato != null) {
      return ehVizinhoPoupador(posicaoPoupadorCimaDireitaVisaoOlfato);
    }

    Integer posicaoPoupadorBaixoEsquerdaVisaoOlfato = posicaoPoupadorBaixoEsquerdaVisaoOlfato(posicaoPoupador);
    if (posicaoPoupadorBaixoEsquerdaVisaoOlfato != null) {
      return ehVizinhoPoupador(posicaoPoupadorBaixoEsquerdaVisaoOlfato);
    }

    Integer posicaoPoupadorBaixoDireitaVisaoOlfato = posicaoPoupadorBaixoDireitaVisaoOlfato(posicaoPoupador);
    if (posicaoPoupadorBaixoDireitaVisaoOlfato != null) {
      return ehVizinhoPoupador(posicaoPoupadorBaixoDireitaVisaoOlfato);
    }


    Integer posicaoOlfato = existePoupadorOlfato();
    Integer ehVizinhoOlfato = ehVizinhoOlfato(posicaoOlfato);
    if (ehVizinhoOlfato != null) {
      return ehVizinhoOlfato;
    }


    return 0;
  }

  private Integer posicaoPoupadorLogoAcimaVisao(int posicaoPoupador) {
    if (posicaoPoupador == posicaoLogoAcimaVisao) {
      if (ehCelularVazia(posicaoCimaVisao)) {
        return posicaoCimaVisao;
      } else {
        List<Integer> aux = new ArrayList<>();
        Integer posicaoVisao = maiorPosicaoVisaoBaseadoOlfato(new int[]{3, 4});
        if (posicaoVisao != null) {
          aux.add(posicaoVisao);
        } else {
          if (ehCelularVazia(posicaoEsquerdaVisao)) {
            aux.add(posicaoEsquerdaVisao);
          }
          if (ehCelularVazia(posicaoDireitaVisao)) {
            aux.add(posicaoDireitaVisao);
          }
        }
        Collections.shuffle(aux); // Embaralhar
        return !aux.isEmpty() ? aux.get(0) : null;
      }
    } else {
      return null;
    }
  }

  private Integer posicaoPoupadorLogoAbaixoVisao(int posicaoPoupador) {
    if (posicaoPoupador == posicaoLogoAbaixoVisao) {
      if (ehCelularVazia(posicaoBaixoVisao)) {
        return posicaoBaixoVisao;
      } else {
        List<Integer> aux = new ArrayList<>();
        Integer posicaoVisao = maiorPosicaoVisaoBaseadoOlfato(new int[]{3, 4});
        if (posicaoVisao != null) {
          aux.add(posicaoVisao);
        } else {
          if (ehCelularVazia(posicaoEsquerdaVisao)) {
            aux.add(posicaoEsquerdaVisao);
          }
          if (ehCelularVazia(posicaoDireitaVisao)) {
            aux.add(posicaoDireitaVisao);
          }
        }
        Collections.shuffle(aux); // Embaralhar
        return !aux.isEmpty() ? aux.get(0) : null;
      }
    } else {
      return null;
    }
  }

  private Integer posicaoPoupadorLogoAEsquerdaVisao(int posicaoPoupador) {
    if (posicaoPoupador == posicaoLogoAEsquerdaVisao) {
      if (ehCelularVazia(posicaoEsquerdaVisao)) {
        return posicaoEsquerdaVisao;
      } else {
        List<Integer> aux = new ArrayList<>();
        Integer posicaoVisao = maiorPosicaoVisaoBaseadoOlfato(new int[]{1, 6});
        if (posicaoVisao != null) {
          aux.add(posicaoVisao);
        } else {
          if (ehCelularVazia(posicaoCimaVisao)) {
            aux.add(posicaoCimaVisao);
          }
          if (ehCelularVazia(posicaoBaixoVisao)) {
            aux.add(posicaoBaixoVisao);
          }
        }
        Collections.shuffle(aux); // Embaralhar
        return !aux.isEmpty() ? aux.get(0) : null;
      }
    } else {
      return null;
    }
  }

  private Integer posicaoPoupadorLogoADireitaVisao(int posicaoPoupador) {
    if (posicaoPoupador == posicaoLogoADireitaVisao) {
      if (ehCelularVazia(posicaoDireitaVisao)) {
        return posicaoDireitaVisao;
      } else {
        List<Integer> aux = new ArrayList<>();
        Integer posicaoVisao = maiorPosicaoVisaoBaseadoOlfato(new int[]{1, 6});
        if (posicaoVisao != null) {
          aux.add(posicaoVisao);
        } else {
          if (ehCelularVazia(posicaoCimaVisao)) {
            aux.add(posicaoCimaVisao);
          }
          if (ehCelularVazia(posicaoBaixoVisao)) {
            aux.add(posicaoBaixoVisao);
          }
        }
        Collections.shuffle(aux); // Embaralhar
        return !aux.isEmpty() ? aux.get(0) : null;
      }
    } else {
      return null;
    }
  }

  private Integer posicaoPoupadorCimaEsquerdaVisao(int posicaoPoupador) {
    List<Integer> aux = new ArrayList<>();
    if (posicaoPoupador == posicaoCimaEsquerdaVisao) {
      if (ehCelularVazia(posicaoCimaVisao)) {
        aux.add(posicaoCimaVisao);
      }
      if (ehCelularVazia(posicaoEsquerdaVisao)) {
        aux.add(posicaoEsquerdaVisao);
      }
    }
    Collections.shuffle(aux); // Embaralhar
    return !aux.isEmpty() ? aux.get(0) : null;
  }

  private Integer posicaoPoupadorCimaDireitaVisao(int posicaoPoupador) {
    List<Integer> aux = new ArrayList<>();
    if (posicaoPoupador == posicaoCimaDireitaVisao) {
      if (ehCelularVazia(posicaoCimaVisao)) {
        aux.add(posicaoCimaVisao);
      }
      if (ehCelularVazia(posicaoDireitaVisao)) {
        aux.add(posicaoDireitaVisao);
      }
    }
    Collections.shuffle(aux); // Embaralhar
    return !aux.isEmpty() ? aux.get(0) : null;
  }

  private Integer posicaoPoupadorBaixoEsquerdaVisao(int posicaoPoupador) {
    List<Integer> aux = new ArrayList<>();
    if (posicaoPoupador == posicaoBaixoEsquerdaVisao) {
      if (ehCelularVazia(posicaoBaixoVisao)) {
        aux.add(posicaoBaixoVisao);
      }
      if (ehCelularVazia(posicaoEsquerdaVisao)) {
        aux.add(posicaoEsquerdaVisao);
      }
    }
    Collections.shuffle(aux); // Embaralhar
    return !aux.isEmpty() ? aux.get(0) : null;
  }

  private Integer posicaoPoupadorBaixoDireitaVisao(int posicaoPoupador) {
    List<Integer> aux = new ArrayList<>();
    if (posicaoPoupador == posicaoBaixoEsquerdaVisao) {
      if (ehCelularVazia(posicaoBaixoVisao)) {
        aux.add(posicaoBaixoVisao);
      }
      if (ehCelularVazia(posicaoDireitaVisao)) {
        aux.add(posicaoDireitaVisao);
      }
    }
    Collections.shuffle(aux); // Embaralhar
    return !aux.isEmpty() ? aux.get(0) : null;
  }

  private Integer posicaoPoupadorCimaEsquerdaVisaoOlfato(int posicaoPoupador) {
    List<Integer> aux = new ArrayList<>();
    for (int i = 0; i < listaPosicoesCimaEsquerda.length; i++) {
      if (posicaoPoupador == listaPosicoesCimaEsquerda[i]) {
        Integer posicaoVisao = maiorPosicaoVisaoBaseadoOlfato(new int[]{1, 3});
        if (posicaoVisao != null) {
          aux.add(posicaoVisao);
        } else {
          if (ehCelularVazia(posicaoCimaVisao)) {
            aux.add(posicaoCimaVisao);
          }
          if (ehCelularVazia(posicaoEsquerdaVisao)) {
            aux.add(posicaoEsquerdaVisao);
          }
        }
        break;
      }
    }
    Collections.shuffle(aux); // Embaralhar
    return !aux.isEmpty() ? aux.get(0) : null;
  }

  private Integer posicaoPoupadorCimaDireitaVisaoOlfato(int posicaoPoupador) {
    List<Integer> aux = new ArrayList<>();
    for (int i = 0; i < listaPosicoesCimaDireita.length; i++) {
      if (posicaoPoupador == listaPosicoesCimaDireita[i]) {
        Integer posicaoVisao = maiorPosicaoVisaoBaseadoOlfato(new int[]{1, 4});
        if (posicaoVisao != null) {
          aux.add(posicaoVisao);
        } else {
          if (ehCelularVazia(posicaoCimaVisao)) {
            aux.add(posicaoCimaVisao);
          }
          if (ehCelularVazia(posicaoDireitaVisao)) {
            aux.add(posicaoDireitaVisao);
          }
        }
        break;
      }
    }
    Collections.shuffle(aux); // Embaralhar
    return !aux.isEmpty() ? aux.get(0) : null;
  }

  private Integer posicaoPoupadorBaixoEsquerdaVisaoOlfato(int posicaoPoupador) {
    List<Integer> aux = new ArrayList<>();
    for (int i = 0; i < listaPosicoesBaixoEsquerda.length; i++) {
      if (posicaoPoupador == listaPosicoesBaixoEsquerda[i]) {
        Integer posicaoVisao = maiorPosicaoVisaoBaseadoOlfato(new int[]{3, 6});
        if (posicaoVisao != null) {
          aux.add(posicaoVisao);
        } else {
          if (ehCelularVazia(posicaoBaixoVisao)) {
            aux.add(posicaoBaixoVisao);
          }
          if (ehCelularVazia(posicaoEsquerdaVisao)) {
            aux.add(posicaoEsquerdaVisao);
          }
        }
        break;
      }
    }
    Collections.shuffle(aux); // Embaralhar
    return !aux.isEmpty() ? aux.get(0) : null;
  }

  private Integer posicaoPoupadorBaixoDireitaVisaoOlfato(int posicaoPoupador) {
    List<Integer> aux = new ArrayList<>();
    for (int i = 0; i < listaPosicoesBaixoDireita.length; i++) {
      if (posicaoPoupador == listaPosicoesBaixoDireita[i]) {
        Integer posicaoVisao = maiorPosicaoVisaoBaseadoOlfato(new int[]{4, 6});
        if (posicaoVisao != null) {
          aux.add(posicaoVisao);
        } else {
          if (ehCelularVazia(posicaoBaixoVisao)) {
            aux.add(posicaoBaixoVisao);
          }
          if (ehCelularVazia(posicaoDireitaVisao)) {
            aux.add(posicaoDireitaVisao);
          }
        }
        break;
      }
    }
    Collections.shuffle(aux); // Embaralhar
    return !aux.isEmpty() ? aux.get(0) : null;
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

  public Integer existePoupadorOlfato() {
    Integer posicao = null;
    Integer valor = 99;
    int[] visao = sensor.getAmbienteOlfatoPoupador();
    for (int i = 0; i < visao.length; i++) {
      if (visao[i] != 0 && visao[i] < valor && ehVizinhoOlfato(i) != null) {
        posicao = i;
        valor = visao[i];
      }
    }

    if (posicao == null) {
      valor = 0;
      for (int i = 0; i < visao.length; i++) {
        if (visao[i] != 0 && visao[i] < valor) {
          valor = visao[i];
          posicao = i;
        }
      }
    }

    return posicao;
  }

  public Integer maiorPosicaoVisaoBaseadoOlfato(int[] posicoesOlfato) {
    int[] olfatos = sensor.getAmbienteOlfatoPoupador();
    Integer posicaoOlfato = null;
    Integer valor = 99;
    for (int i = 0; i < posicoesOlfato.length; i++) {
      if (olfatos[posicoesOlfato[i]] != 0 && olfatos[posicoesOlfato[i]] < valor && ehVizinhoOlfato(posicoesOlfato[i]) != null) {
        posicaoOlfato = posicoesOlfato[i];
        valor = olfatos[posicoesOlfato[i]];
      }
    }
    if (posicaoOlfato != null) {
      return convertePosicaoOlfatoParaVisao(posicaoOlfato);
    }
    return null;
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

  /*private boolean ehCelularVazia(int index) {
    if (sensor.getVisaoIdentificacao()[index] == 0) {
      return true;
    }
    return false;
  }*/

  private boolean ehCelularVazia(int valor) {
    if (valor == 0) {
      return true;
    }
    return false;
  }

  private Integer convertePosicaoOlfatoParaVisao(int posicaoOlfato) {
    switch (posicaoOlfato) {
      case 1:
        return 7;
      case 3:
        return 11;
      case 4:
        return 12;
      case 6:
        return 16;
    }
    return null;
  }
}