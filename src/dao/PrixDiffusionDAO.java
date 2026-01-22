package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.PrixDiffusion;
import util.DatabaseConnection;

public class PrixDiffusionDAO {
    public static List<PrixDiffusion> getAll() {
        String sql = "SELECT * FROM prixdiffusion";
        List<PrixDiffusion> result = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet res = pstmt.executeQuery();
            while (res.next()) {
                PrixDiffusion p = new PrixDiffusion();
                p.setId(res.getInt("id"));
                p.setIdSociete(res.getInt("idSociete"));
                p.setValeur(res.getFloat("valeur"));
                result.add(p);
            }
        } catch (SQLException s) {

        }
        return result;
    }
}
