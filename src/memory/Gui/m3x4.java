/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package memory.Gui;

import java.awt.Color;
import java.awt.Container;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 *
 * @author ALDO
 */
public class m3x4 extends javax.swing.JFrame {

    //array para saber que jlabel no se esta usando
    private int[][] placesI = {
        {0, 0, 0, 0},
        {0, 0, 0, 0},
        {0, 0, 0, 0}
    };
    private List<String> answers = new ArrayList<>();
    private String[] imgs = {"bam", "hwa ryun", "khun", "white", "bam2", "urek"};
    private boolean Select = false;
    private int selectAns = 0;
    private int previousSelectionX = 0;
    private int previousSelectionY = 0;
    private int win =0;
    private Timer timer;
    public m3x4() {
        initComponents();
        setLocationRelativeTo(null);
        // Deshabilitar la capacidad de redimensionar
        setResizable(false);
        this.jLabel15.setText("6");
        JLabel[][] places = {
            {this.jLabel2, this.jLabel4, this.jLabel3, this.jLabel11},
            {this.jLabel5, this.jLabel6, this.jLabel7, this.jLabel13},
            {this.jLabel8, this.jLabel9, this.jLabel10, this.jLabel14}
        };
        //se establecen las imagenes
        for (int i = 0; i < imgs.length; i++) {
            setImageTwo(imgs[i], places);
        }
        // Crear un MouseListener para cada jlabel
        MouseListener listener = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JLabel label = (JLabel) e.getSource();
                viewPhotos(label, 0);
                if (Select) {
                    if(intentosisZero()){
                        JOptionPane.showMessageDialog(rootPane,"No tienes mas intentos");
                        m3x4.this.dispose(); // Cierra la ventana
                    }else{
                        for (String s : answers) {
                            if (label.getText().equals(s)) {
                                if (selectAns % 2 == 0) {
                                    if (answers.get(selectAns + 1).equals(label.getText())) {
                                        System.out.println("Cartas iguales");
                                        win++;
                                        sonido();
                                    } else {
                                        mensajeDiferentes();
                                        enviarAtras(label);
                                        enviarAtras(places[previousSelectionX][previousSelectionY]);
                                        reduce();
                                    }
                                } else {
                                    if (answers.get(selectAns - 1).equals(label.getText())) {
                                        System.out.println("Cartas iguales");
                                        win++;
                                        sonido();
                                    } else {
                                        mensajeDiferentes();
                                        enviarAtras(label);
                                        enviarAtras(places[previousSelectionX][previousSelectionY]);
                                        reduce();
                                    }
                                }
                            }
                        }
                        selectAns = 0;
                        Select = false;
                        previousSelectionX = 0;
                        previousSelectionY = 0;
                    }
                } else {
                    viewPhotos(label, 0);
                    selectAns = answers.indexOf(label.getText());
                    Select = true;
                    //se guarda la posicion anteriors
                    for (int i = 0; i < places.length; i++) {
                        for (int j = 0; j < places[i].length; j++) {
                            if (places[i][j] == label) {
                                previousSelectionX = i;
                                previousSelectionY = j;
                            }
                        }
                    }
                }
            }
        };
        //se agrega a cada jlabel el evento
        for (int i = 0; i < places.length; i++) {
            for (int j = 0; j < places[i].length; j++) {
                places[i][j].addMouseListener(listener);
            }
        }
        //verifica si se gano
        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (win == 6) {
                    JOptionPane.showMessageDialog(rootPane, "Ganaste");
                    sonido();
                    timer.stop(); // Detiene el Timer
                    m3x4.this.dispose(); // Cierra la ventana
                }
            }
        });
        timer.start();
    }
    //comprobacion
    private boolean intentosisZero(){
        boolean is=false;
        if(Integer.parseInt(this.jLabel15.getText())==0){
            is=true;
        }else
            is=false;
        return is;
    }
    //reduce los intentos
    private void reduce(){
        int f=Integer.parseInt(this.jLabel15.getText())-1;
        this.jLabel15.setText(""+f);
    }
    //sonido
    private void sonido(){
         // Usar getResourceAsStream para obtener el archivo de sonido desde el classpath
        try (InputStream audioSrc = getClass().getResourceAsStream("/memory/Gui/Sound/NICE-Sound-Effect.wav")) {

            if (audioSrc == null) {
                throw new RuntimeException("El archivo de sonido no se encontró en la ruta especificada");
            }

            // Convertir InputStream en AudioInputStream
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioSrc);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start(); // Iniciar la reproducción

            // Mantener el programa en ejecución hasta que el sonido termine
            clip.addLineListener(new LineListener() {
                @Override
                public void update(LineEvent event) {
                    if (event.getType() == LineEvent.Type.STOP) {
                        clip.close(); // Cerrar el clip cuando termine el sonido
                    }
                }
            });

        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }
    //mostrar mensaje
    public void mensajeDiferentes() {
        // Crear un JOptionPane para mostrar el mensaje
        JOptionPane pane = new JOptionPane("Cartas diferentes", JOptionPane.INFORMATION_MESSAGE);

        // Crear un JDialog a partir del JOptionPane
        JDialog dialog = pane.createDialog("Mensaje");
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        // Crear un temporizador que cierra el diálogo después de 1 segundo (1000 ms)
        Timer timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose(); // Cerrar el diálogo
            }
        });

        timer.setRepeats(false); // El temporizador no se repite
        timer.start(); // Iniciar el temporizador

        dialog.setVisible(true); // Mostrar el diálogo
    }

    //Poner 1 imagen en 2 jlabels
    private void setImageTwo(String img, JLabel[][] places) {
        for (int i = 0; i < 2; i++) {
            JLabel f = s(places);
            setImage(f, "src\\memory\\Gui\\Imgs\\" + img + ".jpg");
            answers.add(f.getText());
            // Crear un JLabel que actúe como "tapa"
            JLabel coverLabel = tapa(f);

            // Crear un temporizador para mostrar la "tapa" después de 2 segundos
            Timer timer = new Timer(1500, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    coverLabel.setVisible(true); // Mostrar la "tapa"
                    ((Timer) e.getSource()).stop(); // Detener el temporizador
                }
            });
            timer.setRepeats(false);
            timer.start(); // Iniciar el temporizador para esperar 2 segundos
        }
    }

    private JLabel tapa(JLabel f) {
        // Crear un JLabel que actúe como "tapa"
        JLabel coverLabel = new JLabel();
        coverLabel.setOpaque(true);
        coverLabel.setBackground(Color.GRAY); // Color de la "tapa"
        coverLabel.setBounds(f.getBounds()); // Mismo tamaño y posición que el JLabel original
        coverLabel.setVisible(false); // Inicialmente oculto
        f.getParent().add(coverLabel);
        f.getParent().setComponentZOrder(coverLabel, 0);// Colocar la "tapa" al frente
        return coverLabel;
    }

    //poner 1 imagen en 1 jlabel
    private void setImage(JLabel labelname, String root) {
        ImageIcon image = new ImageIcon(root);
        Icon icon = new ImageIcon(
                image.getImage().getScaledInstance(labelname.getWidth(), labelname.getHeight(), Image.SCALE_DEFAULT));
        labelname.setIcon(icon);
        this.repaint();
    }

    //me devuelve 1 jlabel que no se este usando
    private JLabel s(JLabel[][] places) {
        JLabel l = null;
        int randomValue = (int) (Math.random() * 3);
        int randomValue2 = (int) (Math.random() * 4);
        if (placesI[randomValue][randomValue2] == 1) {
            return s(places);
        } else {
            l = places[randomValue][randomValue2];
            placesI[randomValue][randomValue2] = 1;
        }
        return l;
    }
    //Funciones para el evento de cada jlabel

    //me muestra las fotos
    private void viewPhotos(JLabel label, int i) {
        // Obtener el contenedor padre del JLabel
        Container parent = label.getParent();

        // Asegurarte de que el JLabel esté en el contenedor adecuado
        if (parent != null) {
            parent.setComponentZOrder(label, i); // Traer el JLabel al frente
            parent.revalidate();
            parent.repaint(); // Repintar el contenedor para reflejar los cambios
        }

    }

    //me manda para atras las fotos
    private void enviarAtras(JLabel label) {
        // Obtener el contenedor padre del JLabel
        Container parent = label.getParent();

        // Asegurarte de que el JLabel esté en el contenedor adecuado
        if (parent != null) {
            parent.setComponentZOrder(label, parent.getComponentCount() - 1); // Enviar el JLabel al fondo
            parent.revalidate();
            parent.repaint(); // Repintar el contenedor para reflejar los cambios
        }

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel12 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();

        jLabel12.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel2.setText("1");
        jLabel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel3.setText("3");
        jLabel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel4.setText("2");
        jLabel4.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel5.setText("5");
        jLabel5.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel6.setText("6");
        jLabel6.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel7.setText("7");
        jLabel7.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel8.setText("9");
        jLabel8.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel9.setText("10");
        jLabel9.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel10.setText("11");
        jLabel10.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel11.setText("4");
        jLabel11.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel13.setText("8");
        jLabel13.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel14.setText("12");
        jLabel14.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel1.setFont(new java.awt.Font("Lucida Bright", 0, 36)); // NOI18N
        jLabel1.setText("Intentos:");

        jLabel15.setFont(new java.awt.Font("Lucida Bright", 0, 36)); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(55, 55, 55)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(26, 26, 26)
                                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(60, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(35, 35, 35)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(20, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    // End of variables declaration//GEN-END:variables
}
