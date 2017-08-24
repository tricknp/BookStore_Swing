package view;

import entidades.Autor;
import entidades.Livro;
import java.time.LocalDate;
import java.util.ArrayList;
import javax.swing.DefaultListModel;
import static util.ShowMessages.showInputString;
import static util.ShowMessages.showMessage;
import util.util;

public class JDAutoresDialog extends javax.swing.JDialog {

    private ArrayList<Autor> autoresList;
    private ArrayList<Autor> autoresSelecionados = new ArrayList<>();
    private DefaultListModel modelo = new DefaultListModel<>();
    private Livro livro;
    private int numeroDeAutoresRestantes;
    private int autoresCount;


    public JDAutoresDialog(java.awt.Frame parent, boolean modal, ArrayList<Autor> autoresList,Livro livro, int numAutores, int autoresCount) {
        super(parent, modal);
        initComponents();
        this.autoresList = autoresList;
        this.numeroDeAutoresRestantes = numAutores;
        this.autoresCount = autoresCount;
        this.livro = livro;
        listar();
    }

    private final void listar() {
        this.jTextField_numAutoresRestante.setText("" + numeroDeAutoresRestantes);
        modelo.clear();
        for (Autor autor : autoresList) {
            modelo.addElement(autor.getNome());
        }
        this.jList1.setModel(modelo);
    }

    private void select() {
        try {
            if (!autoresSelecionados.contains(autoresList.get(jList1.getSelectedIndex()))) {
                autoresSelecionados.add(autoresList.get(jList1.getSelectedIndex()));
                numeroDeAutoresRestantes -= 1;
                if (numeroDeAutoresRestantes <= 0) {
                    this.dispose();
                }
                listar();
            } else {
                showMessage("Autor já cadastrado.");
            }
        } catch (Exception e) {
            showMessage("Nenhum autor selecionado.");
        }
    }

    private void novo() {
        boolean setouNome;
        boolean setouData;
        String nome = "nome_autor";
        while (true) {
            try {
                nome = showInputString("Nome do Autor :");
                setouNome = true;
                break;
            } catch (Exception e) {
                showMessage("Valor inválido");
            }
        }
        LocalDate data;
        while (true) {
            try {
                data = util.getLocalDate(showInputString("Data nascimento do autor dd/mm/aaaa:"));
                setouData = true;
                break;
            } catch (Exception e) {
                showMessage("Valor inválido");
            }
        }
        if (setouNome && setouData) {
            Autor autor = new Autor(nome, data);
            autoresCount++;
            autor.setCod(autoresCount);
            this.autoresList.add(autor);
            System.out.println("done");
            listar();
        }
    }

    public ArrayList<Autor> getAutoresList() {
        return autoresList;
    }

    public ArrayList<Autor> getAutoresSelecionados() {
        return autoresSelecionados;
    }

    public int getAutoresCount() {
        return autoresCount;
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList<>();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jTextField_numAutoresRestante = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Autores Cadastrados");
        setUndecorated(true);
        setResizable(false);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel1.setText("Autores cadastrados");

        jList1.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane1.setViewportView(jList1);

        jButton1.setText("Selecionar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Novo");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jTextField_numAutoresRestante.setEditable(false);
        jTextField_numAutoresRestante.setText("0");

        jLabel2.setText("Restam selecionar:");

        jLabel3.setText("Autores");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 162, Short.MAX_VALUE)
                        .addComponent(jButton2))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField_numAutoresRestante, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel3))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jButton1, jButton2});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jTextField_numAutoresRestante, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 269, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // botao select
        select();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // botao novo
        novo();
    }//GEN-LAST:event_jButton2ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JList<String> jList1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jTextField_numAutoresRestante;
    // End of variables declaration//GEN-END:variables
}
