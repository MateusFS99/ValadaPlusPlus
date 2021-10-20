package valadaplusplus.analises;

import entidades.FirstFolow;
import entidades.Token;
import java.util.ArrayList;
import java.util.List;

public class AnaliseLexica {
    
    private boolean erro;
    private String saida;
    private List<Token> tokens;

    public AnaliseLexica() {
        this.erro = false;
        this.saida = "";
        this.tokens = new ArrayList();
    }

    public boolean isErro() {
        return erro;
    }

    public String getSaida() {
        return saida;
    }

    public List<Token> getTokens() {
        return tokens;
    }
    
    public void analisar(String codigo) {
        
        int i = 0, count = 1;
        String[] linhas, palavras;
        
        tokens = new ArrayList<Token>();
        linhas = codigo.trim().split("\n");
        for(String linha : linhas) {
            
            palavras = linha.trim().split(" |\\(|\\)");
            for(String palavra : palavras) {
                
                if(!palavra.equals(" ") && !palavra.equals("")) {
                    
                    boolean flag = true;
                    
                    for (int j = 0; j < tokens.size() && flag; j++) {
                        
                        if(tokens.get(j).getToken().getToken().equals("ID") && 
                            tokens.get(j).getNome().equals(palavra.replace(";", ""))) {
                            
                            Token token = new Token(count, palavra, new FirstFolow("ID"));
                            
                            tokens.add(token);
                            token.getToken().setTipo("aloc");
                            token.getToken().getFollow().add("FIMCOMANDO");
                            token.getToken().getFollow().add("ADIÇÃO");
                            token.getToken().getFollow().add("SUBTRAÇÃO");
                            token.getToken().getFollow().add("MULTIPLICAÇÃO");
                            token.getToken().getFollow().add("DIVISÃO");
                            if(palavra.endsWith(";")) {
                                
                                tokens.get(i).setNome(tokens.get(i).getNome().substring(0, tokens.get(i).getNome().length() - 1));
                                tokens.add(new Token(count, ";", retornaToken(";", i++)));
                            }
                            flag = false;
                        }
                        else if(palavra.replace(";", "").equals("begin") && tokens.get(j).getNome().equals("begin")) {
                            
                            tokens.add(new Token(count, palavra, new FirstFolow("Erro: Palavra Reservada")));
                            if(palavra.endsWith(";")) {
                                
                                tokens.get(i).setNome(tokens.get(i).getNome().substring(0, tokens.get(i).getNome().length() - 1));
                                tokens.add(new Token(count, ";", retornaToken(";", i++)));
                            }
                            flag = false;
                        }
                            
                    }
                    if(flag) {
                        
                        if(palavra.charAt(0) == '"') 
                            tokens.add(new Token(count, palavra.replace(";", ""), new FirstFolow("STRING")));
                        else if(palavra.substring(0, 1).equals("'")) 
                            tokens.add(new Token(count, palavra.replace(";", ""), new FirstFolow("CHAR")));
                        else 
                            tokens.add(new Token(count, palavra, retornaToken(palavra.replace(";", ""), i)));
                        if(palavra.endsWith(";")) {

                            tokens.get(i).setNome(tokens.get(i).getNome().substring(0, tokens.get(i).getNome().length() - 1));
                            tokens.add(new Token(count, ";", retornaToken(";", i++)));
                        }
                    }
                }
                i++;
            }
            count++;
        }
        for(Token token : tokens) {
            
            if(token.getToken().getToken().equals("STRING"))
                saida += "linha " + token.getLinha() + " --> token = \"" + token.getToken().getToken() + "\" --> " + 
                    token.getNome() + "\n";
            else if(token.getToken().getToken().equals("CHAR")) 
                saida += "linha " + token.getLinha() + " --> token = \"" + token.getToken().getToken() + "\" --> \'" + 
                    token.getNome() + "\'\n";
            else
                saida += "linha " + token.getLinha() + " --> token = \"" + token.getToken().getToken() + "\" --> \"" + 
                    token.getNome() + "\"\n";
        }
    }
    
    private FirstFolow retornaToken(String palavra, int pos) {
        
        FirstFolow token = new FirstFolow("Erro, Palavra Desconhecida");
        
        switch(palavra) {
            
            case "begin": token.setToken("COMEÇO"); break;
            case "int":
                token.setToken("TIPOINTEGER");
                token.setTipo("decl");
                token.getFollow().add("ID");
            break;
            case "double":
                token.setToken("TIPODOUBLE");
                token.setTipo("decl");
                token.getFollow().add("ID");
            break;
            case "char":
                token.setToken("TIPOCHAR");
                token.setTipo("decl");
                token.getFollow().add("ID");
            break;
            case "string":
                token.setToken("TIPOSTRING");
                token.setTipo("decl");
                token.getFollow().add("ID");
            break;
            case "boolean":
                token.setToken("TIPOBOOLEAN");
                token.setTipo("decl");
                token.getFollow().add("ID");
            break;
            case "true":
                token.setToken("BOOLEAN");
                token.getFollow().add("FIMCOMANDO");
            break;
            case "false":
                token.setToken("BOOLEAN");
                token.getFollow().add("FIMCOMANDO");
            break;
            case "if":
                token.setToken("SE");
                token.setTipo("if");
                token.getFollow().add("ID");
                token.getFollow().add("INTEGER");
                token.getFollow().add("DOUBLE");
                token.getFollow().add("BOOLEAN");
                token.getFollow().add("STRING");
            break;
            case "repeat":
                token.setToken("REPETIR");
                token.setTipo("repeat");
                token.getFollow().add("ID");
                token.getFollow().add("INTEGER");
                token.getFollow().add("DOUBLE");
                token.getFollow().add("BOOLEAN");
                token.getFollow().add("STRING");
            break;
            case "{": 
                token.setToken("CHAVES");
                token.setTipo("abreChaves");
                break;
            case "}":
                token.setToken("CHAVES");
                token.setTipo("fechaChaves");
            break;
            case "=":
                token.setToken("ATRIBUIÇÃO");
                token.getFollow().add("ID");
                token.getFollow().add("INTEGER");
                token.getFollow().add("DOUBLE");
                token.getFollow().add("BOOLEAN");
                token.getFollow().add("STRING");
            break;
            case "+":
                token.setToken("ADIÇÃO");
                token.getFollow().add("ID");
                token.getFollow().add("INTEGER");
                token.getFollow().add("DOUBLE");
            break;
            case "-":
                token.setToken("SUBTRAÇÃO");
                token.getFollow().add("ID");
                token.getFollow().add("INTEGER");
                token.getFollow().add("DOUBLE");
            break;
            case "*":
                token.setToken("MULTIPLICAÇÃO");
                token.getFollow().add("ID");
                token.getFollow().add("INTEGER");
                token.getFollow().add("DOUBLE");
            break;
            case "/":
                token.setToken("DIVISÃO");
                token.getFollow().add("ID");
                token.getFollow().add("INTEGER");
                token.getFollow().add("DOUBLE");
            break;
            case "==":
                token.setToken("IGUALDADE");
                token.getFollow().add("ID");
                token.getFollow().add("INTEGER");
                token.getFollow().add("DOUBLE");
                token.getFollow().add("BOOLEAN");
                token.getFollow().add("STRING");
            break;
            case "!=":
                token.setToken("DIFERENÇA");
                token.getFollow().add("ID");
                token.getFollow().add("INTEGER");
                token.getFollow().add("DOUBLE");
                token.getFollow().add("BOOLEAN");
                token.getFollow().add("STRING");
            break;
            case "<":
                token.setToken("MENOR");
                token.getFollow().add("ID");
                token.getFollow().add("INTEGER");
                token.getFollow().add("DOUBLE");
            break;
            case "<=":
                token.setToken("MENORIGUAL");
                token.getFollow().add("ID");
                token.getFollow().add("INTEGER");
                token.getFollow().add("DOUBLE");
            break;
            case ">":
                token.setToken("MAIOR");
                token.getFollow().add("ID");
                token.getFollow().add("INTEGER");
                token.getFollow().add("DOUBLE");
            break;
            case ">=":
                token.setToken("MAIORIGUAL");
                token.getFollow().add("ID");
                token.getFollow().add("INTEGER");
                token.getFollow().add("DOUBLE");
            break;
            case ";":
                token.setToken("FIMCOMANDO");
                token.setTipo("");
                token.getFollow().add("ID");
                token.getFollow().add("TIPOINTEGER");
                token.getFollow().add("TIPODOUBLE");
                token.getFollow().add("TIPOCHAR");
                token.getFollow().add("TIPOSTRING");
                token.getFollow().add("TIPOBOOLEAN");
                token.getFollow().add("IF");
                token.getFollow().add("REPETIR");
            break;
            default: 
                try {
                    
                    Integer.parseInt(palavra.substring(0, 1));
                }
                catch(Exception e) {
                    
                    if(pos > 0 && tokens.get(pos - 1).getToken().getToken().equals("TIPOINTEGER") ||
                        tokens.get(pos - 1).getToken().getToken().equals("TIPODOUBLE") ||
                        tokens.get(pos - 1).getToken().getToken().equals("TIPOCHAR") ||
                        tokens.get(pos - 1).getToken().getToken().equals("TIPOSTRING") ||
                        tokens.get(pos - 1).getToken().getToken().equals("TIPOBOOLEAN")) {

                        token.setToken("ID");
                        token.setTipo("aloc");
                        token.getFollow().add("FIMCOMANDO");
                        token.getFollow().add("SOMA");
                        token.getFollow().add("SUBTRÇÃO");
                        token.getFollow().add("MULTIPLICAÇÃO");
                        token.getFollow().add("DIVISÃO");
                    } 
                }
                try {

                    Integer.parseInt(palavra);
                    token.setToken("INTEGER");
                    token.getFollow().add("FIMCOMANDO");
                    token.getFollow().add("SOMA");
                    token.getFollow().add("SUBTRÇÃO");
                    token.getFollow().add("MULTIPLICAÇÃO");
                    token.getFollow().add("DIVISÃO");
                } 
                catch (Exception e2) {

                    try {

                        Double.parseDouble(palavra);
                        token.setToken("DOUBLE");
                        token.setTipo("");
                        token.getFollow().add("FIMCOMANDO");
                        token.getFollow().add("SOMA");
                        token.getFollow().add("SUBTRÇÃO");
                        token.getFollow().add("MULTIPLICAÇÃO");
                        token.getFollow().add("DIVISÃO");
                    } 
                    catch (Exception e3) {}
                }
        }
        if(token.getToken().equals("Erro, Palavra Desconhecida"))
            erro = true;
        
        return token;
    }
}