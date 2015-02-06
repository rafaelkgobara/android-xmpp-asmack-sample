package rafaelgobara.com.xmppasmacksample.models;

import android.content.Context;
import android.provider.ContactsContract;

import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.packet.Presence;

import java.util.ArrayList;
import java.util.Collection;

import rafaelgobara.com.xmppasmacksample.utils.XMPPController;

/**
 * Created by rafael.gobara on 06/02/15.
 */
public class RosterModel
{
    private int id;
    private String username;
    private String status;

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public ArrayList<RosterModel> convertFrom(Context context, Collection<RosterEntry> rosterEntries)
    {
        XMPPController controller = XMPPController.getInstance();
        controller.init(context);

        Roster roster = controller.getRoster();

        ArrayList<RosterModel> arrModel = new ArrayList<RosterModel>();

        for (RosterEntry entry : rosterEntries)
        {
            Presence presence = roster.getPresence(entry.getUser());

            RosterModel model = new RosterModel();
            model.setUsername(entry.getUser());
            model.setStatus(presence.getStatus());

            arrModel.add(model);
        }

        return arrModel;
    }
}
