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

public class HomeController extends MenuController implements Initializable {
    private static int whichIdIs;
    private boolean isEmpty=true;

    private static ArrayList<Integer> ids=new ArrayList<>();
    private static ArrayList<String> sender=new ArrayList<>();
    private static ArrayList<String> postContent=new ArrayList<>();
    private static ArrayList<String> postTime=new ArrayList<>();
    private static ArrayList<String> postDate=new ArrayList<>();
    private static ArrayList<String> postImage=new ArrayList<>();
    private static ArrayList<String> commentNum=new ArrayList<>();
    private static ArrayList<String> likeNum=new ArrayList<>();
    private static ArrayList<String> isBusiness=new ArrayList<>();


    private final String DB_url = "jdbc:mysql://localhost/users?serverTimezone=UTC";
    private final String username = "root";
    private final String Password = "ali12345678";


    @FXML
    Label showSender;
    @FXML
    TextArea showPostContent;
    @FXML
    Label showPostDate;
    @FXML
    Label showPostTime;
    @FXML
    ImageView showPostImage;
    @FXML
    Label showCommentNum;
    @FXML
    Label showLikeNum;
    @FXML
    Label showAdText;

    @FXML
    Label nextResult;
    public void next(MouseEvent mouseEvent) throws SQLException {
        if(!isEmpty){
            if(whichIdIs+1<ids.size()){
                whichIdIs++;
                showSender.setText(sender.get(whichIdIs));
                showPostContent.setText( postContent.get(whichIdIs));
                showPostTime.setText(postTime.get(whichIdIs));
                showPostDate.setText(postDate.get(whichIdIs));
                if(postImage.get(whichIdIs)!=null){
                    Image image=new Image(postImage.get(whichIdIs));
                    showPostImage.setImage(image);
                }
                else {
                    showPostImage.setImage(null);
                }
                showCommentNum.setText(countPostComment(ids.get(whichIdIs)));
                showLikeNum.setText(countPostLike(ids.get(whichIdIs)));
                if(isBusiness.get(whichIdIs).equals("yes")){
                    showAdText.setText("AD");
                }
                else {
                    showAdText.setText(null);
                }
                view(ids.get(whichIdIs));
                nextResult.setText(null);
                previousResult.setText(null);
            }
            else{
                nextResult.setText("End of posts!");
                previousResult.setText(null);
            }
        }

    }

    @FXML
    Label previousResult;
    public void previous(MouseEvent mouseEvent) throws SQLException {
        if(!isEmpty){
            if(whichIdIs-1>=0){
                whichIdIs--;
                showSender.setText(sender.get(whichIdIs));
                showPostContent.setText( postContent.get(whichIdIs));
                showPostTime.setText(postTime.get(whichIdIs));
                showPostDate.setText(postDate.get(whichIdIs));
                if(postImage.get(whichIdIs)!=null){
                    Image image=new Image(postImage.get(whichIdIs));
                    showPostImage.setImage(image);
                }
                else {
                    showPostImage.setImage(null);
                }
                showCommentNum.setText(countPostComment(ids.get(whichIdIs)));
                showLikeNum.setText(countPostLike(ids.get(whichIdIs)));
                if(isBusiness.get(whichIdIs).equals("yes")){
                    showAdText.setText("AD");
                }
                else {
                    showAdText.setText(null);
                }
                view(ids.get(whichIdIs));
                nextResult.setText(null);
                previousResult.setText(null);
            }
            else {
                previousResult.setText("This is the first post!");
                nextResult.setText(null);
            }
        }
    }

    public void like(MouseEvent mouseEvent) throws SQLException {
        Like like=new Like(true,Integer.toString(whichIdIs),null,MenuController.userName);

    }

    public void comment(MouseEvent mouseEvent) throws IOException {
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


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            Connection conn = DriverManager.getConnection(DB_url, username, Password);
            Statement followingStatement=conn.createStatement();
            String followingSql="SELECT * FROM followings";
            ResultSet followingResultSet=followingStatement.executeQuery(followingSql);
            ArrayList<String> followingList=new ArrayList<>();
            while (followingResultSet.next()){
                if(followingResultSet.getString(userName)!=null){
                    followingList.add(followingResultSet.getString(userName));
                }
            }

            ArrayList<String > sqls=new ArrayList<>();
            String firstSql = "SELECT * FROM posts WHERE sender NOT LIKE '"+userName+"'";
            for (String s : followingList) {
                if(s!=null){
                    sqls.add(firstSql+" AND sender LIKE '"+s+"'");
                }
            }


            Statement viewStatement=conn.createStatement();
            String lastView="SELECT * FROM  views WHERE viewer LIKE '"+userName+"'";
            ResultSet viewResultSet=viewStatement.executeQuery(lastView);
            ArrayList<Integer> view=new ArrayList<>();
            while (viewResultSet.next()){
                view.add(viewResultSet.getInt("postId"));
            }
            int count=0;
            for (String sql : sqls) {
                for (Integer integer : view) {
                    sql=sql+" AND id!="+integer;
                }
                sqls.set(count,sql);
                count++;
            }

            Statement statement=conn.createStatement();
            isEmpty=true;
            for (String sql : sqls) {
                ResultSet resultSet1=statement.executeQuery(sql);
                while (resultSet1.next()){
                    ids.add(resultSet1.getInt("id"));
                    sender.add(resultSet1.getString("sender"));
                    postContent.add(resultSet1.getString("postContent"));
                    postTime.add(resultSet1.getString("postTime"));
                    postDate.add(resultSet1.getString("postDate"));
                    postImage.add(resultSet1.getString("imagePath"));
                    commentNum.add(countPostComment(resultSet1.getInt("id")));
                    likeNum.add(countPostLike(resultSet1.getInt("id")));
                    isBusiness.add(resultSet1.getString("isBusiness"));
                    isEmpty=false;
                }
            }
            if(!isEmpty){
                whichIdIs=0;
                showSender.setText(sender.get(whichIdIs));
                showPostContent.setText( postContent.get(whichIdIs));
                showPostTime.setText(postTime.get(whichIdIs));
                showPostDate.setText(postDate.get(whichIdIs));
                if(postImage.get(whichIdIs)!=null){
                    Image image=new Image(postImage.get(whichIdIs));
                    showPostImage.setImage(image);
                }
                else {
                    showPostImage.setImage(null);
                }
                showCommentNum.setText(countPostComment(ids.get(whichIdIs)));
                showLikeNum.setText(countPostLike(ids.get(whichIdIs)));
                if(isBusiness.get(whichIdIs).equals("yes")){
                    showAdText.setText("AD");
                }
                else {
                    showAdText.setText(null);
                }
                view(ids.get(whichIdIs));
                nextResult.setText(null);
                previousResult.setText(null);
            }
            else{
                NoPost();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
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

    private void NoPost() throws IOException {
        System.out.println(313);
        Pane pane=null;
        pane= FXMLLoader.load(getClass().getResource("/FXML/NoPost.fxml"));
        Main.scene.setRoot(pane);
    }
}
