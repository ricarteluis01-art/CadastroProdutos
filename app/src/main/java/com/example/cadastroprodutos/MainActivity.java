package com.example.cadastroprodutos;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EditText editNome;
    private EditText editPreco;
    private Button btnSalvar;
    private ListView listaProdutos;

    private ProdutoDbHelper dbHelper;
    private ArrayAdapter<String> adapter;
    private List<String> produtos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editNome = findViewById(R.id.editNome);
        editPreco = findViewById(R.id.editPreco);
        btnSalvar = findViewById(R.id.btnSalvar);
        listaProdutos = findViewById(R.id.listaProdutos);

        dbHelper = new ProdutoDbHelper(this);

        atualizarLista();

        btnSalvar.setOnClickListener(v -> salvarProduto());
    }

    private void salvarProduto() {

        String nome = editNome.getText().toString().trim();
        String precoStr = editPreco.getText().toString().trim();

        // Validação do nome
        if (nome.length() < 3) {
            Toast.makeText(
                    this,
                    "Nome inválido. Mínimo de 3 caracteres.",
                    Toast.LENGTH_SHORT
            ).show();
            return;
        }

        double preco;

        // Validação do preço
        try {
            preco = Double.parseDouble(precoStr);

            if (preco <= 0) {
                Toast.makeText(
                        this,
                        "Preço inválido. Informe um valor maior que zero.",
                        Toast.LENGTH_SHORT
                ).show();
                return;
            }

        } catch (NumberFormatException e) {
            Toast.makeText(
                    this,
                    "Preço inválido. Digite um número.",
                    Toast.LENGTH_SHORT
            ).show();
            return;
        }

        // Inserção no banco
        boolean sucesso = dbHelper.inserirProduto(nome, preco);

        if (sucesso) {

            Toast.makeText(
                    this,
                    "Produto salvo com sucesso!",
                    Toast.LENGTH_SHORT
            ).show();

            editNome.setText("");
            editPreco.setText("");

            atualizarLista();

        } else {

            Toast.makeText(
                    this,
                    "Erro ao salvar produto.",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }

    private void atualizarLista() {

        produtos = dbHelper.listarProdutos();

        adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                produtos
        );

        listaProdutos.setAdapter(adapter);
    }
}