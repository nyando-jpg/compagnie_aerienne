package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Diffusion;
import util.DatabaseConnection;

public class DiffusionDAO {
    public static List<Diffusion> getAll() {
        String query = "SELECT * FROM diffusion";
        List<Diffusion> result = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement pstmt = conn.prepareStatement(query);
            ResultSet res = pstmt.executeQuery();
            while (res.next()) {
                Diffusion d = new Diffusion();
                d.setId(res.getInt("id"));
                d.setIdSociete(res.getInt("idSociete"));
                java.sql.Timestamp ts = res.getTimestamp("dateDiff");
                if (ts != null) {
                    d.setDate(ts);
                }
                d.setIdVolOpere(res.getInt("idVolOpere"));
                // nombre may be null for older rows; default to 0
                try {
                    d.setNombre(res.getInt("nombre"));
                } catch (Exception ex) {
                    d.setNombre(0);
                }
                result.add(d);
            }
        } catch (SQLException e) {

        }
        return result;
    }

    public static Diffusion getById(int id) {
        String query = "SELECT * FROM diffusion WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, id);
            ResultSet res = pstmt.executeQuery();
            if (res.next()) {
                Diffusion d = new Diffusion();
                d.setId(res.getInt("id"));
                d.setIdSociete(res.getInt("idSociete"));
                java.sql.Timestamp ts = res.getTimestamp("dateDiff");
                if (ts != null)
                    d.setDate(ts);
                d.setIdVolOpere(res.getInt("idVolOpere"));
                try {
                    d.setNombre(res.getInt("nombre"));
                } catch (Exception ex) {
                    d.setNombre(0);
                }
                return d;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean insert(Diffusion d) {
        String query = "INSERT INTO diffusion(idSociete, idVolOpere, dateDiff, nombre) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, d.getIdSociete());
            pstmt.setInt(2, d.getIdVolOpere());
            if (d.getDate() != null) {
                pstmt.setTimestamp(3, d.getDate());
            } else {
                pstmt.setNull(3, java.sql.Types.TIMESTAMP);
            }
            pstmt.setInt(4, d.getNombre());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean update(Diffusion d) {
        String query = "UPDATE diffusion SET idSociete = ?, idVolOpere = ?, dateDiff = ?, nombre = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, d.getIdSociete());
            pstmt.setInt(2, d.getIdVolOpere());
            if (d.getDate() != null) {
                pstmt.setTimestamp(3, d.getDate());
            } else {
                pstmt.setNull(3, java.sql.Types.TIMESTAMP);
            }
            pstmt.setInt(4, d.getNombre());
            pstmt.setInt(5, d.getId());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean delete(int id) {
        String query = "DELETE FROM diffusion WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static float calculerCADiff() {
        try {
            java.util.Map<String, Object> res = ChiffreAffaireDiffusionDAO.calculerCA(null, null, null, null, null);
            Object total = res.get("total");
            if (total instanceof Number) {
                return ((Number) total).floatValue();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0f;
    }
}
