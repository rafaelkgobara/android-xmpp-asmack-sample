package rafaelgobara.com.xmppasmacksample.adapters;

import android.content.Context;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.jivesoftware.smack.packet.Presence;

import java.util.ArrayList;
import java.util.Collection;

import rafaelgobara.com.xmppasmacksample.R;
import rafaelgobara.com.xmppasmacksample.models.RosterModel;
import rafaelgobara.com.xmppasmacksample.utils.XMPPController;

/**
 * Created by rafael.gobara on 05/02/15.
 */
public class RostersAdapter extends ArrayAdapter<RosterModel>
{
    private final LayoutInflater mInflater;
    private ArrayList<RosterModel> mValues;
    XMPPController mXMPPController;

    static class ViewHolder
    {
        TextView txtName;
        TextView txtPresence;
    }

    public RostersAdapter(Context context, ArrayList<RosterModel> values)
    {
        super(context, R.layout.adapter_rosters, values);

        mValues = values;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mXMPPController = XMPPController.getInstance();
        mXMPPController.init(context);
    }

    public void updateRostersStatus(ArrayList<RosterModel> values)
    {
        mValues = values;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View rowView;
        ViewHolder viewHolder;

        if (convertView == null)
            rowView = mInflater.inflate(R.layout.adapter_rosters, parent, false);
        else
            rowView = convertView;

        RosterModel roster = mValues.get(position);

        viewHolder = new ViewHolder();

        viewHolder.txtName = (TextView) rowView.findViewById(R.id.txt_name);
        viewHolder.txtName.setText(roster.getUsername());

        viewHolder.txtPresence = (TextView) rowView.findViewById(R.id.txt_presence);
        viewHolder.txtPresence.setText(roster.getStatus());

        return rowView;
    }
}