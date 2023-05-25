package atm;

import java.io.Console;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

/**
 *
 * @author Default
 */
public class Info {

    private int Saldo;
    int Pin;
    String Identificacion, Nomb;
    Scanner entrada = new Scanner(System.in);

    public void Control() throws SQLException {
        //este es el inicio de sesion del ATM

        System.out.println("***********Bienvenido al sistema ATM*********** \n\n");

        System.out.print(" Identificancion: ");
        Identificacion = entrada.nextLine();

        System.out.print("\n Clave - Pin: ");
        Pin = entrada.nextInt();

        try (Connection con = Conexion.getConexion()) {
            //Connection con = Conexion.getConexion();
            int clave = 0;

            //Consultar base de datos (Con un campo en en especifico)
            String SQL = "SELECT * FROM Info WHERE Id = ?";
            PreparedStatement pstmt = con.prepareStatement(SQL);
            pstmt.setString(1, Identificacion); //Este parametro funciona para consultar los datos especificos de un usuario
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String pnom = rs.getString("Nombre");
                Nomb = pnom + " " + rs.getString("Apellido");
                clave = rs.getInt("Pin");
                Saldo = rs.getInt("Saldo");
                //System.out.println("Nombre: " + nombre + ", Edad: " + edad);
            }

            if (Pin == clave) {
                BorrarC();
                pInterfaz();
                //System.out.println("Hola");

            } else {
                System.out.println("Error, la identificacion o clave son incorrectas!!");
            }

            /*rs.close();
                st.close();
                con.close();*/
        } catch (Exception e) {
            System.out.println("Error en la conexion del inicio, " + e);
        }

    }

    public void pInterfaz() {
        Scanner esco = new Scanner(System.in);
        System.out.println("***********Menu*********** \n");
        System.out.print("Usuario: " + Identificacion + "\t\t\t  Nombre: " + Nomb);
        System.out.print("\n\n 1) Consultar Saldo \n 2) Depositar \n 3) Retirar \n 4) Salir \n ■_Elige una opcion: ");
        char ele = esco.next().charAt(0);

        switch (ele) {
            case '1':
                BorrarC();
                Csaldo();
                Pausa();
                BorrarC();
                pInterfaz();
                break;

            case '2':
                BorrarC();
                Deposito();
                Pausa();
                BorrarC();
                pInterfaz();
                break;

            case '3':
                BorrarC();
                Retiro();
                Pausa();
                BorrarC();
                pInterfaz();
                break;

            case '4':
                BorrarC();
                Pausa();
                System.exit(0);
                break;

            default:
                BorrarC();
                pInterfaz();

        }

    }

    public void Csaldo() {
        //Consultar saldo
        System.out.println("***********Saldo_Actual*********** \n");
        System.out.print("Usuario: " + Identificacion + "\t\t\t  Nombre: " + Nomb);
        System.out.println("\n\n Tu saldo actual es de: " + "$" + Saldo);

    }

    public void Deposito() {
        //Depositar dinero
        Scanner entrada = new Scanner(System.in);
        System.out.println("***********Deposito*********** \n");
        System.out.println("Usuario: " + Identificacion + "\t\t\t  Nombre: " + Nomb);
        System.out.println("_-_-_-_- " + Nomb + " El maximo deposito es $9,500 -_-_-_-_");
        System.out.print("\n\n Cuanto desea depositar: ");
        int pago = entrada.nextInt();

        Saldo += pago;

        try (Connection con = Conexion.getConexion()) {
            //Actualizacion (Modificacion de campos)
            PreparedStatement stmt;

            stmt = con.prepareStatement("UPDATE Info SET Saldo= '" + Saldo + "' WHERE Id='" + Identificacion + "'");
            stmt.executeUpdate();
            Statement statement = con.createStatement();

            System.out.println("Su nuevo balance es de: $" + Saldo);

        } catch (Exception e) {
            System.out.println("Error en el deposito " + e);
        }

    }

    public void Retiro() {
        //Modificar (Retirar dinero)
        Scanner entrada = new Scanner(System.in);
        System.out.println("***********Retiro*********** \n");
        System.out.println("Usuario: " + Identificacion + "\t\t\t  Nombre: " + Nomb);
        System.out.println("_-_-_-_- " + Nomb + " El maximo deposito es $9,500 -_-_-_-_");
        System.out.print("\n\n Cuanto desea retirar: ");
        int pago = entrada.nextInt();

        Saldo -= pago;

        try (Connection con = Conexion.getConexion()) {
            //Actualizacion (Modificacion de campos)
            PreparedStatement stmt;

            stmt = con.prepareStatement("UPDATE Info SET Saldo= '" + Saldo + "' WHERE Id='" + Identificacion + "'");
            stmt.executeUpdate();
            Statement statement = con.createStatement();

            System.out.println("Su nuevo balance es de: $" + Saldo);

        } catch (Exception e) {
            System.out.println("Error en el retiro " + e);
        }

    }

    public void BorrarC() {
        try {
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        } catch (Exception e) {
            /*No hacer nada*/
        }
    }

    public void Pausa() {
        try {
            // Pausa de 3 segundos (3000 milisegundos)
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
