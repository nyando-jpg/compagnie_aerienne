package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import model.Societe;
import util.DatabaseConnection;

public class SocieteDao {
    public static List<Societe> getAll() {
        List<Societe> result = new ArrayList<>();
        String query = "SELECT * FROM societe";
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement pstmt = conn.prepareStatement(query);
            ResultSet res = pstmt.executeQuery();
            while (res.next()) {
                Societe s = new Societe();
                s.setId(res.getInt("id"));
                s.setNom(res.getString("nom"));
                result.add(s);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static Societe getById(int id) {
        String query = "SELECT * FROM societe WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, id);
            ResultSet res = pstmt.executeQuery();
            if (res.next()) {
                Societe s = new Societe();
                s.setId(res.getInt("id"));
                s.setNom(res.getString("nom"));
                return s;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean insert(Societe s) {
        String query = "INSERT INTO societe(nom) VALUES (?)";
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, s.getNom());
            int affected = pstmt.executeUpdate();
            if (affected > 0)
                return true;
            else {
                System.err.println("SocieteDao.insert: no rows affected when inserting societe");
                return false;
            }
        } catch (Exception e) {
            System.err.println("SocieteDao.insert error: " + e);
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static boolean update(Societe s) {
        String query = "UPDATE societe SET nom = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, s.getNom());
            pstmt.setInt(2, s.getId());
            return pstmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean delete(int id) {
        String query = "DELETE FROM societe WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
