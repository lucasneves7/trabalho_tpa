package merge;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;

public class KwaySort {
    public static void kwayMerge(String arq) throws IOException {
        //Inicio as variaveis que utilizarei no codigo e as auxiliares
        //Um numero total de registros por arquivo, a quantidade de arquivos
        int totalBuffer =  200000, numFiles = 0, total = 0, index = 1;
        ArrayList merge = new ArrayList<String>(), aux;

        BufferedReader buffRead = new BufferedReader(new FileReader(arq));
        String line = buffRead.readLine();

        while(line != null){
            merge.add(line);
            total++;
            if(total == totalBuffer){
                numFiles++;
                Collections.sort(merge);
                writeFile(merge, arq + "." + numFiles);
                merge.clear();
                total = 0;
            }
            line = buffRead.readLine();
        }

        if(total > 0){
            numFiles++;
            Collections.sort(merge);
            writeFile(merge, arq + "." + numFiles);
            merge.clear();
        }

        buffRead.close();

        merge = readFile(arq + "." + index);
        if(numFiles > 2){
            for(index = 2; index < numFiles + 1; index++){
                aux = readFile(arq + "." + index);
                merge = merging(aux, merge);
            }
        }
        writeFile(merge, arq + "-orderly");
    }
    
    //Fun��o responsavel por ler os arquivos da memoria secundaria
    public static ArrayList readFile(String arq) throws IOException{
        ArrayList reader = new ArrayList<String>();
        BufferedReader buffRead = new BufferedReader(new FileReader(arq));
        String line = buffRead.readLine();
        while (line != null) {
            reader.add(line);
            line = buffRead.readLine();
        }
        buffRead.close();
        return reader;
    }
    
    //Grava o vetor na memora secundaria.
    public static void writeFile(ArrayList array, String arq) throws IOException{
        BufferedWriter file = new BufferedWriter(new FileWriter(arq));

        for(int i = 0; i<array.size(); i++){
            file.append(array.get(i) + "\n");
        }
        file.close();
    }
    //Merging dos dados.
    public static ArrayList merging(ArrayList<String> v1, ArrayList<String> v2){
        ArrayList temp = new ArrayList();
        int index1 = 0, index2 = 0;

        while (index1 < v1.size() && index2 < v2.size()) {
            if (v1.get(index1).compareTo(v2.get(index2)) <= 0) {
                temp.add(v1.get(index1));
                index1++;
            } else {
                temp.add(v2.get(index2));
                index2++;
            }
        }
        while(index1 < v1.size()) {
            temp.add(v1.get(index1));
            index1++;
        }
        while (index2 < v2.size()){
            temp.add(v2.get(index2));
            index2++;
        }

        return temp;
    }

    public static void main(String[] args) throws IOException {
        final long horaInicial = System.currentTimeMillis();// Pegando a hora para saber o tempo de execução.
        kwayMerge("entrada.csv");
        final long tempoTotal = System.currentTimeMillis() - horaInicial; //Pegando  otempo de execução.
        System.out.println("Tempo de de execução" + tempoTotal);

    }
}
