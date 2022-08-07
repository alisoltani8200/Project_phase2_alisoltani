package Main;

public class CommentForShow {
    private int id;
    private String Sender;
    private String Content;
    private  int Like;
    private String Time;
    private String Date;

    public CommentForShow(int id, String sender, String content, int like, String time, String date) {
        this.id = id;
        Sender = sender;
        Content = content;
        Like = like;
        Time = time;
        Date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSender() {
        return Sender;
    }

    public void setSender(String sender) {
        Sender = sender;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public int getLike() {
        return Like;
    }

    public void setLike(int like) {
        Like = like;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }
}
