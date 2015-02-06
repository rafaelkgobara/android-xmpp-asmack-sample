package rafaelgobara.com.xmppasmacksample;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.RosterListener;
import org.jivesoftware.smack.packet.Presence;

import java.util.ArrayList;
import java.util.Collection;

import rafaelgobara.com.xmppasmacksample.adapters.RostersAdapter;
import rafaelgobara.com.xmppasmacksample.models.RosterModel;
import rafaelgobara.com.xmppasmacksample.utils.XMPPController;


public class RostersActivity extends ActionBarActivity
{
    XMPPController mXMPPController;

    ListView mLstRosters;
    ArrayList<RosterModel> mArrRosters;
    RostersAdapter mAdapter;
    RosterModel mRosterModel;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rosters);

        initUI();

        mXMPPController = XMPPController.getInstance();
        mXMPPController.init(this);

        if (mXMPPController.isConnected())
        {
            Roster roster = mXMPPController.getRoster();
            mRosterModel = new RosterModel();

            roster.addRosterListener(new RosterListener()
            {
                public void entriesAdded(Collection<String> addresses) { }

                public void entriesDeleted(Collection<String> addresses) { }

                public void entriesUpdated(Collection<String> addresses) { }

                public void presenceChanged(Presence presence)
                {
                    mArrRosters = mRosterModel.convertFrom(getApplicationContext(), mXMPPController.getRoster().getEntries());
                    mAdapter.updateRostersStatus(mArrRosters);

                    runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            mAdapter.notifyDataSetChanged();
                        }
                    });
                }
            });

            mArrRosters = mRosterModel.convertFrom(getApplicationContext(), mXMPPController.getRoster().getEntries());
            mAdapter = new RostersAdapter(getApplicationContext(), mArrRosters);
            mLstRosters.setAdapter(mAdapter);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_rosters, menu);
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
        mLstRosters = (ListView) this.findViewById(R.id.lst_rosters);
        mLstRosters.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Intent intent = new Intent(getApplicationContext(), MessagesActivity.class);
                intent.putExtra("user", mArrRosters.get(position).getUsername());

                startActivity(intent);
            }
        });
    }
}