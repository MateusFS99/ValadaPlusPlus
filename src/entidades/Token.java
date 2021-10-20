package entidades;

public class Token {
    
    private int linha;
    private String nome;
    private FirstFolow token;

    public Token() {}

    public Token(int linha, String nome, FirstFolow token) {
        this.linha = linha;
        this.nome = nome;
        this.token = token;
    }

    public int getLinha() {
        return linha;
    }

    public void setLinha(int linha) {
        this.linha = linha;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public FirstFolow getToken() {
        return token;
    }

    public void setToken(FirstFolow token) {
        this.token = token;
    }
}