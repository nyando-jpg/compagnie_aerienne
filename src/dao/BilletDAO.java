package dao;

import model.Billet;
import util.DatabaseConnection;
import java.sql.*;

public class BilletDAO {

    public int insert(Billet billet) {
        String sql = "INSERT INTO billet (reservation_id, siege_vol_id, prix, statut) " +
                     "VALUES (?, ?, ?, ?::statut_billet_enum) RETURNING id";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, billet.getReservationId());
            stmt.setInt(2, billet.getSiegeVolId());
            stmt.setDouble(3, billet.getPrix());
            stmt.setString(4, billet.getStatut());
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
