package chess.dao;

import chess.domain.piece.Color;

import java.sql.ResultSet;
import java.sql.SQLException;

public class GameDao {

    private GameDao() {
    }

    public static boolean enrollGameOf(String userId, String gameId, String turn) {
        deleteGameOf(userId);
        final var query = "INSERT INTO game VALUES(?, ?, ?)";
        try (var connection = ConnectionHandler.getConnection();
             var preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, userId);
            preparedStatement.setString(2, gameId);
            preparedStatement.setString(3, turn);
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static boolean alreadyExistGame(String userId) {
        final var query = "SELECT * FROM game WHERE user_id = ?";
        try (var connection = ConnectionHandler.getConnection();
             var preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, userId);
            var resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return true;
            }
            return false;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getGameIdOf(String userId) {
        final var query = "SELECT game_id FROM game WHERE user_id = ?";
        try (var connection = ConnectionHandler.getConnection();
             var preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, userId);
            var resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("game_id");
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean deleteGameOf(String userId) {
        final var query = "DELETE FROM game WHERE user_id = ?";
        try (var connection = ConnectionHandler.getConnection();
             var preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, userId);

            int result = preparedStatement.executeUpdate();

            if (result == 1) {
                return true;
            }

            return false;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public static Color getGameTurnOf(String gameId) {
        final var query = "SELECT turn FROM game WHERE game_id = ?";
        try (var connection = ConnectionHandler.getConnection();
             var preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, gameId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String turn = resultSet.getString("turn");
                return Color.valueOf(turn);
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
