import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.*;

public class VehicleInfoApp extends JFrame {
    private JLabel licensePlateLabel, entryTimeLabel, ticketLabel;
    private JTextField licensePlateTextField, entryTimeTextField, ticketTextField;

    public VehicleInfoApp() {
        super("Vehicle Information");
        initializeComponents();
        addComponentsToFrame();
    }

    private void initializeComponents() {
        licensePlateLabel = new JLabel("License Plate:");
        entryTimeLabel = new JLabel("Entry Time:");
        ticketLabel = new JLabel("Ticket:");

        licensePlateTextField = new JTextField(15);
        entryTimeTextField = new JTextField(15);
        ticketTextField = new JTextField(15);
        ticketTextField.setEditable(false);

        licensePlateTextField.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    String licensePlate = licensePlateTextField.getText();
                    fetchVehicleInfo(licensePlate);
                }
            }
        });
    }

    private void addComponentsToFrame() {
        setLayout(new GridLayout(3, 2));
        add(licensePlateLabel);
        add(licensePlateTextField);
        add(entryTimeLabel);
        add(entryTimeTextField);
        add(ticketLabel);
        add(ticketTextField);
    }

    private void fetchVehicleInfo(String licensePlate) {
        String jdbcURL = "jdbc:mysql://localhost:3306/doanoop";
        String username = "root";
        String password = "";

        try {
            Connection connection = DriverManager.getConnection(jdbcURL, username, password);
            String sql = "SELECT v.MATHE, v.THOIGIANVAO FROM vexe v INNER JOIN xe x ON v.MATHE = x.MATHE WHERE x.BIENSOXE = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, licensePlate);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int ticket = resultSet.getInt("MATHE");
                Timestamp entryTime = resultSet.getTimestamp("THOIGIANVAO");

                ticketTextField.setText(String.valueOf(ticket));
                entryTimeTextField.setText(entryTime.toString());
            } else {
                JOptionPane.showMessageDialog(this, "Vehicle not found!");
            }

            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                VehicleInfoApp app = new VehicleInfoApp();
                app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                app.pack();
                app.setVisible(true);
            }
        });
    }
}
