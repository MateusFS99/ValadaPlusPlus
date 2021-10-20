package entidades;

import java.util.ArrayList;
import java.util.List;

public class FirstFolow {
    
    private String token, tipo;
    private List follow;

    public FirstFolow() {
        this.follow = new ArrayList();
    }

    public FirstFolow(String token) {
        this.token = token;
        this.follow = new ArrayList();
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public List getFollow() {
        return follow;
    }

    public void setFollow(List follow) {
        this.follow = follow;
    }
}