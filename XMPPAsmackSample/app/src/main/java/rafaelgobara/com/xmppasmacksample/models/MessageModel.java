package rafaelgobara.com.xmppasmacksample.models;

/**
 * Created by rafael.gobara on 06/02/15.
 */
public class MessageModel
{
    private String id;
    private String user;
    private String message;

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getUser()
    {
        return user;
    }

    public void setUser(String user)
    {
        this.user = user;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }
}
