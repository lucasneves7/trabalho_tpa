package hashTable;

public class Registro {
    public String chave;
    public String telefone;
    public String cidade;
    public String pais;

    public Registro (String chave, String telefone, String cidade, String pais){
        this.chave = chave;
        this.telefone = telefone;
        this.cidade = cidade;
        this.pais = pais;
    }

    public Registro(){
        this.chave = null;
        this.telefone = null;
        this.cidade = null;
        this.pais = null;
    }
}
