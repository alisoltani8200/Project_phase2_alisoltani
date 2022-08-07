package Main;

import Main.Model.Like;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ExplorePostController extends MenuController implements Initializable {




    public static String userName;
    public static String name;
    private static int counter;
    private static int whichIdIs;
    private static String sql;
    private static ArrayList<Integer> ids=new ArrayList<>();


    private final String DB_url = "jdbc:mysql://localhost/users?serverTimezone=UTC";
    private final String username = "root";
    private final String Password = "ali12345678";


    @FXML
    Label sender;
    @FXML
    TextArea postContent;
    @FXML
    Label postDate;
    @FXML
    Label postTime;
    @FXML
    ImageView postImage;
    @FXML
    Label commentNum;
    @FXML
    Label likeNum;
    @FXML
    Label adText;

    @FXML
    Label nextResult;
    public void next(MouseEvent mouseEvent) throws SQLException {

        Connection conn = DriverManager.getConnection(DB_url, username, Password);
        Statement Statement1=conn.createStatement();
        String thisScopSql=sql+" AND id>"+whichIdIs;
        ResultSet resultSet1 = Statement1.executeQuery(thisScopSql);
        if(resultSet1.next()){
            sender.setText(resultSet1.getString("sender"));
            postContent.setText(resultSet1.getString("postContent"));
            postTime.setText(resultSet1.getString("postTime"));
            postDate.setText(resultSet1.getString("postDate"));
            if(resultSet1.getString("imagePath")!=null){
                Image image=new Image(resultSet1.getString("imagePath"));
                postImage.setImage(image);
            }
            else {
                postImage.setImage(null);
            }
            commentNum.setText(countPostComment(resultSet1.getInt("id")));
            likeNum.setText(countPostLike(resultSet1.getInt("id")));
            if(resultSet1.getString("isBusiness").equals("yes")){
                adText.setText("AD");
            }
            else {
                adText.setText(null);
            }
            whichIdIs= resultSet1.getInt("id");
            view(whichIdIs);
            nextResult.setText(null);
            previousResult.setText(null);
        }
        else{
            nextResult.setText("End of posts!");
            previousResult.setText(null);
        }
    }

    @FXML
    Label previousResult;
    public void previous(MouseEvent mouseEvent) throws SQLException {
        Connection conn = DriverManager.getConnection(DB_url, username, Password);
        Statement Statement2=conn.createStatement();
        int previous=0;
        if((ids.indexOf(whichIdIs)-1)>=0){
            previous=ids.get((ids.indexOf(whichIdIs)-1));
            String thisScopSql=sql+" AND id="+previous;
            ResultSet resultSet2 = Statement2.executeQuery(thisScopSql);
            if(resultSet2.next()){
                sender.setText(resultSet2.getString("sender"));
                postContent.setText(resultSet2.getString("postContent"));
                postTime.setText(resultSet2.getString("postTime"));
                postDate.setText(resultSet2.getString("postDate"));
                if(resultSet2.getString("imagePath")!=null){
                    Image image=new Image(resultSet2.getString("imagePath"));
                    postImage.setImage(image);
                }
                else {
                    postImage.setImage(null);
                }
                commentNum.setText(countPostComment(resultSet2.getInt("id")));
                likeNum.setText(countPostLike(resultSet2.getInt("id")));
                if(resultSet2.getString("isBusiness").equals("yes")){
                    adText.setText("AD");
                }
                else {
                    adText.setText(null);
                }
                whichIdIs= resultSet2.getInt("id");
                view(whichIdIs);
                previousResult.setText(null);
                nextResult.setText(null);
            }
        }
        else {
            previousResult.setText("This is the first post!");
            nextResult.setText(null);
        }
    }

    public void like(MouseEvent mouseEvent) throws SQLException {
        Like like=new Like(true,Integer.toString(whichIdIs),null,MenuController.userName);
        likeNum.setText(countPostLike(whichIdIs));
    }

    public void comment(MouseEvent mouseEvent) throws  IOException {
        CreateCommentController.postId=whichIdIs;
        Pane pane=null;
        pane= FXMLLoader.load(getClass().getResource("/FXML/CreateComment.fxml"));
        Main.scene.setRoot(pane);
    }


    private void view(int postId) throws SQLException {
        Connection conn = DriverManager.getConnection(DB_url, username, Password);
        Statement statement1=conn.createStatement();
        Statement statement2=conn.createStatement();
        String lastView="SELECT * FROM  views WHERE postId='"+postId+"'"+" AND viewer LIKE '"+userName+"'";
        ResultSet resultSet=statement1.executeQuery(lastView);
        if(!resultSet.next()){
            String viewSql="INSERT INTO views (postId,viewer,viewTime,viewDate) VALUES ('"+postId+"','"+userName+"','"+ LocalTime.now().format(Formatter1()).toString() +"','"+ LocalDate.now().toString()+"')";
            statement2.executeUpdate(viewSql);
        }
    }

    private DateTimeFormatter Formatter1(){
        return DateTimeFormatter.ofPattern("HH:mm");
    }


    private void follow(String follower,String following){
        try {
            Connection conn = DriverManager.getConnection(DB_url, username, Password);
            Statement statement = conn.createStatement();
            String followingSql = "INSERT INTO followers ("+following+") VALUES ('"+follower+"')";
            String followerSql = "INSERT INTO followings ("+follower+") VALUES ('"+following+"')";
            int b =statement.executeUpdate(followingSql);
            int a = statement.executeUpdate(followerSql);
            if(a==1&&b==1){
                System.out.println("You follow "+following+" successfully :)");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void unFollow(String follower,String following){
        try {
            Connection conn = DriverManager.getConnection(DB_url, username, Password);
            Statement followingStatement = conn.createStatement();
            Statement followerStatement=conn.createStatement();
            String followingSql = "DELETE FROM followers WHERE "+following+" LIKE '"+follower+"'";
            String followerSql = "DELETE FROM followings WHERE "+follower+" LIKE '"+following+"'";
            int a=followingStatement.executeUpdate(followingSql);
            int b=followerStatement.executeUpdate(followerSql);
            if(a==1&&b==1){
                System.out.println("You unfollow "+following+" successfully :)");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        sql="";
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(DB_url, username, Password);
            System.out.println(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            Statement followingStatement=conn.createStatement();
            String followingSql="SELECT * FROM followings";
            ResultSet followingResultSet=followingStatement.executeQuery(followingSql);
            ArrayList<String> followingList=new ArrayList<>();
            while (followingResultSet.next()){
                if(followingResultSet.getString(userName)!=null){
                    followingList.add(followingResultSet.getString(userName));
                }
            }
            Statement firstStatement = conn.createStatement();
            String firstSql = "SELECT * FROM posts WHERE sender NOT LIKE '"+userName+"'";
            for (String s : followingList) {
                if(s!=null){
                    firstSql=firstSql+" AND sender NOT LIKE '"+s+"'";
                }
            }
            Statement findId=conn.createStatement();
            ResultSet findIds=findId.executeQuery(firstSql);
            while (findIds.next()){
                ids.add(findIds.getInt("id"));
            }
            sql=firstSql;
            ResultSet resultSet = firstStatement.executeQuery(firstSql);
            if (resultSet.next()){
                sender.setText(resultSet.getString("sender"));
                postContent.setText(resultSet.getString("postContent"));
                postTime.setText(resultSet.getString("postTime"));
                postDate.setText(resultSet.getString("postDate"));
                if(resultSet.getString("imagePath")!=null){
                    Image image=new Image(resultSet.getString("imagePath"));
                    postImage.setImage(image);
                }
                commentNum.setText(countPostComment(resultSet.getInt("id")));
                likeNum.setText(countPostLike(resultSet.getInt("id")));
                if(resultSet.getString("isBusiness").equals("yes")){
                    adText.setText("AD");
                }
                else {
                    adText.setText(null);
                }
                whichIdIs= resultSet.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private String countPostLike(int id) throws SQLException {
        Connection conn = DriverManager.getConnection(DB_url, username, Password);
        Statement statement = conn.createStatement();
        String sql="SELECT * FROM likes WHERE isPost LIKE 'yes' AND postId="+id;
        ResultSet resultSet=statement.executeQuery(sql);
        int counter=0;
        while (resultSet.next()){
            counter++;
        }
        return Integer.toString(counter);
    }
    private String countPostComment(int id) throws SQLException {
        Connection conn = DriverManager.getConnection(DB_url, username, Password);
        Statement statement = conn.createStatement();
        String sql="SELECT * FROM comments WHERE isPost LIKE 'yes' AND postId="+id;
        ResultSet resultSet=statement.executeQuery(sql);
        int counter=0;
        while (resultSet.next()){
            counter++;
        }
        return Integer.toString(counter);
    }
}
