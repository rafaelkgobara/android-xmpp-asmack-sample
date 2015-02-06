package rafaelgobara.com.xmppasmacksample;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.MessageTypeFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.util.StringUtils;

import java.util.Collection;

import rafaelgobara.com.xmppasmacksample.utils.XMPPController;


public class MainActivity extends ActionBarActivity
{
    Button mBtnSignin;
    TextView mTxtLogin;
    TextView mTxtPassword;

    XMPPController mXMPPController;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mXMPPController = XMPPController.getInstance();
        mXMPPController.init(this);

        if (mXMPPController.isConnected())
            startActivity(new Intent(this, RostersActivity.class));
        else
            initUI();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
        mTxtLogin = (TextView) this.findViewById(R.id.txt_login);
        mTxtLogin.setText("teste");

        mTxtPassword = (TextView) this.findViewById(R.id.txt_password);
        mTxtPassword.setText("123456");

        mBtnSignin = (Button) this.findViewById(R.id.btn_signin);
        mBtnSignin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                connect();
            }
        });
    }

    private void connect()
    {
        final ProgressDialog dialog = ProgressDialog.show(this, "Connecting...", "Please wait...", false);

        Thread t = new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    if (!mXMPPController.isConnected())
                        mXMPPController.login(mTxtLogin.getText().toString(), mTxtPassword.getText().toString());

                    startActivity(new Intent(getApplicationContext(), RostersActivity.class));
                }
                catch (Exception e)
                {
                    // TODO Implement exception
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }

                dialog.dismiss();
            }
        });

        t.start();
        dialog.show();
    }
}
