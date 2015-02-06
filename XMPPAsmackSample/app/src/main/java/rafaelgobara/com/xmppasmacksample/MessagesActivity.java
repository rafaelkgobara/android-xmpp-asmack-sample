package rafaelgobara.com.xmppasmacksample;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.ChatManagerListener;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.MessageTypeFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.util.StringUtils;

import java.util.ArrayList;
import rafaelgobara.com.xmppasmacksample.adapters.MessagesAdapter;
import rafaelgobara.com.xmppasmacksample.models.MessageModel;
import rafaelgobara.com.xmppasmacksample.utils.XMPPController;

public class MessagesActivity extends ActionBarActivity implements MessageListener
{
    XMPPController mXMPPController;
    ArrayList<MessageModel> mArrMessages;
    MessagesAdapter mAdapter;

    Chat mChat;

    ListView mLstMessages;
    EditText mTxtMessage;
    Button mBtnSend;

    String mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        initUI();

        mArrMessages = new ArrayList<MessageModel>();
        mAdapter = new MessagesAdapter(getApplicationContext(), mArrMessages);
        mLstMessages.setAdapter(mAdapter);

        mXMPPController = XMPPController.getInstance();
        mXMPPController.init(this);

        Intent intent = getIntent();
        mUser = intent.getStringExtra("user");

        final ChatManager chatManager = ChatManager.getInstanceFor(mXMPPController.getConnection());
        mChat = chatManager.createChat(mUser, this);

//        mChat = chatManager.createChat(mUser, new MessageListener()
//        {
//            @Override
//            public void processMessage(Chat chat, Message message)
//            {
//                Log.d("XMPPChat", "Received message: " + message);
//
////                MessageModel messageModel = new MessageModel();
////
////                messageModel.setId(message.getPacketID());
////                messageModel.setUser(message.getFrom());
////                messageModel.setMessage(message.getBody());
////
////                mArrMessages.add(messageModel);
////
////                setListAdapter();
//            }
//        });

        // Add a packet listener to get messages sent to us
        PacketFilter filter = new MessageTypeFilter(Message.Type.chat);
        mXMPPController.getConnection().addPacketListener(new PacketListener()
        {
            @Override
            public void processPacket(Packet packet)
            {
                Message message = (Message) packet;

                if (message.getBody() != null)
                {
                    MessageModel messageModel = new MessageModel();

                    messageModel.setId(message.getPacketID());
                    messageModel.setUser(StringUtils.parseBareAddress(message.getFrom()));
                    messageModel.setMessage(message.getBody());

                    mArrMessages.add(messageModel);

                    setListAdapter();
                }
            }
        }, filter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_message, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
            return true;

        return super.onOptionsItemSelected(item);
    }

    private void initUI()
    {
        mLstMessages = (ListView) this.findViewById(R.id.lst_messages);
        mTxtMessage = (EditText) this.findViewById(R.id.txt_message);
        mBtnSend = (Button) this.findViewById(R.id.btn_send);
        mBtnSend.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                try
                {
                    mChat.sendMessage(mTxtMessage.getText().toString());

                    mTxtMessage.setText("");
                }
                catch (XMPPException e)
                {
                    // TODO Implement exception
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
                catch (SmackException.NotConnectedException e)
                {
                    // TODO Implement exception
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void setListAdapter()
    {
        mAdapter.updateMessages(mArrMessages);

        runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void processMessage(Chat chat, Message message)
    {
        Log.d("Teste", message.getBody());
        Toast.makeText(this, message.getBody(), Toast.LENGTH_LONG).show();
    }
}
