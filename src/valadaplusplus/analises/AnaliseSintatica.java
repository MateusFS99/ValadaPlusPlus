package valadaplusplus.analises;

import entidades.Token;
import java.util.List;

public class AnaliseSintatica {
    
    private int index, nivel;
    private boolean erro;
    private String saida;
    private List<Token> tokens;
    

    public AnaliseSintatica(List<Token> tokens) {
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
        
        return !token.getToken().getToken().equals("ID") &&
                !token.getToken().getToken().equals("INTEGER") &&
                !token.getToken().getToken().equals("DOUBLE") &&
                !token.getToken().getToken().equals("CHAR") &&
                !token.getToken().getToken().equals("STRING") &&
                !token.getToken().getToken().equals("BOOLEAN");
    }
    
    public void analisar() {
        
        index = 2;
        nivel = 1;
        if(tokens.get(0).getToken().getToken().equals("COMEÇO") && tokens.get(1).getToken().getToken().equals("CHAVES")) {
            
            analisador();
            if(nivel > 0 && saida == "")
                saida += "Erro, Linha " + tokens.get(tokens.size() - 1).getLinha()+ " Esperado '}'";
        }
        else {
            
            if(!tokens.get(0).getToken().getToken().equals("COMEÇO"))
                saida += "Erro, Linha " + tokens.get(0).getLinha() + " Esperado 'begin'";
            else if(!tokens.get(1).getToken().getToken().equals("CHAVES"))
                saida += "Erro, Linha " + tokens.get(1).getLinha() + " Esperado '{'";
        }
        if(saida.equals(""))
            saida = "OK";
        else
            erro = true;
    }
    
    private void analisador() {
        
        while(index < tokens.size()) {
            
            switch(tokens.get(index).getToken().getTipo()) {
                
                case "decl": declaracao(); break;
                case "aloc": alocacao(); break;
                case "if": comandoIF(); break;
                case "repeat": comandoRepeat(); break;
                case "abreChaves": nivel++; break;
                case "fechaChaves": nivel--; break;
                default: 
                    saida += "Erro, Linha " + tokens.get(index - 1).getLinha() + " Esperado " + 
                            tokens.get(index - 1).getToken().getFollow() + ", Depois de " + tokens.get(index - 1).getNome() + "\n";
                    erro = true;
            } 
            index++;
        }
    }
    
    private void declaracao() {
        
        boolean flag = true;
        
        index++;
        if(tokens.get(index).getToken().getToken().equals("ID") && 
            tokens.get(index + 1).getToken().getToken().equals("FIMCOMANDO") || 
            tokens.get(index + 1).getToken().getToken().equals("ATRIBUIÇÃO")) {
            
            if(tokens.get(index + 1).getToken().getToken().equals("ATRIBUIÇÃO"))
                index++;
            index++;
            while(index < tokens.size() && !tokens.get(index).getToken().getToken().equals("FIMCOMANDO") && saida.equals("")) {
                
                flag = true;
                for(int i = 0; i < tokens.get(index - 1).getToken().getFollow().size() && flag; i++)
                    if(tokens.get(index - 1).getToken().getFollow().get(i).equals(tokens.get(index).getToken().getToken()))
                        flag = false;
                if(!flag)
                    index++;
                else
                    saida += "Erro, Linha " + tokens.get(index - 1).getLinha() + " Esperado " + 
                        tokens.get(index - 1).getToken().getFollow() + ", Depois de " + tokens.get(index - 1).getNome();
            }
            if(retornaTipo(tokens.get(index - 1)))
                saida += "Erro, Linha " + tokens.get(index - 1).getLinha() + " Esperado ID ou TIPO DE DADOS" + 
                    ", Depois de " + tokens.get(index - 1).getNome();
        }
        else
            saida += "Erro, Linha " + tokens.get(index - 1).getLinha() + 
                " Esperado ATRIBUIÇÃO ou FIMCOMANDO" + ", Depois de " + tokens.get(index - 1).getNome();
        
    }
    
    private void alocacao() {
        
        boolean flag = true;
        
        if(tokens.get(index + 1).getToken().getToken().equals("ATRIBUIÇÃO")) {
            
            index += 2;
            while(index < tokens.size() && !tokens.get(index).getToken().getToken().equals("FIMCOMANDO") && saida.equals("")) {
                
                flag = true;
                for(int i = 0; i < tokens.get(index - 1).getToken().getFollow().size() && flag; i++) 
                    if(tokens.get(index - 1).getToken().getFollow().get(i).equals(tokens.get(index).getToken().getToken()))
                        flag = false;
                if(!flag)
                    index++;
                else
                    saida += "Erro, Linha " + tokens.get(index - 1).getLinha() + " Esperado " + 
                        tokens.get(index - 1).getToken().getFollow() + ", Depois de " + tokens.get(index - 1).getNome();
            }
            if(retornaTipo(tokens.get(index - 1)))
                saida += "Erro, Linha " + tokens.get(index - 1).getLinha() + " Esperado ID ou TIPO DE DADOS" + 
                    ", Depois de " + tokens.get(index - 1).getNome();
        }
        else
            saida += "Erro, Linha " + tokens.get(index - 1).getLinha() + 
                " Esperado ATRIBUIÇÃO" + ", Depois de " + tokens.get(index - 1).getNome();
    }
    
    private void comandoIF() {
        
        boolean flag = true;
        
        index++;
        for(int i = 0; i < tokens.get(index - 1).getToken().getFollow().size() && flag; i++) 
            if(tokens.get(index - 1).getToken().getFollow().get(i).equals(tokens.get(index).getToken().getToken()))
                flag = false;
        if(!flag) {
            
            index++;
            while(index < tokens.size() && !tokens.get(index).getToken().getToken().equals("CHAVES") && 
                !tokens.get(index).getToken().getToken().equals("FIMCOMANDO") && saida.equals("")) {
                
                flag = true;
                if(tokens.get(index).getToken().getToken().equals("IGUALDADE") ||
                        tokens.get(index).getToken().getToken().equals("DIFERENÇA") ||
                        tokens.get(index).getToken().getToken().equals("MENOR") ||
                        tokens.get(index).getToken().getToken().equals("MENORIGUAL") ||
                        tokens.get(index).getToken().getToken().equals("MAIOR") ||
                        tokens.get(index).getToken().getToken().equals("MAIORIGUAL"))
                    flag = false;
                if(!flag) {
                    
                    flag = true;
                    index++;
                    for (int i = 0; i < tokens.get(index - 1).getToken().getFollow().size() && flag; i++)
                        if(tokens.get(index - 1).getToken().getFollow().get(i).equals(tokens.get(index).getToken().getToken()))
                            flag = false;
                    if(!flag)
                        index++;
                    else
                        saida += "Erro, Linha " + tokens.get(index - 1).getLinha() + " Esperado " + 
                            tokens.get(index - 1).getToken().getFollow() + ", Depois de " + tokens.get(index - 1).getNome();
                }
                else
                    if(tokens.get(index - 1).getToken().getToken().equals("ID"))
                        saida += "Erro, Linha " + tokens.get(index - 1).getLinha() + " Esperado " + 
                            tokens.get(index - 1).getToken().getFollow() + ", Depois de " + tokens.get(index - 1).getNome();
            }
            if(retornaTipo(tokens.get(index - 1)))
                saida += "Erro, Linha " + tokens.get(index - 1).getLinha() + " Esperado ID ou TIPO DE DADOS" + 
                    ", Depois de " + tokens.get(index - 1).getNome();
        }
        else
            saida += "Erro, Linha " + tokens.get(index - 1).getLinha() + 
                " Esperado ATRIBUIÇÃO" + ", Depois de " + tokens.get(index - 1).getNome();
        index--;
    }
    
    private void comandoRepeat() {
        
        index++;
        declaracao();
        if(saida.equals("")) {
            
            comandoIF();
            index++;
            if(tokens.get(index).getToken().getToken().equals("FIMCOMANDO")) {
                
                index++;
                alocacao();
                if(saida.equals("")) 
                    if(!tokens.get(index + 1).getToken().getToken().equals("CHAVES"))
                        saida += "Erro, Linha " + tokens.get(index).getLinha() + " Esperado { , Depois de " + 
                            tokens.get(index).getNome();
            }
            else
                saida += "Erro, Linha " + tokens.get(index - 1).getLinha() + " Esperado " + 
                    tokens.get(index - 1).getToken().getFollow() + ", Depois de " + tokens.get(index - 1).getNome();
        }
    }
}