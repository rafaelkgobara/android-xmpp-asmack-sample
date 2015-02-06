package rafaelgobara.com.xmppasmacksample.utils;

import android.content.Context;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Toast;

import org.apache.harmony.javax.security.sasl.SaslException;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.RosterListener;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;

import java.io.IOException;
import java.util.Collection;

/**
 * Created by rafael.gobara on 05/02/15.
 */
public class XMPPController
{
    public static final String HOST = "10.3.0.3";
    public static final int PORT = 5222;
    public static final String SERVICE = "10.3.0.3";

    private static XMPPController mInstance = null;
    private Context mContext;
    private XMPPConnection mConnection;
    private Roster mRoster;

    public XMPPController() {}

    public static XMPPController getInstance()
    {
        if (mInstance == null)
            mInstance = new XMPPController();

        return mInstance;
    }

    public void init(Context context)
    {
        mContext = context;

        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
    }

    public void connect() throws IOException, XMPPException, SmackException
    {
        ConnectionConfiguration connConfig = new ConnectionConfiguration(HOST, PORT, SERVICE);
        connConfig.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);
        connConfig.setSendPresence(true);

        mConnection = new XMPPTCPConnection(connConfig);
        mConnection.connect();
    }

    public boolean isConnected()
    {
        return mConnection != null && mConnection.isConnected();
    }

    public void login(String login, String password) throws IOException, SmackException, XMPPException
    {
        this.connect();

        if (mConnection != null)
        {
            mConnection.login(login, password);

            Presence presence = new Presence(Presence.Type.available);
            mConnection.sendPacket(presence);
        }
    }

    public Roster getRoster()
    {
        mRoster = mConnection.getRoster();

        return mRoster;
    }

    public Presence getPresence(String user)
    {
        if (mRoster == null)
            this.getRoster();

        return mRoster.getPresence(user);
    }

    public XMPPConnection getConnection ()
    {
        return mConnection;
    }

    public ChatManager getChatManager ()
    {
        return ChatManager.getInstanceFor(mConnection);
    }
}
