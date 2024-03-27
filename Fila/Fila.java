
public class Fila
{
    public double tempoGlobal;
    private int servidores;
    private int capacidadeFila;
    private int tamanhoFila = 0;
    private int[] mediaChegada = new int[2];
    private int[] mediaSaida = new int[2];
    private double tempoProximaChegada = 0;
    private double tempoProximaSaida = 0;
    private int perdas = 0;
    
    
    private static double proximo = 1025555898; //0.8; //semente - parametros do gerador de codigos pseudoaleatorios
    private static double m = Math.pow(2, 32); // M   
    
    

    public Fila(int servidores, int capacidade, double chegadaInicial, int chegadas[], int atendimentos[]){
        this.servidores = servidores;
        this.capacidadeFila = capacidade;
        this.tempoGlobal = chegadaInicial;
        this.mediaChegada[0] = chegadas[0];
        this.mediaChegada[1] = chegadas[1];
        this.mediaSaida[0] = atendimentos[0];
        this.mediaSaida[1] = atendimentos[1];
    }
    

    private void chegada()
    {
       if(tamanhoFila<capacidadeFila){
           tamanhoFila++;
           if(tamanhoFila<=servidores) tempoProximaSaida = tempoGlobal+agendaEvento(mediaSaida[0],mediaSaida[1]);
           else perdas++;
           tempoProximaChegada = tempoGlobal+agendaEvento(mediaChegada[0],mediaChegada[1]);
       }
    }
    
    private void saida()
    {
       tamanhoFila--;
       if(tamanhoFila>=servidores) tempoProximaSaida = tempoProximaChegada+agendaEvento(mediaSaida[0],mediaSaida[1]);
    }
    
    private double agendaEvento(int min, int max){
        return (max-min)*(pseudos(proximo) / m)+min;
    }
            
    private double pseudos(double proximo) {
        int a = 1664525; 
        int c = 1013904223; 
        proximo = (a * proximo + c) % m;
        return proximo;
    }
    
    public void main() {
        int x=0; 
        double estado[] = new double[capacidadeFila+1];
        while (x < 100000) {
            if(x==0) {
                estado[tamanhoFila] = tempoGlobal;
                chegada();
            }
            else{
                if(tempoProximaChegada<=tempoProximaSaida || tempoGlobal==tempoProximaSaida){
                    estado[tamanhoFila] += tempoProximaChegada-tempoGlobal;
                    tempoGlobal = tempoProximaChegada;
                    chegada();
                }
                else{
                    estado[tamanhoFila] += tempoProximaSaida-tempoGlobal;
                    tempoGlobal = tempoProximaSaida;
                    saida();
                }
            }
            x++;
        }
        
        System.out.println("Tempos acumulados para os estados:");
        for (int i = 0; i < estado.length; i++) {
            System.out.println("Estado " + i + ": " + estado[i]);
        }

        
        double tempoTotal = 0;
        for (int i = 0; i < estado.length; i++) {
            tempoTotal += estado[i];
        }

        System.out.println("\nDistribuição de probabilidades:");
        for (int i = 0; i < estado.length; i++) {
            double probabilidade = estado[i] / tempoTotal;
            System.out.println("Estado " + i + ": " + probabilidade);
        }
        
        System.out.println("\nPerdas: "+ perdas);
        System.out.println("\nTempoGlobal: "+ tempoGlobal);
        System.out.println("\n\n\n\n");
        
    }
}
