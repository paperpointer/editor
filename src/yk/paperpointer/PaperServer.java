package yk.paperpointer;

import yk.editor.JustExit;
import yk.jcommon.net.anio.*;
import yk.jcommon.utils.ErrorAlert;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.List;

import static java.lang.Math.max;
import static yk.jcommon.utils.Util.list;

/**
 * Created with IntelliJ IDEA.
 * User: yuri
 * Date: 2/4/14
 * Time: 9:06 PM
 */
public class PaperServer {
    public static final int PORT = 9353;
    private long lastTime = System.currentTimeMillis();
    private boolean paymentWarningIsActive = false;

    public static void main(String[] args) {
        new PaperServer().start();
    }

    private void start() {
        try {
            JOptionPane.showMessageDialog(null, "This is closed test version. Use it on your own risk.\nhttp://paperpointer.com");

            final DDDBrush brush = new DDDBrush();
            final List<AClient> clients = list();

            final JFrame frame = new JFrame("Paper pointer");
            frame.getContentPane().setPreferredSize(new Dimension(640, 480));

        //menu
            JMenuBar menuBar = new JMenuBar();
            JMenu menu = new JMenu("File");
            menu.setMnemonic(KeyEvent.VK_F);
            JMenuItem menuItem = new JMenuItem(new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JFileChooser chooser = new JFileChooser();
                    chooser.showOpenDialog(frame);
                    File file = chooser.getSelectedFile();
                }
            });
            menuItem.setText("OpenFile");
            menu.add(menuItem);
            menuBar.add(menu);
            frame.setJMenuBar(menuBar);

        //panel
            final JPanel panel = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    if (brush.bi != null) g.drawImage(brush.bi, 0, 0, null);
                }
            };
            panel.setPreferredSize(new Dimension(640, 480));
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            frame.getContentPane().add(panel);

        //labels
            final JLabel warning = new JLabel("payment notification window is showing");
            warning.setForeground(new Color(255, 100, 100));
            warning.setVisible(false);
            panel.add(warning);
            final JLabel fps = new JLabel("label 1");
            fps.setForeground(Color.WHITE);
            panel.add(fps);
            final JLabel position = new JLabel();
            panel.add(position);
            final JLabel rotation = new JLabel();
            panel.add(rotation);
            final JLabel connectedInfo = new JLabel();
            panel.add(connectedInfo);
            final JLabel posDif = new JLabel();
            panel.add(posDif);
            final JLabel rotDif = new JLabel();
            panel.add(rotDif);


            new Timer(1000*60*30, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    paymentWarningIsActive = true;
                    warning.setVisible(true);
                    JOptionPane.showMessageDialog(null, "This is closed test version. Use it on your own risk.\nhttp://paperpointer.com");
                    paymentWarningIsActive = false;
                    warning.setVisible(false);
                }
            }).start();

            final ASocket socket = new ASocket(PORT, new OnConnection() {
                @Override
                public void call(final AClient socket) {
                    clients.add(socket);
                    socket.onData = new OnData() {
                        @Override
                        public void call(Object data) {
                            System.out.println("received from client " + data);
                        }
                    };
                    socket.onDisconnect = new OnDisconnect() {
                        @Override
                        public void call() {
                            clients.remove(socket);
                        }
                    };
                }
            });

            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            frame.pack();
            frame.setVisible(true);

            new Timer(20, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    long newTime = System.currentTimeMillis();
                    fps.setText("fps: " + 1000 / max(1, (newTime - lastTime)));
                    lastTime = newTime;
                    if (!paymentWarningIsActive) brush.tick();
                    position.setText(String.format("x: %.1f y: %.1f z: %.1f", brush.arrowCamSpacePos.x, brush.arrowCamSpacePos.y, brush.arrowCamSpacePos.z));
                    rotation.setText(brush.arrowCamSpaceRot.toString());//TODO make axis angles
                    connectedInfo.setText("listeners: " + clients.size());
                    posDif.setText(String.format("pos dif: %.1f", brush.curPosDif));
                    rotDif.setText(String.format("rot dif: %.3f", brush.curRotDif));
                    socket.tick();
                    for (AClient client : clients) client.send(list(brush.arrowCamSpacePos, brush.arrowCamSpaceRot));
                    frame.repaint();
                }
            }).start();
            System.out.println("started");
        } catch (JustExit je) {
            System.out.println("Just exiting: " + je.getMessage());
            System.exit(1);
        } catch (Throwable t) {
            t.printStackTrace();
            new ErrorAlert(t);
        }

    }


}
