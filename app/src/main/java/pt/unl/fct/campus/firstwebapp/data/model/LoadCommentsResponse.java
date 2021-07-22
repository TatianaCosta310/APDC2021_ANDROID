package pt.unl.fct.campus.firstwebapp.data.model;

import java.util.List;

public class LoadCommentsResponse {

    List<CommentObject2> comments;
    String cursor;
    public LoadCommentsResponse() {}
    public LoadCommentsResponse(List<CommentObject2> comments, String cursor) {
        this.comments=comments;
        this.cursor=cursor;
    }

    public List<CommentObject2> getList(){
        return  comments;
    }
}
