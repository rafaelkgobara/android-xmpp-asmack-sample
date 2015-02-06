package rafaelgobara.com.xmppasmacksample.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import rafaelgobara.com.xmppasmacksample.R;
import rafaelgobara.com.xmppasmacksample.models.MessageModel;
import rafaelgobara.com.xmppasmacksample.utils.XMPPController;

/**
 * Created by rafael.gobara on 05/02/15.
 */
public class MessagesAdapter extends ArrayAdapter<MessageModel>
{
    private final LayoutInflater mInflater;
    private ArrayList<MessageModel> mValues;
    XMPPController mXMPPController;

    static class ViewHolder
    {
        TextView txtName;
        TextView txtPresence;
    }

    public MessagesAdapter(Context context, ArrayList<MessageModel> values)
    {
        super(context, R.layout.adapter_rosters, values);

        mValues = values;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void updateMessages(ArrayList<MessageModel> values)
    {
        mValues = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View rowView;
        ViewHolder viewHolder;

        if (convertView == null)
            rowView = mInflater.inflate(R.layout.adapter_messages, parent, false);
        else
            rowView = convertView;

        MessageModel messageModel = mValues.get(position);

        viewHolder = new ViewHolder();

        viewHolder.txtName = (TextView) rowView.findViewById(R.id.txt_user);
        viewHolder.txtName.setText(messageModel.getUser());

        viewHolder.txtPresence = (TextView) rowView.findViewById(R.id.txt_message);
        viewHolder.txtPresence.setText(messageModel.getMessage());

        return rowView;
    }
}