package algoritmo;

public class LadraoPerseguicao extends ProgramaLadrao {
  private int[][] caminho = new int[30][30];

  private int contadorLoop = 0;
  private int x = 0;
  private int y = 0;

  private int parado = 0;
  private int cima = 1;
  private int baixo = 2;
  private int direita = 3;
  private int esquerda = 4;

  private static int[] experiencia = new int[]{0, 0, 0, 0};


  public int acao() {
    boolean teste = false;
    for (int i = 0; i < sensor.getAmbienteOlfatoPoupador().length; i++) {
      if (sensor.getAmbienteOlfatoPoupador()[i] > 0) {
        teste = true;
      }
    }

    if (teste) {
      faro();
    }


    if (contadorLoop == 3) {
      contadorLoop = 0;
      return (int) (Math.random() * 5);
    }

    if (x == (int) sensor.getPosicao().getX() && y == (int) sensor.getPosicao().getY()) {
      contadorLoop++;
    }
    x = (int) sensor.getPosicao().getX();
    y = (int) sensor.getPosicao().getY();
    this.caminho[x][y] = 1;

    Integer posicaoVizinho = ehVizinhoVisao();
    if (posicaoVizinho != null) {
      return roubar(posicaoVizinho);
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
    return random();
  }

  private boolean ehPoupador(int valor) {
    if (valor == 100 || valor == 110) {
      return true;
    }
    return false;
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

    /*private int segueFaro(int posicaoFaroPoupador) {
    }*/


}
