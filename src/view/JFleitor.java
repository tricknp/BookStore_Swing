package view;

import entidades.Autor;
import entidades.Capitulo;
import entidades.Livro;
import javax.swing.DefaultListModel;
import util.ShowMessages;

public class JFleitor extends javax.swing.JFrame {

    Livro livro;
    JFlivraria livraria;
    DefaultListModel modelList;

    public JFleitor(Livro livro, JFlivraria livraria) {
        initComponents();
        this.livro = livro;
        this.livraria = livraria;
        preencherCampos();
    }
    
    private final void preencherCampos() {
        this.jTextArea_CorpoDoLivro.setText("");
        this.jTextArea_CorpoDoLivro.append("\n");
        this.jTextArea_CorpoDoLivro.append("___________[ " + livro.getTitulo() + " ]___________\n");
        this.jTextArea_CorpoDoLivro.append("\n");
        int capitulosSize = livro.getCapitulosList().size();
        for (int i = 0; i < capitulosSize; i++) {
            this.jTextArea_CorpoDoLivro.append("\n");
            this.jTextArea_CorpoDoLivro.append("Capítulo " + (i + 1) + "/" + capitulosSize + " - " + livro.getCapitulosList().get(i).getTitulo() + "\n");
            this.jTextArea_CorpoDoLivro.append("***************************\n");
            this.jTextArea_CorpoDoLivro.append(livro.getCapitulosList().get(i).getTexto());
        }
        this.jTextArea_Descricao.setText("");
        this.jTextArea_Descricao.append(livro.getDescricao());
        modelList = new DefaultListModel();
        atualizarListaAutores();
        this.jList_ListaAutores.setModel(modelList);
    }
    
    private void atualizarListaAutores() {
        modelList.clear();
        for (int i = 0; i < livro.getAutorList().size(); i++) {
            modelList.addElement(livro.getAutorList().get(i).getNome());
        }
    }

    private void adicionarAutorDoLivro() {
        if (livro.getAutorList().size() < 6) {
            JDAutoresDialog autoresDial = new JDAutoresDialog(this, true, livraria.getAutoresList(), livro, 6 - livro.getAutorList().size(), livraria.getAutoresCount());
            autoresDial.getAutoresSelecionados().addAll(livro.getAutorList());
            autoresDial.setVisible(true);

            for (int i = 0; i < autoresDial.getAutoresSelecionados().size(); i++) {
                if (!livro.getAutorList().contains(autoresDial.getAutoresSelecionados().get(i))) {
                    livro.getAutorList().add(autoresDial.getAutoresSelecionados().get(i));
                    autoresDial.getAutoresSelecionados().get(i).getLivrosDoAutor().add(livro);
                }
            }
            livraria.updateAutorJList();
            livraria.setAutoresCount(livraria.getAutoresCount() + autoresDial.getAutoresCount());
            atualizarListaAutores();
        } else {
            ShowMessages.showMessage("Livro ja possui numero máximo de autores.");
        }
    }

    private void removerAutorDoLivro() {
        try {
            int autorSelecionado = this.jList_ListaAutores.getSelectedIndex();
            System.out.println("selecionado: " + autorSelecionado);
            for (Autor autor : livraria.getAutoresList()) {
                System.out.println("for" + autor.getCod());
                if (autor.getCod() == livro.getAutorList().get(autorSelecionado).getCod()) {
                    System.out.println("achou");
                    for (int i = 0; i < autor.getLivrosDoAutor().size(); i++) {
                        if (autor.getLivrosDoAutor().get(i).getCod() == livro.getAutorList().get(i).getCod()) {
                            autor.getLivrosDoAutor().remove(i);
                            livro.getAutorList().remove(autorSelecionado);
                            break;
                        }
                    }
                    break;
                }
            }
            atualizarListaAutores();
            livraria.updateAutorJList();
        } catch (Exception e) {
            ShowMessages.showMessage("Nenhum autor selecionado.");
        }
    }
    
    private void buscarCapitulo() {
        try {
            int x = Integer.parseInt(jTextField_BuscaNumCapitulo.getText());
            jTextField_Titulo.setText(livro.getCapitulosList().get(x-1).getTitulo());
            System.out.println("af");
            jTextArea_TextoCapitulo.setText(livro.getCapitulosList().get(x-1).getTexto());
            jTextField_NumCapitulo.setText(""+x);
        } catch (Exception e) {
            ShowMessages.showMessage("Capítulo não encontrado.");
        }
    }
    
    private void salvarCapitulo() {
        try {
        Capitulo capitulo = livro.getCapitulosList().get(Integer.parseInt(jTextField_NumCapitulo.getText()) -1);
        capitulo.setTitulo(jTextField_Titulo.getText());
        capitulo.setTexto(jTextArea_TextoCapitulo.getText());
        } catch (Exception e) {
            ShowMessages.showMessage("Erro ao salvar capítulo.");
        }
        
        preencherCampos();
        limparComponentesCapitulos();
    }
    
    private void adicionarNovoCapitulo() {
        Capitulo capitulo = new Capitulo();
        livro.getCapitulosList().add(capitulo);
        this.jTextField_NumCapitulo.setText(livro.getCapitulosList().size()+"");
        salvarCapitulo();
    }
    
    private void limparComponentesCapitulos() {
        jTextField_Titulo.setText("");
        jTextField_NumCapitulo.setText("");
        jTextArea_TextoCapitulo.setText("");
    }

//============================================================================
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea_CorpoDoLivro = new javax.swing.JTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextArea_Descricao = new javax.swing.JTextArea();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jTextField_BuscaNumCapitulo = new javax.swing.JTextField();
        jButton_Buscar = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jTextField_Titulo = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTextArea_TextoCapitulo = new javax.swing.JTextArea();
        jButton_Salvar = new javax.swing.JButton();
        jButton_Cancelar = new javax.swing.JButton();
        jButton_AdicionarNovoCapitulo = new javax.swing.JButton();
        jTextField_NumCapitulo = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jList_ListaAutores = new javax.swing.JList<>();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();
        jSeparator3 = new javax.swing.JSeparator();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Leitor/Editor");

        jTextArea_CorpoDoLivro.setColumns(20);
        jTextArea_CorpoDoLivro.setRows(5);
        jScrollPane1.setViewportView(jTextArea_CorpoDoLivro);

        jTabbedPane1.addTab("Livro", jScrollPane1);

        jTextArea_Descricao.setColumns(20);
        jTextArea_Descricao.setRows(5);
        jScrollPane2.setViewportView(jTextArea_Descricao);

        jTabbedPane1.addTab("Descrição", jScrollPane2);

        jLabel1.setText("Capítulo:");

        jButton_Buscar.setText("Buscar");
        jButton_Buscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_BuscarActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel2.setText("Edição de capítulos");

        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel3.setText("Título:");

        jLabel4.setText("Texto:");

        jTextArea_TextoCapitulo.setColumns(20);
        jTextArea_TextoCapitulo.setRows(5);
        jScrollPane3.setViewportView(jTextArea_TextoCapitulo);

        jButton_Salvar.setText("Salvar");
        jButton_Salvar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_SalvarActionPerformed(evt);
            }
        });

        jButton_Cancelar.setText("Cancelar");
        jButton_Cancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_CancelarActionPerformed(evt);
            }
        });

        jButton_AdicionarNovoCapitulo.setText("Adicionar novo Capítulo");
        jButton_AdicionarNovoCapitulo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_AdicionarNovoCapituloActionPerformed(evt);
            }
        });

        jTextField_NumCapitulo.setEditable(false);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addComponent(jButton_AdicionarNovoCapitulo)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 102, Short.MAX_VALUE)
                        .addComponent(jButton_Cancelar)
                        .addGap(18, 18, 18)
                        .addComponent(jButton_Salvar))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField_Titulo)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField_NumCapitulo, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        jPanel3Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jButton_Cancelar, jButton_Salvar});

        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jTextField_Titulo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField_NumCapitulo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 241, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 13, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton_Salvar)
                    .addComponent(jButton_Cancelar)
                    .addComponent(jButton_AdicionarNovoCapitulo))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField_BuscaNumCapitulo, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton_Buscar)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jTextField_BuscaNumCapitulo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton_Buscar)
                    .addComponent(jLabel2))
                .addGap(18, 18, 18)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(55, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Capítulos", jPanel2);

        jList_ListaAutores.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane4.setViewportView(jList_ListaAutores);

        jButton2.setText("Remover");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setText("Adicionar");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator1)
                    .addComponent(jScrollPane4)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(0, 281, Short.MAX_VALUE)
                        .addComponent(jButton3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton2))
                    .addComponent(jSeparator2)
                    .addComponent(jSeparator3, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton2)
                    .addComponent(jButton3))
                .addGap(18, 18, 18)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(163, 163, 163)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 75, Short.MAX_VALUE)
                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Autores", jPanel1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1)
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // botao remover autor
        removerAutorDoLivro();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // botao adicionar autor
        adicionarAutorDoLivro();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton_BuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_BuscarActionPerformed
        // botao buscar capitulo
        buscarCapitulo();        
    }//GEN-LAST:event_jButton_BuscarActionPerformed

    private void jButton_SalvarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_SalvarActionPerformed
        // botao salvar capitulo
        salvarCapitulo();
    }//GEN-LAST:event_jButton_SalvarActionPerformed

    private void jButton_CancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_CancelarActionPerformed
        // botao cancelar
        limparComponentesCapitulos();
    }//GEN-LAST:event_jButton_CancelarActionPerformed

    private void jButton_AdicionarNovoCapituloActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_AdicionarNovoCapituloActionPerformed
        // botao adicionar novo capitulo
        adicionarNovoCapitulo();
    }//GEN-LAST:event_jButton_AdicionarNovoCapituloActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton_AdicionarNovoCapitulo;
    private javax.swing.JButton jButton_Buscar;
    private javax.swing.JButton jButton_Cancelar;
    private javax.swing.JButton jButton_Salvar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JList<String> jList_ListaAutores;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextArea jTextArea_CorpoDoLivro;
    private javax.swing.JTextArea jTextArea_Descricao;
    private javax.swing.JTextArea jTextArea_TextoCapitulo;
    private javax.swing.JTextField jTextField_BuscaNumCapitulo;
    private javax.swing.JTextField jTextField_NumCapitulo;
    private javax.swing.JTextField jTextField_Titulo;
    // End of variables declaration//GEN-END:variables
}
