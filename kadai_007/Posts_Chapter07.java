package kadai_007;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Posts_Chapter07 {
    public static void main(String[] args) {

        Connection con = null;
        PreparedStatement statement = null;

        String[][] postList = {
                { "1003", "2023-02-08", "昨日の夜は徹夜でした・・", "13" },
                { "1002", "2023-02-08", "お疲れ様です！", "12" },
                { "1003", "2023-02-09", "今日も頑張ります！", "18" },
                { "1001", "2023-02-09", "無理は禁物ですよ！", "17" },
                { "1002", "2023-02-10", "明日から連休ですね！", "20" }
            };

        try {
            // データベースに接続
            con = DriverManager.getConnection(
                "jdbc:mysql://localhost/challenge_java",
                "root",
                "020305Yuiha1"
            );

            System.out.println("データベース接続成功：" + con); // 接続成功メッセージを改善
            System.out.println("レコード追加を実行します");
            
            String sql = "INSERT INTO posts (user_id, posted_at, post_content, likes) VALUES (?, ?, ?, ?);";
            statement = con.prepareStatement(sql);
            
            int totalRecords = 0;
            for( int i = 0; i <postList.length; i++ ) {
                statement.setInt(1, Integer.parseInt(postList[i][0])); 
                statement.setDate(2, Date.valueOf(postList[i][1])); 
                statement.setString(3, postList[i][2]); 
                statement.setInt(4, Integer.parseInt(postList[i][3]));
                
                // レコードの挿入を実行
                totalRecords += statement.executeUpdate();
            }
            System.out.println(totalRecords + "件のレコードが追加されました");

            // SQLクエリを準備
            String selectSql = "SELECT posted_at, post_content, likes FROM posts WHERE user_id = 1002;";
            statement = con.prepareStatement(selectSql);
            
            System.out.println("ユーザーIDが1002のレコードを検索しました");

            ResultSet result = statement.executeQuery();
           
            // SQLクエリの実行結果を抽出
            int rowNum = 0;
            while(result.next()) {
                Date posted_at = result.getDate("posted_at");
                String post_content = result.getString("post_content");
                int likes = result.getInt("likes");
                System.out.println((++rowNum) + "件目：投稿日時=" + posted_at
                                   + "／投稿内容=" + post_content + "／いいね数=" + likes );
            }
        } catch(SQLException e) {
            System.out.println("エラー発生：" + e.getMessage());
        } finally {
            // 使用したオブジェクトを解放
            if( statement != null ) {
                try { statement.close(); } catch(SQLException ignore) {}
            }
            if( con != null ) {
                try { con.close(); } catch(SQLException ignore) {}
            }
        }
    }
}