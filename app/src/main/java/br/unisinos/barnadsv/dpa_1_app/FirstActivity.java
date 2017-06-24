package br.unisinos.barnadsv.dpa_1_app;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.Toast;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.app.DatePickerDialog;
import android.app.Dialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Pattern;

public class FirstActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private EditText edtNomeCompleto;
    private EditText edtDataNascimento;
    private Spinner spSexo;
    private AutoCompleteTextView actvProfissao;
    private Spinner spEstadoCivil;
    private EditText edtSenha;
    private EditText edtConfirmaSenha;
    private static final String[] PROFISSOES = {"Administrador de empresas", "Advogado", "Aeromoça/ Comissário(a) de Bordo", "Agrônomo", "Arquiteto", "Contador", "Corretor de Seguros", "Eletricista", "Enfermeiro – auxiliar", "Enfermeiro – técnico", "Enfermeiro", "Engenheiro", "Farmacêutico", "Fisioterapeuta", "Fonoaudiólogo", "Garçom", "Jornalista", "Químico", "Mestre de Obras", "Motoboy – mensageiro motociclista", "Nutricionista", "Piloto de Avião (comandante)", "Professor da rede básica de educação pública", "Professor universitário federal", "Psicólogo", "Representante comercial", "Secretária de nível técnico", "Secretária de nível superior", "Técnico em Radiologia", "Trabalhador doméstico", "Veterinário"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        this.edtNomeCompleto = (EditText) findViewById(R.id.edtNomeCompleto);
        this.edtDataNascimento = (EditText) findViewById(R.id.edtDataNascimento);
        this.spSexo = (Spinner) findViewById(R.id.spSexo);
        this.actvProfissao = (AutoCompleteTextView) findViewById(R.id.actvProfissao);
        this.spEstadoCivil = (Spinner) findViewById(R.id.spEstadoCivil);
        this.edtSenha = (EditText) findViewById(R.id.edtSenha);
        this.edtConfirmaSenha = (EditText) findViewById(R.id.edtConfirmaSenha);

        /*
         * Quando o controle edtNomeCompleto tem o texto mudado, faz a validação.
         */
        this.edtNomeCompleto.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validarNome(String.valueOf(s));
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        /*
         * Quando o controle edtDataNascimento recebe o foco, abre o DatePicker.
         */
        this.edtDataNascimento.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus) {
                setDate(FirstActivity.this.edtDataNascimento.getText().toString().trim());
            }
            }
        });

        /*
         * Quando o controle edtDataNascimento recebe um click, abre o DatePicker.
         */
        this.edtDataNascimento.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            setDate(FirstActivity.this.edtDataNascimento.getText().toString().trim());
            }
        });


        /*
         * Quando o controle actvProfissao tem o texto mudado, faz a validação.
         */
        this.actvProfissao.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validarProfissao(String.valueOf(s));
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        /*
         * Quando o botão btConfirmar é clicado, os dados são validados e enviados para a SecondActivity.
         */
        Button btConfirmar = (Button) findViewById(R.id.btConfirmar);
        btConfirmar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            if (validarDados()) {
                Intent intent = new Intent(getApplicationContext(), SecondActivty.class);
                intent.putExtra("nome_completo", FirstActivity.this.edtNomeCompleto.getText().toString());
                intent.putExtra("data_nascimento", FirstActivity.this.edtDataNascimento.getText().toString());
                intent.putExtra("sexo", FirstActivity.this.spSexo.getSelectedItem().toString());
                intent.putExtra("profissao", FirstActivity.this.actvProfissao.getText().toString());
                intent.putExtra("estado_civil", FirstActivity.this.spEstadoCivil.getSelectedItem().toString());
                startActivity(intent);
            }
            }
        });

        /*
         * Popula o spinner spSexo com sexo_array.
         */
        ArrayAdapter<CharSequence> sexoAdapter = ArrayAdapter.createFromResource(this, R.array.sexo_array, android.R.layout.simple_spinner_item);
        sexoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.spSexo.setAdapter(sexoAdapter);

        /*
         * Popula o autoCompleteTextView actvProfissao com o array PROFISSOES.
         */
        ArrayAdapter<String> profissaoAdapter = new ArrayAdapter<>(this, android.R.layout.select_dialog_item, PROFISSOES);
        this.actvProfissao.setThreshold(1);//will start working from first character
        this.actvProfissao.setAdapter(profissaoAdapter);//setting the adapter data into the AutoCompleteTextView

        /*
         * Popula o spinner spEstadoCivil com estado_civil_array.
         */
        ArrayAdapter<CharSequence> estadoCivilAdapter = ArrayAdapter.createFromResource(this, R.array.estado_civil_array, android.R.layout.simple_spinner_item);
        estadoCivilAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.spEstadoCivil.setAdapter(estadoCivilAdapter);

    }

    /*
     * DatePickerFragment: utilizado para mostrar um DatePicker
     */

    public static class DatePickerFragment extends DialogFragment {
        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            int dia, mes, ano;
            Bundle args = getArguments();
            String dataNascimento = args.getString("dataNascimento");
            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            df.setLenient(false);
            Date data;
            Calendar c = Calendar.getInstance();
            try {
                data = df.parse(dataNascimento);
                c.setTime(data);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            ano = c.get(Calendar.YEAR);
            mes = c.get(Calendar.MONTH);
            dia = c.get(Calendar.DAY_OF_MONTH);
            // Cria uma nova instância de DatePickerDialog e a retorna
            return new DatePickerDialog(getActivity(), (FirstActivity)getActivity(), ano, mes, dia);
        }
    }

    /**
     * Método que é chamado quando uma data é selecionada em DatePickerFragment.
     * Sobrescreve o método onDateSet original da interface DatePickerDialog.OnDateSetListener.
     *
     * @param view O próprio DatePicker
     * @param ano Ano da data selecionada
     * @param mes Mês da data selecionada
     * @param dia Dia do mês da data selecionada
     */
    @Override
    public void onDateSet(DatePicker view, int ano, int mes, int dia) {
        mostraData(("0000" + String.valueOf(ano)).substring(String.valueOf(ano).length()), ("00" + String.valueOf(mes+1)).substring(String.valueOf(mes+1).length()), ("00" + String.valueOf(dia)).substring(String.valueOf(dia).length()));
    }

    /**
     * Cria um DatePickerFragment.
     *
     * @param dataNascimento Data de nascimento enviada pelo controle edtDataNascimento.
     */
    private void setDate(String dataNascimento) {
        DatePickerFragment picker = new DatePickerFragment();
        Bundle args = new Bundle();
        args.putString("dataNascimento", dataNascimento);
        picker.setArguments(args);
        picker.show(getSupportFragmentManager(), "datePicker");
    }

    /**
     * Passa o valor do DatePicker para o controle edtDataNascimento.
     *
     * @param ano Ano da data selecionada no DatePicker
     * @param mes Mês da data selecionada no DatePicker
     * @param dia Dia da data selecionada no DatePicker
     */
    private void mostraData(String ano, String mes, String dia) {
        String dataNascimento = dia + "/" + mes + "/" + ano;
        this.edtDataNascimento.setText(dataNascimento);
        validarData(dataNascimento);
    }

    /* Validação */

    /**
     * Valida o nome, que pode ter qualquer caracter sem e com acentuação.
     * O nome deve ter no mínimo 5 caracteres e no máximo 60 caracteres.
     *
     * @param nomeCompleto Nome a ser validade
     * @return Retorna um valor booleano.
     */
    private boolean validarNome(String nomeCompleto){
        Pattern padrao = Pattern.compile("^[A-Za-záàâãéèêíïóôõöúçñÁÀÂÃÉÈÍÏÓÔÕÖÚÇÑ ]+$");
        if (nomeCompleto.equals("")) {
            this.edtNomeCompleto.setError("Nome inválido\nNulo");
            return false;
        } else if (!padrao.matcher(nomeCompleto).matches()) {
            this.edtNomeCompleto.setError("Nome inválido\nCaracter inválido");
            return false;
        } else if (nomeCompleto.length() > 60) {
            this.edtNomeCompleto.setError("Nome inválido\nMais de 60 caracteres");
            return false;
        } else if (nomeCompleto.length() < 5) {
            this.edtNomeCompleto.setError("Nome inválido\nMenos de 5 caracteres");
            return false;
        } else {
            this.edtNomeCompleto.setError(null);
        }
        return true;
    }

    /**
     * Valida a data de acordo com o formato 00/00/0000
     *
     * @param dataNascimento Data de nascimento enviada para validação.
     * @return Retorna um valor booleano.
     */
    private boolean validarData(String dataNascimento){
        //Pattern padrao = Pattern.compile("^(?:(?:31(\\/|-|\\.)(?:0?[13578]|1[02]))\\1|(?:(?:29|30)(\\/|-|\\.)(?:0?[1,3-9]|1[0-2])\\2))(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$|^(?:29(\\/|-|\\.)0?2\\3(?:(?:(?:1[6-9]|[2-9]\\d)?(?:0[48]|[2468][048]|[13579][26])|(?:(?:16|[2468][048]|[3579][26])00))))$|^(?:0?[1-9]|1\\d|2[0-8])(\\/|-|\\.)(?:(?:0?[1-9])|(?:1[0-2]))\\4(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$");
        Pattern padrao = Pattern.compile("^(?:(?:31(/|-|\\.)(?:0?[13578]|1[02]))\\1|(?:(?:29|30)(/|-|\\.)(?:0?[1,3-9]|1[0-2])\\2))(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$|^(?:29(/|-|\\.)0?2\\3)$|^(?:0?[1-9]|1\\d|2[0-8])(/|-|\\.)(?:(?:0?[1-9])|(?:1[0-2]))\\4(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$");
        if (dataNascimento.equals("")) {
            this.edtDataNascimento.setError("Data inválida\nNula");
            return false;
        } else if (!padrao.matcher(dataNascimento).matches()) {
            this.edtDataNascimento.setError("Data inválida\nCaracter inválido");
            return false;
        } else {
            this.edtDataNascimento.setError(null);
        }
        return true;
    }

    /**
     * Valida sexo, verificando apenas se o valor não é nulo.
     *
     * @param sexo Masculino ou Feminino
     * @return Retorna um valor booleano.
     */
    private boolean validarSexo(String sexo) {
        return !sexo.equals("");
    }

    private boolean validarProfissao(String profissao){
//        Pattern padrao = Pattern.compile("^[A-Za-záàâãéèêíïóôõöúçñÁÀÂÃÉÈÍÏÓÔÕÖÚÇÑ\\\\/\\\\(\\\\) ]+$");
//        if (!padrao.matcher(profissao).matches()) {
//            this.actvProfissao.setError("Profissão inválida\nCaracter inválido");
//            return false;
//        } else if (profissao.length() > 60) {
        if (profissao.equals("")) {
            this.actvProfissao.setError("Profissão inválida\nNula");
            return false;
        } else if (profissao.length() > 60) {
            this.actvProfissao.setError("Profissão inválida\nMais de 60 caracteres");
            return false;
        } else if (profissao.length() < 5) {
            this.actvProfissao.setError("Profissão inválida\nMenos de 5 caracteres");
            return false;
        } else {
            this.actvProfissao.setError(null);
        }
        return true;
    }


    /**
     * Valida o estado civil, verificando se o valor não é nulo.
     *
     * @param estadoCivil Pode ser Solteiro, Casado, Divorciado, Viúvo ou Separado
     * @return Retorna um valor booleano.
     */
    private boolean validarEstadoCivil(String estadoCivil) {
        return !estadoCivil.equals("");
    }

    /**
     * Valida as senhas, verificando se uma delas é nula ou se são diferentes entre si.
     *
     * @param senha Senha inserida pelo usuário
     * @param confirmacaoSenha Confirmação da senha inserida pelo usuário
     * @return Retorna um valor booleano
     */
    private boolean validarSenhas(String senha, String confirmacaoSenha) {
        if (senha.compareTo("") == 0) {
            this.edtSenha.setError("Senha em branco");
            return false;
        } else if (confirmacaoSenha.compareTo("") == 0) {
            this.edtConfirmaSenha.setError("Confirmação da senha em branco");
            return false;
        } else if (senha.compareTo(confirmacaoSenha) != 0) {
            this.edtSenha.setError("Senhas não conferem");
            this.edtConfirmaSenha.setError("Senhas não conferem");
            return false;
        } else {
            this.edtSenha.setError(null);
            this.edtConfirmaSenha.setError(null);
        }
        return true;
    }

    /**
     * Chama a validação de cada campo do formulário e realiza a validação total
     *
     * @return Retorna um valor booleano.
     */
    private boolean validarDados() {

        String nomeCompleto = this.edtNomeCompleto.getText().toString().trim();
        String dataNascimento = this.edtDataNascimento.getText().toString().trim();
        String sexo = this.spSexo.getSelectedItem().toString().trim();
        String profissao = this.actvProfissao.getText().toString().trim();
        String estadoCivil = this.spEstadoCivil.getSelectedItem().toString().trim();
        String senha = this.edtSenha.getText().toString();
        String confirmaSenha = this.edtConfirmaSenha.getText().toString();

        boolean a = validarNome(nomeCompleto);
        boolean b = validarData(dataNascimento);
        boolean c = validarSexo(sexo);
        boolean d = validarProfissao(profissao);
        boolean e = validarEstadoCivil(estadoCivil);
        boolean f = validarSenhas(senha, confirmaSenha);

        if (a && b && c && d && e && f) {
            Toast.makeText(this, "Dados Armazenados", Toast.LENGTH_LONG).show();
            return true;
        } else {
            return false;
        }

    }
}
