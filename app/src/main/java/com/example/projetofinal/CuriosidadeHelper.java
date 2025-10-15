package com.example.projetofinal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CuriosidadeHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "curiosidades.db";
    private static final int DATABASE_VERSION = 1;

    private Context context;

    // Tabela de curiosidades
    private static final String TABLE_CURIOSIDADES = "curiosidades";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_TEXTO = "texto";
    private static final String COLUMN_ATIVA = "ativa";

    // Tabela de configuração de rotação
    private static final String TABLE_CONFIG = "config_rotacao";
    private static final String COLUMN_ULTIMA_CURIOSIDADE_ID = "ultima_curiosidade_id";

    public CuriosidadeHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Criar tabela de curiosidades
        String createCuriosidadesTable = "CREATE TABLE " + TABLE_CURIOSIDADES + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_TEXTO + " TEXT NOT NULL," +
                COLUMN_ATIVA + " INTEGER DEFAULT 1" +
                ")";
        db.execSQL(createCuriosidadesTable);

        // Criar tabela de configuração
        String createConfigTable = "CREATE TABLE " + TABLE_CONFIG + "(" +
                "id INTEGER PRIMARY KEY DEFAULT 1," +
                COLUMN_ULTIMA_CURIOSIDADE_ID + " INTEGER" +
                ")";
        db.execSQL(createConfigTable);

        // Inserir registro inicial na configuração
        ContentValues configValues = new ContentValues();
        configValues.put("id", 1);
        configValues.put(COLUMN_ULTIMA_CURIOSIDADE_ID, 0);
        db.insert(TABLE_CONFIG, null, configValues);

        // Inserir curiosidades iniciais
        inserirCuriosidadesIniciais(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CURIOSIDADES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONFIG);
        onCreate(db);
    }

    private void inserirCuriosidadesIniciais(SQLiteDatabase db) {
        String[] curiosidades = {
                "A água é conhecida como o \"solvente universal\" porque dissolve mais substâncias que qualquer outro líquido.",
                "O elemento mais reativo da tabela periódica é o frâncio, um metal alcalino raro e radioativo.",
                "O pH do suco gástrico no estômago é entre 1.5 e 3.5, altamente ácido para digerir alimentos.",
                "O ferro muda de sólido para líquido a 1.538°C, mas o tungstênio só derrete a 3.422°C.",
                "O carbono pode formar mais compostos que todos os outros elementos juntos, devido à sua tetravalência.",
                "A prata é o melhor condutor de eletricidade entre todos os metais, seguida pelo cobre e ouro.",
                "O gás hélio é tão leve que escapa da atmosfera terrestre e não pode ser recapturado.",
                "A reação entre sódio e água produz hidróxido de sódio, hidrogênio gasoso e libera calor.",
                "O diamante e a grafite são feitos do mesmo elemento (carbono), mas com estruturas cristalinas diferentes.",
                "O elemento mercúrio é líquido em temperatura ambiente e foi usado em termômetros por séculos."
        };

        for (String texto : curiosidades) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_TEXTO, texto);
            values.put(COLUMN_ATIVA, 1);
            db.insert(TABLE_CURIOSIDADES, null, values);
        }
    }

    // Método para obter uma curiosidade aleatória que não seja a última exibida
    public String obterProximaCuriosidade() {
        SQLiteDatabase db = this.getReadableDatabase();

        // Obter a última curiosidade exibida
        int ultimaId = obterUltimaCuriosidadeId();

        // Buscar todas as curiosidades ativas, excluindo a última
        String query = "SELECT * FROM " + TABLE_CURIOSIDADES +
                " WHERE " + COLUMN_ATIVA + " = 1 AND " + COLUMN_ID + " != ?";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(ultimaId)});

        List<Integer> idsDisponiveis = new ArrayList<>();
        List<String> textosDisponiveis = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
                String texto = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TEXTO));
                idsDisponiveis.add(id);
                textosDisponiveis.add(texto);
            } while (cursor.moveToNext());
        }
        cursor.close();

        // Se não há curiosidades disponíveis (exceto a última), buscar todas as ativas
        if (idsDisponiveis.isEmpty()) {
            cursor = db.query(TABLE_CURIOSIDADES,
                    new String[]{COLUMN_ID, COLUMN_TEXTO},
                    COLUMN_ATIVA + " = 1",
                    null, null, null, null);

            if (cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
                    String texto = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TEXTO));
                    idsDisponiveis.add(id);
                    textosDisponiveis.add(texto);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }

        // Se ainda não há curiosidades, retornar mensagem padrão
        if (textosDisponiveis.isEmpty()) {
            return "“Você sabia? O ouro é o único metal que não oxida facilmente no ar.”";
        }

        // Selecionar uma curiosidade aleatória
        Random random = new Random();
        int index = random.nextInt(textosDisponiveis.size());
        String curiosidadeSelecionada = textosDisponiveis.get(index);
        int idSelecionado = idsDisponiveis.get(index);

        // Atualizar a última curiosidade exibida
        atualizarUltimaCuriosidade(idSelecionado);

        return "“" + curiosidadeSelecionada + "”";
    }

    private int obterUltimaCuriosidadeId() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_CONFIG,
                new String[]{COLUMN_ULTIMA_CURIOSIDADE_ID},
                "id = 1", null, null, null, null);

        int ultimaId = 0;
        if (cursor.moveToFirst()) {
            ultimaId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ULTIMA_CURIOSIDADE_ID));
        }
        cursor.close();
        return ultimaId;
    }

    private void atualizarUltimaCuriosidade(int novaId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ULTIMA_CURIOSIDADE_ID, novaId);
        db.update(TABLE_CONFIG, values, "id = 1", null);
    }

    // Método para adicionar nova curiosidade (opcional)
    public void adicionarCuriosidade(String texto) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TEXTO, texto);
        values.put(COLUMN_ATIVA, 1);
        db.insert(TABLE_CURIOSIDADES, null, values);
    }

    // Método para desativar uma curiosidade (opcional)
    public void desativarCuriosidade(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ATIVA, 0);
        db.update(TABLE_CURIOSIDADES, values, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
    }
}