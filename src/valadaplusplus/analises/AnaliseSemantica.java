package valadaplusplus.analises;

import entidades.Token;
import java.util.List;

public class AnaliseSemantica {
    
    private boolean erro;
    private String saida;
    private List<Token> tokens;

    public AnaliseSemantica(List<Token> tokens) {
        this.erro = false;
        this.saida = "";
        this.tokens = tokens;
    }

    public boolean isErro() {
        return erro;
    }

    public String getSaida() {
        return saida;
    }
    
    private boolean retornaTipo(Token token) {
        
        return token.getToken().getToken().equals("TIPOINTEGER") ||
                token.getToken().getToken().equals("TIPODOUBLE") ||
                token.getToken().getToken().equals("TIPOCHAR") ||
                token.getToken().getToken().equals("TIPOSTRING") ||
                token.getToken().getToken().equals("TIPOBOOLEAN");
    }
    
    public void analisar() {
        
        uso();
        declaracao();
        alocacao();
        if(saida.equals(""))
            saida = "OK";
    }
    
    private void uso() {
        
        for(int i = 0; i < tokens.size(); i++) {
            
            if(tokens.get(i).getToken().getToken().equals("ID") && retornaTipo(tokens.get(i - 1))) {

                boolean flag = false;
                String nome = tokens.get(i).getNome();

                for(int j = i + 1; j < tokens.size() && !flag; j++) 
                   if(tokens.get(j).getNome().equals(nome))
                       flag = true;
                if(!flag)
                    saida += "Atenção, Linha " + tokens.get(i).getLinha() + " Variável \"" + nome + "\" Nunca é Utilizada!\n";
            }
        }   
    }
    
    private void declaracao() {

        for(int i = 0; i < tokens.size(); i++) {
            
            if(tokens.get(i).getToken().getToken().equals("ID") && retornaTipo(tokens.get(i - 1))) {

                boolean flag = true;
                String nome = tokens.get(i).getNome();

                for(int j = 0; j < i - 2 && flag; j++) 
                   if(tokens.get(j).getNome().equals(nome))
                       flag = false;
                if(!flag)
                    saida += "Erro, Linha " + tokens.get(i).getLinha() + " Variável \"" + nome + "\" já Está Declarado!\n";
            }
        }     
    }
    
    private void alocacao() {
        
        for(int i = 0; i < tokens.size(); i++) {
            
            if(tokens.get(i).getToken().getToken().equals("ID") && 
                tokens.get(i + 1).getToken().getToken().equals("ATRIBUIÇÃO")) {
                
                if(!tokens.get(i + 2).getToken().getToken().equals("ID") &&
                    !tokens.get(i - 1).getToken().getToken().equals("TIPO" + tokens.get(i + 2).getToken().getToken()))
                    saida += "Erro, Linha " + tokens.get(i).getLinha() + ", " + tokens.get(i + 2).getToken().getToken().toUpperCase() + 
                            " Não Pode Ser Convertido em " + tokens.get(i - 1).getToken().getToken() + "\n";
                else if(!tokens.get(i - 1).getToken().getToken().equals("TIPO" + tokens.get(i + 2).getToken().getToken()))
                    saida += "Erro, Linha " + tokens.get(i).getLinha() + ", " + tokens.get(i + 2).getToken().getToken() + 
                            " Não Pode Ser Convertido em " + tokens.get(i - 1).getToken().getToken() + "\n";
            }
        }
    }
}