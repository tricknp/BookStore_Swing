package view;

import entidades.Autor;
import entidades.Livro;
import java.time.LocalDate;
import java.util.ArrayList;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import util.util;
import static util.ShowMessages.showInputString;
import static util.ShowMessages.showMessage;
import static util.ShowMessages.showQuestion;
import static util.util.getInt;
import util.LivrariaUtil;

public class JFlivraria extends javax.swing.JFrame {

    private ArrayList<Livro> livrosList = new ArrayList<>();
    private ArrayList<Autor> autoresList = new ArrayList<>();

    private Object[] colunas = {"Cód.", "Título", "ISBN", "Autor(es)", "Num. Capítulos"};
    private DefaultTableModel modeloTabela = new DefaultTableModel(colunas, 0) {
        // desabilitar edição da tabela    
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    private DefaultListModel modeloListaAutor = new DefaultListModel();
    private DefaultListModel modeloListaAutorSelecionado = new DefaultListModel();

    private int livrosCount;
    private int autoresCount;

    public JFlivraria() {
        initComponents();
        this.jTable_Tabela.setModel(modeloTabela);

        this.jList_AutoresCadastradosList.setModel(modeloListaAutor);
        modeloListaAutorSelecionado.addElement("*Nenhum autor selecionado");
        this.jList_LivroDoAutorSelecionado.setModel(modeloListaAutorSelecionado);

        this.jList_AutoresCadastradosList.addListSelectionListener(new ListSelectionListener() {
            @Override // Metodo que executa quando selecionado novo item na lista de autores
            public void valueChanged(ListSelectionEvent e) {
                int selectedAutor = jList_AutoresCadastradosList.getSelectedIndex();
                updateAutorLivrosJList(selectedAutor);
            }
        });
        this.jList_LivroDoAutorSelecionado.addListSelectionListener(new ListSelectionListener() {
            @Override // Metodo que executa quando selecionado novo item na lista de livros do autor x
            public void valueChanged(ListSelectionEvent e) {
                int x = 0;
                try {
                    x = jList_LivroDoAutorSelecionado.getSelectedIndex();
                } catch (Exception f) {
                }
                try {
                    int y = jList_AutoresCadastradosList.getSelectedIndex();
                    jTextArea_DescricaoAbaAutores.setText(autoresList.get(y).getLivrosDoAutor().get(x).getDescricao());
                } catch (Exception f) {
                }

            }
        });
    }

    private int cadastrarLivro() {
        if (!jTextField_TituloLivro.getText().isEmpty() && jTextField_TituloLivro.getText().length() < 30) {
            int numeroDeAutores = getNumAutoresFromJtext();
            if (numeroDeAutores > 0) {
                int numeroDeCapitulos = getNumCapitulosFromJtext();
                if (numeroDeCapitulos > 0) {
                    try {
                        String titulo = getTituloFromJtext();
                        String ISBN = getISBNFromJtext();
                        if (ISBN.length() != 13) {
                            showMessage("ISBN inválido.");
                            return -1;
                        }
                        livrosCount++;
                        Livro livro = new Livro(titulo, ISBN, livrosCount);
                        String descricao = getDescricaoFromJtext();
                        livro.setDescricao(descricao);
                        int escolha1 = showQuestion("Cadastrar Autor(es) agora?");
                        String nomeTemporario = "nome_autor";
                        JDAutoresDialog listaAutorDial = new JDAutoresDialog(this, true, autoresList, livro, numeroDeAutores, autoresCount);
                        if (escolha1 == JOptionPane.NO_OPTION) {
                            for (int i = 0; i < numeroDeAutores; i++) {
                                LocalDate ldate = LocalDate.of(1900, 01, 01);
                                Autor autor = new Autor(nomeTemporario + autoresCount, ldate);
                                livro.adicionarAutor(autor);
                                this.autoresList.add(autor);
                                autoresCount++;
                                autor.setCod(autoresCount);
                                autor.getLivrosDoAutor().add(livro);
                            }
                        } else if (escolha1 == JOptionPane.YES_OPTION) {
                            listaAutorDial.setVisible(true);

                            for (int i = 0; i < listaAutorDial.getAutoresSelecionados().size(); i++) {
                                if (!livro.getAutorList().contains(listaAutorDial.getAutoresSelecionados().get(i))) {
                                    livro.getAutorList().add(listaAutorDial.getAutoresSelecionados().get(i));
                                    //listaAutorDial.getAutoresSelecionados().get(i).getLivrosDoAutor().add(livro);
                                }
                            }

                            autoresCount += listaAutorDial.getAutoresCount();
                        }

                        int escolha2 = showQuestion("Cadastrar Capitulos agora?");
                        nomeTemporario = "título_capítulo";
                        String textoTemporario = "texto";
                        if (escolha2 == JOptionPane.NO_OPTION) {
                            for (int i = 0; i < numeroDeCapitulos; i++) {
                                livro.adicionarCapitulo(nomeTemporario, textoTemporario);
                            }
                        } else if (escolha2 == JOptionPane.YES_OPTION) {
                            for (int i = 0; i < numeroDeCapitulos; i++) {
                                while (true) {
                                    try {
                                        nomeTemporario = showInputString("Título do Capitulo:" + (i + 1) + "/" + numeroDeCapitulos);
                                        break;
                                    } catch (Exception e) {
                                        showMessage("Valor inválido");
                                    }
                                }
                                while (true) {
                                    try {
                                        textoTemporario = showInputString("Texto do Capítulo:" + (i + 1) + "/" + numeroDeCapitulos);
                                        break;
                                    } catch (Exception e) {
                                        showMessage("Valor inválido");
                                    }
                                }
                                livro.adicionarCapitulo(nomeTemporario, textoTemporario);
                            }
                        }
                        for (int i = 0; i < listaAutorDial.getAutoresSelecionados().size(); i++) {
                            listaAutorDial.getAutoresSelecionados().get(i).getLivrosDoAutor().add(livro);
                        }
                        this.livrosList.add(livro);
                        showMessage("Livro cadastrado com sucesso!");
                        limparCampos();
                        updateTabela();
                        updateAutorJList();
                        return this.livrosList.lastIndexOf(livro);
                    } catch (Exception e) {
                        showMessage("Erro ao cadastrar livro. " + e);
                        return -1;
                    }
                } else {
                    showMessage("Precisa de pelo menos 1 Capitulo.");
                    return -1;
                }
            } else {
                showMessage("Precisa de pelo menos 1 Autor.");
                return -1;
            }
        } else {
            showMessage("Título inválido ou não preenchido.");
            return -1;
        }
    }

    private void abrirLivroTabela() {
        try {
            int x = jTable_Tabela.getSelectedRow();
            if (x == -1) {
                showMessage("Nenhum livro selecionado.");
            } else {
                for (int i = 0; i < livrosList.size(); i++) {
                    if (jTable_Tabela.getValueAt(x, 0).equals(livrosList.get(i).getCod())) {
                        Livro livro = livrosList.get(x);
                        new JFleitor(livro, this).setVisible(true);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private void abrirLivroAutorList() {
        try {
            Autor autor = autoresList.get(jList_AutoresCadastradosList.getSelectedIndex());
            Livro livro = autor.getLivrosDoAutor().get(jList_LivroDoAutorSelecionado.getSelectedIndex());
            new JFleitor(livro, this).setVisible(true);
        } catch (Exception e) {
            showMessage("Nenhum autor/livro selecionado.");
        }
    }

    private void limparCampos() {
        this.jTextField_TituloLivro.setText("");
        this.jTextField_ISBN.setText("");
        this.jTextField_NumeroDeAutores.setText("0");
        this.jTextField_NumeroDeCapitulos.setText("0");
        this.jTextArea_DescricaoLivro.setText("@ Sem Descrição.");
    }

    private void updateTabela() {
        this.modeloTabela.setRowCount(0);
        Object[] row = new Object[5];
        for (int i = 0; i < livrosList.size(); i++) {
            row[0] = livrosList.get(i).getCod();
            row[1] = livrosList.get(i).getTitulo();
            row[2] = livrosList.get(i).getISBN();
            row[3] = livrosList.get(i).toString();
            row[4] = livrosList.get(i).getCapitulosList().size();
            this.modeloTabela.addRow(row);

        }
    }

    private void addItemTabela(int i) {
        Object[] row = new Object[5];
        row[0] = livrosList.get(i).getCod();
        row[1] = livrosList.get(i).getTitulo();
        row[2] = livrosList.get(i).getISBN();
        row[3] = livrosList.get(i).toString();
        row[4] = livrosList.get(i).getCapitulosList().size();
        this.modeloTabela.addRow(row);
    }

    public void updateAutorJList() {
        this.modeloListaAutor.removeAllElements();
        for (int i = 0; i < autoresList.size(); i++) {
            try {
                Autor autor = autoresList.get(i);
                this.modeloListaAutor.addElement(autor.getCod() + " " + autor.getNome());
            } catch (Exception e) {
                System.out.println("erro updateAutorJList");
            }
            if (i == autoresList.size()) {
                updateAutorLivrosJList(i);
            }
        }
    }

    private void updateAutorLivrosJList(int selectedAutor) {
        this.modeloListaAutorSelecionado.removeAllElements();
        if (selectedAutor >= 0) {
            try {
                Autor autor = autoresList.get(selectedAutor);
                for (int i = 0; i < autor.getLivrosDoAutor().size(); i++) {
                    modeloListaAutorSelecionado.addElement(autor.getLivrosDoAutor().get(i).getTitulo());
                }
                //jList_LivroDoAutorSelecionado.setSelectedIndex(0);
                jTextArea_DescricaoAbaAutores.setText(autor.getLivrosDoAutor().get(jList_LivroDoAutorSelecionado.getSelectedIndex()).getDescricao());
            } catch (Exception e) {
            }
        }
    }

    public ArrayList<Autor> getAutoresList() {
        return autoresList;
    }

    private void removerDaTabelaLista() {
        int x = jTable_Tabela.getSelectedRow();
        if (x == -1) {
            showMessage("Nenhum livro selecionado.");
        } else {
            int opcao = showQuestion("Remover " + livrosList.get(x).getTitulo() + "?");
            if (opcao == JOptionPane.YES_OPTION) {
                for (int i = 0; i < livrosList.size(); i++) {
                    if (jTable_Tabela.getValueAt(x, 0).equals(livrosList.get(i).getCod())) {
                        for (int j = 0; j < autoresList.size(); j++) {
                            for (int k = 0; k < autoresList.get(j).getLivrosDoAutor().size(); k++) {
                                Livro livro = autoresList.get(j).getLivrosDoAutor().get(k);
                                if (autoresList.get(j).getLivrosDoAutor().get(k).equals(livrosList.get(x))) {
                                    autoresList.get(j).getLivrosDoAutor().remove(k);
                                }
                            }
                        }
                        livrosList.remove(x);
                        modeloTabela.removeRow(x);
                    }
                }
                updateAutorJList();
            }
        }
    }

    private void pesquisarTitulo() {
        try {
            String txt = showInputString("Pesquisa por títulos:");
            modeloTabela.setRowCount(0);
            for (int i = 0; i < livrosList.size(); i++) {
                try {
                    if (livrosList.get(i).getTitulo().contains(txt)) {
                        addItemTabela(i);
                    }
                } catch (Exception e) {
                    updateTabela();
                }
            }
        } catch (Exception e) {
        }
    }

    private void pesquisarAutor() {
        try {
            String txt = showInputString("Pesquisa por autores:");
            modeloTabela.setRowCount(0);
            for (int i = 0; i < livrosList.size(); i++) {
                for (int j = 0; j < livrosList.get(i).getAutorList().size(); j++) {
                    try {
                        if (livrosList.get(i).getAutorList().get(j).getNome().contains(txt)) {
                            addItemTabela(i);
                        }
                    } catch (Exception e) {
                        updateTabela();
                    }
                }
            }
        } catch (Exception e) {
        }
    }

    private void pesquisarISBN() {
        try {
            String txt = showInputString("Pesquisa por ISBN:");
            modeloTabela.setRowCount(0);
            for (int i = 0; i < livrosList.size(); i++) {
                try {
                    if (livrosList.get(i).getISBN().contains(txt)) {
                        addItemTabela(i);
                    }
                } catch (Exception e) {
                    updateTabela();
                }
            }
        } catch (Exception e) {
        }
    }

    private void autorInfoFromList() {
        try {
            String nome = autoresList.get(jList_AutoresCadastradosList.getSelectedIndex()).getNome();
            String dataNasc = util.convertLocalDateUStoBR(autoresList.get(jList_AutoresCadastradosList.getSelectedIndex()).getDataNasc());
            showMessage("Nome: " + nome + "    Data de Nascimento: " + dataNasc);
        } catch (Exception e) {
        }
    }

    private void plusAutores() {
        int n = Integer.parseInt(this.jTextField_NumeroDeAutores.getText());
        if (n < 6) {
            this.jTextField_NumeroDeAutores.setText(Integer.toString(n + 1));
        }
    }

    private void lessAutores() {
        int n = Integer.parseInt(this.jTextField_NumeroDeAutores.getText());
        if (n > 0) {
            this.jTextField_NumeroDeAutores.setText(Integer.toString(n - 1));
        }
    }

    private void lessCapitulos() {
        int n = Integer.parseInt(this.jTextField_NumeroDeCapitulos.getText());
        if (n > 0) {
            this.jTextField_NumeroDeCapitulos.setText(Integer.toString(n - 1));
        }
    }

    private void plusCapitulos() {
        int n = Integer.parseInt(this.jTextField_NumeroDeCapitulos.getText());
        if (n < 100) {
            this.jTextField_NumeroDeCapitulos.setText(Integer.toString(n + 1));
        }
    }

    private void plusPlusCapitulos() {
        int n = Integer.parseInt(this.jTextField_NumeroDeCapitulos.getText());
        if (n + 10 > 100) {
            this.jTextField_NumeroDeCapitulos.setText(Integer.toString(100));
        } else {
            this.jTextField_NumeroDeCapitulos.setText(Integer.toString(n + 10));
        }
    }

    private void limparLivraria() {
        int opcao = showQuestion("Apagar tudo?");
        if (opcao == JOptionPane.YES_OPTION) {
            this.livrosList.clear();
            this.livrosCount = 0;
            this.autoresList.clear();
            this.autoresCount = 0;
            updateTabela();
            updateAutorJList();
            limparCampos();
        }
    }

    private int getNumAutoresFromJtext() {
        return getInt(jTextField_NumeroDeAutores.getText());
    }

    private int getNumCapitulosFromJtext() {
        return getInt(jTextField_NumeroDeCapitulos.getText());
    }

    private String getTituloFromJtext() {
        return jTextField_TituloLivro.getText();
    }

    private String getISBNFromJtext() {
        return jTextField_ISBN.getText().toUpperCase();
    }

    private String getDescricaoFromJtext() {
        return jTextArea_DescricaoLivro.getText();
    }

    public int getAutoresCount() {
        return autoresCount;
    }

    public void setAutoresCount(int autoresCount) {
        this.autoresCount = autoresCount;
    }

//============================================================================    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jTabbedPane_PainelComAbas = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable_Tabela = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton10 = new javax.swing.JButton();
        jSeparator6 = new javax.swing.JSeparator();
        jSeparator7 = new javax.swing.JSeparator();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jList_AutoresCadastradosList = new javax.swing.JList<>();
        jLabel9 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jList_LivroDoAutorSelecionado = new javax.swing.JList<>();
        jLabel10 = new javax.swing.JLabel();
        jSeparator4 = new javax.swing.JSeparator();
        jSeparator5 = new javax.swing.JSeparator();
        jScrollPane5 = new javax.swing.JScrollPane();
        jTextArea_DescricaoAbaAutores = new javax.swing.JTextArea();
        jLabel11 = new javax.swing.JLabel();
        jButton_AbrirLivroAutoSelecionado = new javax.swing.JButton();
        jSeparator9 = new javax.swing.JSeparator();
        jButton_AbrirLivroAutoSelecionado1 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jTextField_TituloLivro = new javax.swing.JTextField();
        jButton_AutorMais = new javax.swing.JButton();
        jButton_CapituloMais = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jTextField_ISBN = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jTextField_NumeroDeCapitulos = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jTextField_NumeroDeAutores = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextArea_DescricaoLivro = new javax.swing.JTextArea();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        jButton_Cadastrar = new javax.swing.JButton();
        jSeparator3 = new javax.swing.JSeparator();
        jSeparator8 = new javax.swing.JSeparator();
        jButton_LimparCancelar = new javax.swing.JButton();
        jButton_AutorMenos = new javax.swing.JButton();
        jButton_CapituloMenos = new javax.swing.JButton();
        jButton_CapituloMaisMain = new javax.swing.JButton();
        jLabel12 = new javax.swing.JLabel();
        jButton_CapituloMais1 = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jButton_reset = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Livraria - Projeto APII");

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel1.setText("Livraria - Projeto APII Aula02");
        jLabel1.setToolTipText("");

        jTabbedPane_PainelComAbas.setToolTipText("");
        jTabbedPane_PainelComAbas.setAutoscrolls(true);

        jTable_Tabela.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Cód.", "Título", "ISBN", "Autor(es)", "Capítulos"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.Integer.class, java.lang.String.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jTable_Tabela);
        if (jTable_Tabela.getColumnModel().getColumnCount() > 0) {
            jTable_Tabela.getColumnModel().getColumn(0).setResizable(false);
            jTable_Tabela.getColumnModel().getColumn(1).setResizable(false);
            jTable_Tabela.getColumnModel().getColumn(2).setResizable(false);
            jTable_Tabela.getColumnModel().getColumn(2).setPreferredWidth(1);
            jTable_Tabela.getColumnModel().getColumn(3).setResizable(false);
            jTable_Tabela.getColumnModel().getColumn(4).setResizable(false);
            jTable_Tabela.getColumnModel().getColumn(4).setPreferredWidth(1);
        }

        jButton1.setText("Pesquisar por Título");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Pesquisar por Autor");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setText("Pesquisar por ISBN");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setText("X Remover");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton5.setText("Abrir livro");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton10.setText("Listar todos");
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });

        jSeparator6.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jSeparator7.setOrientation(javax.swing.SwingConstants.VERTICAL);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton3)
                .addGap(18, 18, 18)
                .addComponent(jSeparator6, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton10)
                .addGap(18, 18, 18)
                .addComponent(jSeparator7, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton4)
                .addGap(18, 18, 18)
                .addComponent(jButton5, javax.swing.GroupLayout.DEFAULT_SIZE, 101, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jButton1, jButton2, jButton3});

        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 282, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jButton1)
                                .addComponent(jButton2)
                                .addComponent(jButton3)
                                .addComponent(jButton4)
                                .addComponent(jButton5))
                            .addComponent(jButton10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jSeparator7, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(3, 3, 3))
                    .addComponent(jSeparator6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jTabbedPane_PainelComAbas.addTab("Listagem de livros cadastrados", jPanel1);

        jList_AutoresCadastradosList.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "*Nenhum livro/autor cadastrado" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jList_AutoresCadastradosList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jList_AutoresCadastradosList.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jScrollPane3.setViewportView(jList_AutoresCadastradosList);

        jLabel9.setText("Autores cadastrados:");

        jList_LivroDoAutorSelecionado.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "*Nenhum livro/autor cadastrado" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jList_LivroDoAutorSelecionado.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jList_LivroDoAutorSelecionado.setSelectedIndex(0);
        jScrollPane4.setViewportView(jList_LivroDoAutorSelecionado);

        jLabel10.setText("Livros do autor selecionado:");

        jTextArea_DescricaoAbaAutores.setColumns(20);
        jTextArea_DescricaoAbaAutores.setRows(5);
        jScrollPane5.setViewportView(jTextArea_DescricaoAbaAutores);

        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel11.setText("Descrição:");

        jButton_AbrirLivroAutoSelecionado.setText("Abrir livro");
        jButton_AbrirLivroAutoSelecionado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_AbrirLivroAutoSelecionadoActionPerformed(evt);
            }
        });

        jButton_AbrirLivroAutoSelecionado1.setText("Info");
        jButton_AbrirLivroAutoSelecionado1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_AbrirLivroAutoSelecionado1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 215, Short.MAX_VALUE)
                    .addComponent(jLabel9)
                    .addComponent(jButton_AbrirLivroAutoSelecionado1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 195, Short.MAX_VALUE)
                        .addComponent(jButton_AbrirLivroAutoSelecionado, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jLabel10))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jSeparator5)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, 102, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jSeparator9, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane5))
                .addContainerGap(22, Short.MAX_VALUE))
        );

        jPanel3Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jScrollPane3, jScrollPane4});

        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(jLabel10))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 257, Short.MAX_VALUE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel11, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jSeparator9, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jSeparator4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane5)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jSeparator5, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButton_AbrirLivroAutoSelecionado)
                        .addComponent(jButton_AbrirLivroAutoSelecionado1)))
                .addContainerGap())
        );

        jTabbedPane_PainelComAbas.addTab("Autores", jPanel3);

        jLabel2.setText("Título do livro:");

        jButton_AutorMais.setText("+");
        jButton_AutorMais.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_AutorMaisActionPerformed(evt);
            }
        });

        jButton_CapituloMais.setText("+");
        jButton_CapituloMais.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_CapituloMaisActionPerformed(evt);
            }
        });

        jLabel3.setText("ISBN(13 digitos):");

        jLabel4.setText("Numero de Autores:");

        jTextField_NumeroDeCapitulos.setEditable(false);
        jTextField_NumeroDeCapitulos.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField_NumeroDeCapitulos.setText("0");

        jLabel5.setText("Numero de Capítulos:");

        jTextField_NumeroDeAutores.setEditable(false);
        jTextField_NumeroDeAutores.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField_NumeroDeAutores.setText("0");

        jTextArea_DescricaoLivro.setColumns(20);
        jTextArea_DescricaoLivro.setRows(5);
        jTextArea_DescricaoLivro.setText("@ Sem Descrição.");
        jScrollPane2.setViewportView(jTextArea_DescricaoLivro);

        jLabel6.setText("Descrição:");

        jLabel7.setText("*");

        jLabel8.setText("* > 0");

        jButton_Cadastrar.setText("CADASTRAR");
        jButton_Cadastrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_CadastrarActionPerformed(evt);
            }
        });

        jSeparator8.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jButton_LimparCancelar.setText("LIMPAR/CANCELAR");
        jButton_LimparCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_LimparCancelarActionPerformed(evt);
            }
        });

        jButton_AutorMenos.setText("-");
        jButton_AutorMenos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_AutorMenosActionPerformed(evt);
            }
        });

        jButton_CapituloMenos.setText("-");
        jButton_CapituloMenos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_CapituloMenosActionPerformed(evt);
            }
        });

        jButton_CapituloMaisMain.setText("++");
        jButton_CapituloMaisMain.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_CapituloMaisMainActionPerformed(evt);
            }
        });

        jLabel12.setText("*");

        jButton_CapituloMais1.setText("Random ISBN");
        jButton_CapituloMais1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_CapituloMais1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator2)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(jSeparator8, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addGroup(jPanel2Layout.createSequentialGroup()
                                            .addComponent(jLabel6)
                                            .addGap(641, 641, 641))
                                        .addComponent(jScrollPane2))
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(jButton_CapituloMais1)
                                            .addGroup(jPanel2Layout.createSequentialGroup()
                                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                    .addComponent(jLabel12))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(jLabel2)
                                                    .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                    .addComponent(jTextField_TituloLivro)
                                                    .addComponent(jTextField_ISBN, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                        .addGap(10, 10, 10)
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel5)
                                            .addComponent(jLabel4))
                                        .addGap(10, 10, 10)
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGroup(jPanel2Layout.createSequentialGroup()
                                                .addComponent(jTextField_NumeroDeAutores, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jButton_AutorMenos)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jButton_AutorMais))
                                            .addGroup(jPanel2Layout.createSequentialGroup()
                                                .addComponent(jTextField_NumeroDeCapitulos, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jButton_CapituloMenos)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jButton_CapituloMais)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jButton_CapituloMaisMain)))))
                                .addGap(8, 8, 8))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 501, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
                                .addComponent(jButton_LimparCancelar)
                                .addGap(18, 18, 18)
                                .addComponent(jButton_Cadastrar)))
                        .addGap(9, 9, 9)))
                .addContainerGap())
        );

        jPanel2Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jButton_AutorMais, jButton_CapituloMais});

        jPanel2Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jLabel4, jLabel5});

        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(9, 9, 9)
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel2)
                                    .addComponent(jTextField_TituloLivro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel7)
                                    .addComponent(jLabel4)
                                    .addComponent(jTextField_NumeroDeAutores, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButton_AutorMais)
                                    .addComponent(jButton_AutorMenos))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel3)
                                    .addComponent(jTextField_ISBN, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel5)
                                    .addComponent(jTextField_NumeroDeCapitulos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButton_CapituloMais)
                                    .addComponent(jButton_CapituloMenos)
                                    .addComponent(jButton_CapituloMaisMain)
                                    .addComponent(jLabel12))
                                .addGap(18, 18, 18)
                                .addComponent(jLabel6))
                            .addComponent(jButton_CapituloMais1, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(24, 24, 24))
                    .addComponent(jSeparator8, javax.swing.GroupLayout.PREFERRED_SIZE, 246, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButton_Cadastrar)
                        .addComponent(jButton_LimparCancelar))
                    .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane_PainelComAbas.addTab("Cadastro de livros", jPanel2);

        jLabel13.setText("by - Yuri Bento; Patrick Porto; Marcos Motta");

        jLabel14.setText("Senac - FATEC 2017/2");

        jButton_reset.setText("Limpar Livraria");
        jButton_reset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_resetActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTabbedPane_PainelComAbas)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jSeparator1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton_reset))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(jLabel13))))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel14)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel1))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel13)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton_reset)
                            .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jTabbedPane_PainelComAbas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel14))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton_CadastrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_CadastrarActionPerformed
        // botao cadastrar livro
        cadastrarLivro();
    }//GEN-LAST:event_jButton_CadastrarActionPerformed

    private void jButton_AutorMaisActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_AutorMaisActionPerformed
        // botao + autores
        plusAutores();
    }//GEN-LAST:event_jButton_AutorMaisActionPerformed

    private void jButton_AutorMenosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_AutorMenosActionPerformed
        // botao - autores
        lessAutores();
    }//GEN-LAST:event_jButton_AutorMenosActionPerformed

    private void jButton_CapituloMenosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_CapituloMenosActionPerformed
        // botao - capitulos
        lessCapitulos();
    }//GEN-LAST:event_jButton_CapituloMenosActionPerformed

    private void jButton_CapituloMaisActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_CapituloMaisActionPerformed
        // botao + capitulos
        plusCapitulos();
    }//GEN-LAST:event_jButton_CapituloMaisActionPerformed

    private void jButton_CapituloMaisMainActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_CapituloMaisMainActionPerformed
        // botao ++ capitulos
        plusPlusCapitulos();
    }//GEN-LAST:event_jButton_CapituloMaisMainActionPerformed

    private void jButton_LimparCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_LimparCancelarActionPerformed
        // botao limpar/cancelar
        limparCampos();
    }//GEN-LAST:event_jButton_LimparCancelarActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // botao pesquisar por titulo
        pesquisarTitulo();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        // botao listar todos
        updateTabela();
    }//GEN-LAST:event_jButton10ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // botao pesquisar por Autor
        pesquisarAutor();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // botao pesquisar por ISBN
        pesquisarISBN();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // botao remover
        removerDaTabelaLista();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton_CapituloMais1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_CapituloMais1ActionPerformed
        // botao random ISBN
        jTextField_ISBN.setText(LivrariaUtil.gerarRandomIntString(13));
    }//GEN-LAST:event_jButton_CapituloMais1ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // botao ABRIR LIVRO tabela
        abrirLivroTabela();
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton_AbrirLivroAutoSelecionado1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_AbrirLivroAutoSelecionado1ActionPerformed
        // botao info
        autorInfoFromList();

    }//GEN-LAST:event_jButton_AbrirLivroAutoSelecionado1ActionPerformed

    private void jButton_AbrirLivroAutoSelecionadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_AbrirLivroAutoSelecionadoActionPerformed
        // botao ABRIR LIVRO autor list
        abrirLivroAutorList();
    }//GEN-LAST:event_jButton_AbrirLivroAutoSelecionadoActionPerformed

    private void jButton_resetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_resetActionPerformed
        //botao limpar livraria
        limparLivraria();
    }//GEN-LAST:event_jButton_resetActionPerformed

    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel("com.jtattoo.plaf.fast.FastLookAndFeel");
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(JFlivraria.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(JFlivraria.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(JFlivraria.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(JFlivraria.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new JFlivraria().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton_AbrirLivroAutoSelecionado;
    private javax.swing.JButton jButton_AbrirLivroAutoSelecionado1;
    private javax.swing.JButton jButton_AutorMais;
    private javax.swing.JButton jButton_AutorMenos;
    private javax.swing.JButton jButton_Cadastrar;
    private javax.swing.JButton jButton_CapituloMais;
    private javax.swing.JButton jButton_CapituloMais1;
    private javax.swing.JButton jButton_CapituloMaisMain;
    private javax.swing.JButton jButton_CapituloMenos;
    private javax.swing.JButton jButton_LimparCancelar;
    private javax.swing.JButton jButton_reset;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JList<String> jList_AutoresCadastradosList;
    private javax.swing.JList<String> jList_LivroDoAutorSelecionado;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JSeparator jSeparator7;
    private javax.swing.JSeparator jSeparator8;
    private javax.swing.JSeparator jSeparator9;
    private javax.swing.JTabbedPane jTabbedPane_PainelComAbas;
    private javax.swing.JTable jTable_Tabela;
    private javax.swing.JTextArea jTextArea_DescricaoAbaAutores;
    private javax.swing.JTextArea jTextArea_DescricaoLivro;
    private javax.swing.JTextField jTextField_ISBN;
    private javax.swing.JTextField jTextField_NumeroDeAutores;
    private javax.swing.JTextField jTextField_NumeroDeCapitulos;
    private javax.swing.JTextField jTextField_TituloLivro;
    // End of variables declaration//GEN-END:variables
}
