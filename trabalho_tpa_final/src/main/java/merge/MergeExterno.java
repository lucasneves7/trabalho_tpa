package merge;

import java.io.*;

public class MergeExterno {
    public static String[] nameFiles = new String[4];

    public static int IN1 = 0;
    public static int IN2 = 1;
    public static int OUT1 = 2;
    public static int OUT2 = 3;
    //Divide a lista em dois e retorna o tamanho da lista
    static int divider(String inFile) throws IOException {
        BufferedReader in = new BufferedReader(new FileReader(inFile));

        BufferedWriter in1 = new BufferedWriter(new FileWriter(nameFiles[IN1]));
        BufferedWriter in2 = new BufferedWriter(new FileWriter(nameFiles[IN2]));

        int total = 0, count = 1;

        while (count != -1){

            String line = in.readLine();
            if(line != null){
                if (count % 2 == 0){
                    in1.append(line + "\n");
                }else{
                    in2.append(line + "\n");
                }
                count++;
            }else{
                total = count;
                count = -1;
            }

        }

        in.close();in2.close();in1.close();
        return total;
    }
    //Realiza o merge de 2 arquivos
    public static void mergeExterno(String in_1, String in_2, String out_1, String out_2, int roundSize) throws IOException {
        BufferedReader in1 = new BufferedReader(new FileReader(in_1));
        BufferedReader in2 = new BufferedReader(new FileReader(in_2));

        BufferedWriter out1 = new BufferedWriter(new FileWriter(out_1));
        BufferedWriter out2 = new BufferedWriter(new FileWriter(out_2));

        BufferedWriter temp = out1;

        int index1 = 0, index2 = 0;
        String  val1 = in1.readLine(), val2 = in2.readLine();


        while (val1 != null || val2 != null){

            for(int count = 0;count < roundSize*2; count++){
                if(val1 != null && val2 != null && val1.compareTo(val2) <= 0 && index1 < roundSize){
                    temp.append(val1 + "\n");
                    index1++;
                    val1 = in1.readLine();
                }else if(val2 != null && index2 < roundSize){
                    temp.append(val2 + "\n");
                    index2++;
                    val2 = in2.readLine();
                }else if(val1 != null && index1 < roundSize){
                    temp.append(val1 + "\n");
                    index1++;
                    val1 = in1.readLine();
                }else{
                }
            }
            index1 = 0; index2 = 0;
            if(temp == out1) temp = out2; else temp = out1;
        }
        in1.close();in2.close();out1.close();out2.close();
    }
    //Realiza varias rodadas at� ordenar todo o conteudo
    static void mergeSortExterno(String infile) throws IOException {
        int totalRuns = divider(infile);
        String in1 = nameFiles[IN1], in2 = nameFiles[IN2], out1 = nameFiles[OUT1], out2 = nameFiles[OUT2], aux1, aux2;
        int round = 1;
        while (round < totalRuns*2){
            mergeExterno(in1, in2, out1, out2, round);
            round = round * 2;
            aux1 = in1; aux2 = in2; in1 = out1; in2 = out2; out1 = aux1; out2 = aux2;
        }
        mergeExterno(in1, in2, out1, out2, round);
    }

    public static void main(String[] args) throws IOException {

        String output = "converted.csv";
        String input = "entrada.csv";

        nameFiles[IN1] = input + ".1";
        nameFiles[IN2] = input + ".2";
        nameFiles[OUT1] = output + ".1";
        nameFiles[OUT2] = output + ".2";

        final long horaInicial = System.currentTimeMillis();// Pegando a hora para saber o tempo de execução.
        mergeSortExterno(input);
        final long tempoTotal = System.currentTimeMillis() - horaInicial; //Pegando  otempo de execução.
        System.out.println("Tempode de execução" + tempoTotal);


    }
}

