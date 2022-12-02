package org.example;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Main {
    private static final Connection con;
    private static final String ruta = "src/main/resources/zonasBarriosCalles.csv";
    private static final Set<String> zonaSet = new HashSet<>();
    private static final Set<Barrio> barrioSet = new HashSet<>();
    private static final Set<Calle> calleSet = new HashSet<>();

    static {
        try {
            con = DriverManager.getConnection("jdbc:sqlite:src/main/resources/database.db");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        eliminarDatos();
        leerDatosArchivo();
        insertarDatos();
    }

    private static void insertarDatos() {
        int zonas, barrios, calles;

        String sql = "insert into Zona values (?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            for (String s : zonaSet) {
                ps.setString(1, s);
                ps.addBatch();
            }
            zonas = Arrays.stream(ps.executeBatch()).sum();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        sql = "insert into Barrio values (?,?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            for (Barrio barrio : barrioSet) {
                ps.setString(1, barrio.getNombre());
                ps.setString(2, barrio.getZona());
                ps.addBatch();
            }
            barrios = Arrays.stream(ps.executeBatch()).sum();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        sql = "insert into Calle values (?,?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            for (Calle calle : calleSet) {
                ps.setString(1, calle.getNombre());
                ps.setString(2, calle.getTipo());
                ps.addBatch();
            }
            calles = Arrays.stream(ps.executeBatch()).sum();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        sql = "insert into Calle_Barrio values (?,?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            for (Calle calle : calleSet) {
                ps.setString(1, calle.getBarrio());
                ps.setString(2, calle.getNombre());
                ps.addBatch();
            }
            ps.executeBatch();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Se han insertado " + zonas + " zonas en la tabla Zona");
        System.out.println("Se han insertado " + barrios + " barrios en la tabla Barrio");
        System.out.println("Se han insertado " + calles + " calles en la tabla Calle");
    }

    private static void eliminarDatos() {
        String sql = "delete from Calle";
        String sql1 = "delete from Calle_Barrio";
        String sql2 = "delete from Barrio";
        String sql3 = "delete from Zona";
        try (Statement s = con.createStatement()) {
            int calles = s.executeUpdate(sql);
            s.executeUpdate(sql1);
            int barrios = s.executeUpdate(sql2);
            int zonas = s.executeUpdate(sql3);

            System.out.println("Se han eliminado " + calles + " calles");
            System.out.println("Se han eliminado " + barrios + " barrios");
            System.out.println("Se han eliminado " + zonas + " zonas");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static void leerDatosArchivo() {
        File f = new File(ruta);
        if (!f.isFile()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(f, StandardCharsets.UTF_8))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(";");

                String zona = datos[0];
                String barrio = datos[1];
                try {
                    Integer.parseInt(datos[2]);
                } catch (NumberFormatException e) {
                    continue;
                }
                String tipo = datos[3];
                String calle = datos[4];

                zonaSet.add(zona);
                Barrio b = new Barrio(barrio, zona);
                barrioSet.add(b);
                Calle c = new Calle(calle, tipo, barrio);
                calleSet.add(c);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}