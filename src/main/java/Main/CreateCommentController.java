package Main;

import Main.Model.Comment;
import Main.Model.Like;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
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
import java.util.Objects;
import java.util.ResourceBundle;

public class CreateCommentController implements Initializable {
    public static int postId;

    private final String DB_url = "jdbc:mysql://localhost/users?serverTimezone=UTC";
    private final String username = "root";
    private final String Password = "ali12345678";




    @FXML
    TextField commentContent;
    @FXML
    Label resultText;
    public void sendComment(MouseEvent mouseEvent) {
        Comment comment=new Comment(true,Integer.toString(postId),null,commentContent.getText(),ExplorePostController.userName);
        resultText.setText("comment send successfully :) ");
    }


    @FXML
    TextArea commentContentShow;
    @FXML
    Label commentOfCommentResult;
    public void showCommentContent(MouseEvent mouseEvent){
        CommentForShow commentForShow=commentTable.getSelectionModel().getSelectedItem();
        commentContentShow.setText(commentForShow.getContent());
    }
    public void likeSelectedComment() throws SQLException {
        CommentForShow commentForShow=commentTable.getSelectionModel().getSelectedItem();
        System.out.println(commentForShow.getId());
        Like like=new Like(false,null,Integer.toString(commentForShow.getId()),MenuController.userName);
    }
    public void showCommentOfComment(MouseEvent mouseEvent) throws IOException, SQLException {
        CommentForShow commentForShow=commentTable.getSelectionModel().getSelectedItem();
        CommentOfCommentController.commentId= commentForShow.getId();
        Pane pane=null;
        pane= FXMLLoader.load(getClass().getResource("/FXML/CommentOfComment.fxml"));
        Main.scene.setRoot(pane);

    }
    public void back(MouseEvent mouseEvent) throws IOException {
        Pane pane=null;
        pane= FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/FXML/ExplorePost.fxml")));
        Main.scene.setRoot(pane);
    }

    @FXML
    TableView<CommentForShow> commentTable;
    @FXML
    TableColumn<CommentForShow,String> senderColumn;
    @FXML
    TableColumn<CommentForShow,String> likeColumn;
    @FXML
    TableColumn<CommentForShow,String> timeColumn;
    @FXML
    TableColumn<CommentForShow ,String> dateColumn;
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        senderColumn.setCellValueFactory(new PropertyValueFactory<>("Sender"));
        likeColumn.setCellValueFactory(new PropertyValueFactory<>("Like"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("Time"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("Date"));
        try {
            commentTable.setItems(getCommentList());
        } catch (SQLException e) {
            System.out.println(123456);
            e.printStackTrace();
        }
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(DB_url, username, Password);
            System.out.println(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            Statement followingStatement=conn.createStatement();
            Statement firstStatement = conn.createStatement();
            String sql = "SELECT * FROM posts WHERE id="+postId;
            ResultSet resultSet = firstStatement.executeQuery(sql);
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
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }


    public ObservableList<CommentForShow> getCommentList () throws SQLException {
        ObservableList<CommentForShow> commentList = FXCollections.observableArrayList();
        Connection conn = DriverManager.getConnection(DB_url, username, Password);

        Statement statement = conn.createStatement();
        String sql="SELECT * FROM comments WHERE isPost LIKE 'yes' AND postId="+postId;
        ResultSet resultSet=statement.executeQuery(sql);
        ArrayList<CommentForShow> commentForShowArrayList=new ArrayList<>();
        while (resultSet.next()){
            String sender=resultSet.getString("sender");
            int commentId=resultSet.getInt("id");
            String content=resultSet.getString("commentContent");
            int like=Integer.parseInt(countCommentLike(commentId));
            String time=resultSet.getString("commentTime");
            String date=resultSet.getString("commentDate");
            CommentForShow commentForShow=new CommentForShow(commentId,sender,content,like,time,date);
            commentForShowArrayList.add(commentForShow);
        }
        commentList.addAll(commentForShowArrayList);
        return commentList;
    }

    private String countCommentLike(int id) throws SQLException {
        Connection conn = DriverManager.getConnection(DB_url, username, Password);
        Statement statement = conn.createStatement();
        String sql="SELECT * FROM likes WHERE isPost LIKE 'no' AND commentId="+id;
        ResultSet resultSet=statement.executeQuery(sql);
        int counter=0;
        while (resultSet.next()){
            counter++;
        }
        return Integer.toString(counter);
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
    private DateTimeFormatter Formatter1(){
        return DateTimeFormatter.ofPattern("HH:mm");
    }

}
