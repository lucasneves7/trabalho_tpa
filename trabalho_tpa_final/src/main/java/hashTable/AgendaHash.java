package hashTable;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class AgendaHash {
    public ArrayList<ArrayList<Registro>> tabela;
    public int tamanho;

    public void initAgenda(){
        tabela = new ArrayList<>();
        for(int i = 0; i < tamanho; i++){
            tabela.add(new ArrayList());
        }
    }

    public int calcHash(String chave){
        int hash = 0, contador = 1;
        char[] chars = chave.toCharArray();
        for (char keyChar: chars) {
            hash = hash + (keyChar * ((contador * contador) * contador ));
            contador++;
        }

        return hash%tamanho;
    }

    public boolean insertReg(String chave, String telefone, String cidade, String pais){
        if(tamanho > 0){
            int hash = calcHash(chave);
            tabela.get(hash).add(new Registro(chave, telefone, cidade, pais));
            return true;
        }else{
            System.out.println("Tabela não carregada");
            return false;
        }
    }

    public Registro getHash(String chave){
        int hash = calcHash(chave);
        ArrayList<Registro> registros = tabela.get(hash);

        if(registros.size() == 1){
            return tabela.get(hash).get(0);
        }else{
            for (int i = 0; i < registros.size(); i++) {
                if(tabela.get(hash).get(i).chave.equalsIgnoreCase(chave)){
                    return tabela.get(hash).get(i);
                }
            }
        }
        return null;
    }

    public boolean delete(String chave){
        if(tamanho > 0){
            int hash = calcHash(chave);
            if(tabela.get(hash).size() ==1){
                tabela.get(hash).remove(0);
                return true;
            }else{
                for (int i = 0; i < tabela.get(hash).size(); i++) {
                    if(tabela.get(hash).get(i).chave.equalsIgnoreCase(chave)){
                        tabela.get(hash).remove(i);
                        return true;
                    }
                }
            }
        }else{
            System.out.println("Tabela não carregada");
            return false;
        }
       return false;
    }

    public boolean updateHash(String chave, String nome, String telefone, String cidade, String pais){
        if(tamanho > 0 && delete(chave)){
            insertReg(nome, telefone, cidade, pais);
            return true;
        }else{
            System.out.println("Tabela não carregada");
            return false;
        }
    }

    public void saveFile() throws IOException {
        if(tamanho > 0){
            BufferedWriter buff = new BufferedWriter(new FileWriter("saida.txt"));
            ArrayList<Registro> regList;
            Registro reg;
            String line;
            for(int i = 0; i<tamanho; i++){
                regList = tabela.get(i);
                if(regList != null && !regList.isEmpty()){
                    reg = regList.get(0);
                    line = reg.chave + "," + reg.telefone + "," + reg.cidade + "," + reg.pais + ",\n";
                    buff.append(line);

                    if(regList.size() > 1){
                        for(int j = 1; j < regList.size(); j++){
                            reg = regList.get(j);
                            line = reg.chave + "," + reg.telefone + "," + reg.cidade + "," + reg.pais + ",\n";
                            buff.append(line);
                        }
                    }
                }
            }
            buff.close();
        }else{
            System.out.println("Tabela não carregada");
        }

    }

    public void load() throws IOException {
        String[] contato;
        int cont = 0;
        ArrayList<String> contatos = new ArrayList<>();
        FileInputStream stream = new FileInputStream("hashIn.csv");
        InputStreamReader reader = new InputStreamReader(stream);
        BufferedReader br = new BufferedReader(reader);
        String linha = br.readLine();
        while(linha != null) {
            cont++;
            contatos.add(linha);
            linha = br.readLine();
        }
        br.close();
        this.tamanho = (contatos.size() * (contatos.size()/2));

        initAgenda();

        for (int i = 0; i < cont; i++){
            contato = contatos.get(i).split(",");
            if(contato.length == 4)insertReg(contato[0],contato[1],contato[2],contato[3]);
        }
    }

    public static void main(String[] args) throws IOException {
        Scanner entradaScanner = new Scanner(System.in);
        AgendaHash agenda = new AgendaHash();
        int entrada = 0;


        do {
            System.out.println(
                "1. Carregar arquivo\n2. Localizar Contato sando como chave de busca o Nome Completo." +
                "\n3. Inserir Contato Novo\n4. Excluir Contato\n" +
                "5. Atualizar Contato (Atualizar dados de Telefone, Cidade e Pais)\n6. Salvar Dados\n7. Fim do Programa"
            );

            entrada = entradaScanner.nextInt();
            switch (entrada)
            {
                case 1:// init
                    System.out.println("Carregando Arquivo...");
                    long horaInicial = System.currentTimeMillis();// Pegando a hora para saber o tempo de execução.
                    agenda.load();
                    long tempoTotal = System.currentTimeMillis() - horaInicial; //Pegando  otempo de execução.
                    System.out.println("Arquivo Carregado em " + tempoTotal);
                    break;
                case 2://Get
                    System.out.println("Busca");
                    Registro contato = null;
                    if(agenda.tamanho > 0){
                        Scanner entradaLocalizar = new Scanner(System.in);
                        System.out.println("chave(Nome): ");
                        String key = entradaLocalizar.nextLine();
                        horaInicial = System.currentTimeMillis();// Pegando a hora para saber o tempo de execução.
                        contato = agenda.getHash(key);
                        tempoTotal = System.currentTimeMillis() - horaInicial; //Pegando  otempo de execução.
                        System.out.println("Tempo:" + tempoTotal);
                    }

                    if(contato != null){
                        System.out.println(
                            "\nNome:" + contato.chave + "\nTel:" + contato.telefone + "\nCidade:" + contato.cidade +
                            "\nPais:" + contato.pais
                        );
                    }else{
                        System.out.println("Nada encontrado com o valor informado");
                    }
                    break;
                case 3://Insert
                    System.out.println("Inserir");
                    if(agenda.tamanho > 0){
                        Scanner insert = new Scanner(System.in);
                        System.out.println("Nome: ");
                        String name = insert.nextLine();

                        System.out.println("\nTelefone: ");
                        String tel = insert.nextLine();

                        System.out.println("Cidade: ");
                        String cidade = insert.nextLine();

                        System.out.println("Pais: ");
                        String pais =  insert.nextLine();

                        agenda.insertReg(name, tel, cidade, pais);

                        System.out.println(name + "Inserido com Sucesso \n|------------------------------------|\n\n");

                    }else{
                        System.out.println("Tabela não carregada");
                    }

                    break;
                case 4://delete
                    System.out.println("Deletar");
                    if(agenda.tamanho > 0){
                        Scanner scannerDel = new Scanner(System.in);
                        System.out.println("Chave(Nome):");
                        String name = scannerDel.nextLine();
                        boolean deleted = agenda.delete(name);
                        if(deleted){
                            System.out.println(name + "Deletado com Sucesso\n|------------------------------------|\n\n");

                        }else{
                            System.out.println("Não foi encontrado\n|------------------------------------|\n\n");

                        }
                    }else{
                        System.out.println("Tabela não carregada");
                    }

                    break;
                case 5://update
                    System.out.println("Atualizar");
                    if(agenda.tamanho > 0){
                        System.out.println("Atualizar Contato");

                        Scanner scannerUpdate = new Scanner(System.in);

                        System.out.println("Chave(Nome) a ser atualizado: ");
                        String chave = scannerUpdate.nextLine();

                        System.out.println("Novo Nome: ");
                        String name = scannerUpdate.nextLine();

                        System.out.println("Telefone: ");
                        String phone = scannerUpdate.nextLine();

                        System.out.println("Cidade: ");
                        String city = scannerUpdate.nextLine();

                        System.out.println("Pais: ");
                        String country =  scannerUpdate.nextLine();
                        boolean updated = agenda.updateHash(chave, name, phone, city, country);
                        if(updated){
                            System.out.println(name + "Atualizado com Sucesso\n|------------------------------------|\n\n");

                        }else{
                            System.out.println("Erro ao atualizar\n|------------------------------------|\n\n");
                        }
                    }else{
                        System.out.println("Tabela Vazia");
                    }

                    break;
                case 6://save
                    agenda.saveFile();
                    break;
            }
        }while (entrada != 7);

    }

}


