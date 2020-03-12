package algoritmo;

public class Ladrao extends ProgramaLadrao {
  private int[][] caminho = new int[30][30];
  private int poupador = 100;

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
    /*if (contadorLoop == 3) {
      contadorLoop = 0;
      return (int) (Math.random() * 5);
    }*/
    if (x == (int) sensor.getPosicao().getX() && y == (int) sensor.getPosicao().getY()) {
      contadorLoop++;
    }
    x = (int) sensor.getPosicao().getX();
    y = (int) sensor.getPosicao().getY();
    this.caminho[x][y] = 1;

    if (ehPoupador(sensor.getVisaoIdentificacao()[7]) || ehPoupador(sensor.getVisaoIdentificacao()[12])
        || ehPoupador(sensor.getVisaoIdentificacao()[16]) || ehPoupador(sensor.getVisaoIdentificacao()[11])) {
      return parado;
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
    return (int) (Math.random() * 5);
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
}