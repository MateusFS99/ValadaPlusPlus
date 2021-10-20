package valadaplusplus;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTabPane;
import com.jfoenix.controls.JFXTextArea;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Scanner;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Tab;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.stage.FileChooser;
import valadaplusplus.analises.AnaliseLexica;
import valadaplusplus.analises.AnaliseSemantica;
import valadaplusplus.analises.AnaliseSintatica;

public class TelaPrincipalController implements Initializable {

    private int cont = 1;
    private File arq = null;
    private AnaliseLexica lexica;
    private AnaliseSintatica sintatica;
    private AnaliseSemantica semantica;
    
    @FXML
    private JFXButton btLexica;
    @FXML
    private JFXButton btSintatica;
    @FXML
    private JFXButton btSemantica;
    @FXML
    private JFXButton btLimparIn;
    @FXML
    private JFXButton btLimparOut;
    @FXML
    private JFXTextArea taLinhas;
    @FXML
    private JFXTextArea taEditor;
    @FXML
    private JFXTabPane pnTab;
    @FXML
    private Tab tbLexica;
    @FXML
    private JFXTextArea taLexica;
    @FXML
    private Tab tbSintatica;
    @FXML
    private JFXTextArea taSintatica;
    @FXML
    private Tab tbSemantica;
    @FXML
    private JFXTextArea taSemantica;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        addNumLinha();
        editorKey();
        selecionaTab(1);
        taEditor.requestFocus();
    }   
    
    private void addNumLinha() {
        
        taLinhas.setText(taLinhas.getText() + " " + cont++ + "\n");
    }
    
    private void editorKey(){
        
        taEditor.setOnKeyPressed(k ->{
            
            final KeyCombination ENTER = new KeyCodeCombination(KeyCode.ENTER);
            final KeyCombination BACK_SPACE = new KeyCodeCombination(KeyCode.BACK_SPACE);
            if(ENTER.match(k))
                addNumLinha();
        });
    }
    
    public void selecionaTab(int op){
        
        SingleSelectionModel<Tab> selectionModel = pnTab.getSelectionModel();
        
        if(op == 1)
            selectionModel.select(tbLexica);
        else if(op == 2)
            selectionModel.select(tbSintatica);
        else if(op == 3)
            selectionModel.select(tbSemantica);
    } 

    @FXML
    private void clkBtAbrir(ActionEvent event) throws FileNotFoundException {
        
        FileChooser fc = new FileChooser();
        arq = fc.showOpenDialog(null);      
        Scanner scan = new Scanner(new FileReader(arq.toPath().toString()));
        
        while(scan.hasNextLine()) {
            
            String linha = scan.nextLine();
            taEditor.setText(taEditor.getText() + linha + "\n");
            addNumLinha();
        }
    }

    @FXML
    private void clkBtFechar(ActionEvent event) {
        
        if(arq != null) {
            
            taLinhas.setText("");
            taEditor.setText("");
            taLexica.setText("");
            taSintatica.setText("");
            taSemantica.setText("");
            arq = null;
            cont = 1;
            addNumLinha();
        }
    }

    @FXML
    private boolean clkBtLexica(ActionEvent event) {
        
        boolean flag = false;
        
        if(!taEditor.getText().equals("")) {
            
            lexica = new AnaliseLexica();
            lexica.analisar(taEditor.getText());
            taLexica.setText(lexica.getSaida());
            flag = true;
            selecionaTab(1);
            taSintatica.setText("");
            taSemantica.setText("");
        }
        if(!flag) {
            
            Alert a = new Alert(Alert.AlertType.INFORMATION);

            a.setTitle("Compilador");
            a.setHeaderText("Atenção!");
            a.setContentText("Editor Vazio!");
            a.showAndWait();
        }
        
        return flag;
    }

    @FXML
    private boolean clkBtSintatica(ActionEvent event) {
        
        boolean flag = false;
        
        if(clkBtLexica(null)) {
            
            if(lexica.isErro()) 
                taSintatica.setText(taSintatica.getText() + 
                        "Impossivel Realizar a Analize Sintática, Análise Léxica Retornou um Erro!");
            else {

                long tempoinicial = System.currentTimeMillis();
                
                sintatica = new AnaliseSintatica(lexica.getTokens());
                sintatica.analisar();
                taSintatica.setText(sintatica.getSaida() + "\nTempo Decorrido = " 
                        + (System.currentTimeMillis() - tempoinicial) + " mS");
                flag = true;
            }
            selecionaTab(2);
            taSemantica.setText("");
        }
            
        return flag;
    }

    @FXML
    private boolean clkBtSemantica(ActionEvent event) {
        
        boolean flag = false;
        
        if(clkBtSintatica(null)) {
            
            if(sintatica.isErro())
                taSemantica.setText(taSemantica.getText() + 
                        "Impossivel Realizar a Analize Semântica, Análise Sintática Retornou um Erro!");
            else {

                semantica = new AnaliseSemantica(lexica.getTokens());
                semantica.analisar();
                taSemantica.setText(semantica.getSaida());
                flag = true;
            }
            selecionaTab(3);
        }
        
        return flag;
    }


    @FXML
    private void clkBtLimparIn(ActionEvent event) {
        
        taEditor.setText("");
    }

    @FXML
    private void clkBtLimparOut(ActionEvent event) {
        
        taLexica.setText("");
        taSintatica.setText("");
        taSemantica.setText("");
    }
}