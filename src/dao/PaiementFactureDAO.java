package dao;

import model.PaiementFacture;
import java.sql.*;
import java.util.*;

public class PaiementFactureDAO {
    public static List<PaiementFacture> getPaiementsBySociete(int societeId) {
        List<PaiementFacture> list = new ArrayList<>();
        String query = "SELECT pd.datePaiement, pd.montant, 'Diffusion #' || d.id || ' (VolOpere: ' || d.idVolOpere || ', Date: ' || d.dateDiff || ')' AS diffusionInfo FROM paiementDiff pd JOIN diffusion d ON pd.idDiffusion = d.id WHERE d.idSociete = ? ORDER BY pd.datePaiement DESC";
        try (Connection conn = util.DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, societeId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    PaiementFacture pf = new PaiementFacture();
                    pf.setDatePaiement(rs.getTimestamp("datePaiement"));
                    pf.setMontant(rs.getBigDecimal("montant"));
                    pf.setDiffusionInfo(rs.getString("diffusionInfo"));
                    list.add(pf);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
