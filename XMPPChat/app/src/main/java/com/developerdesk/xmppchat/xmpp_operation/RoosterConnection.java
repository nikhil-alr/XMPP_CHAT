package com.developerdesk.xmppchat.xmpp_operation;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.developerdesk.xmppchat.Interface.ConnectionCallback;
import com.developerdesk.xmppchat.Interface.RoosterOperation;
import com.developerdesk.xmppchat.service.RoosterConnectionService;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.ReconnectionManager;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.chat.ChatManagerListener;
import org.jivesoftware.smack.chat.ChatMessageListener;
import org.jivesoftware.smack.packet.DefaultExtensionElement;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smackx.search.ReportedData;
import org.jivesoftware.smackx.search.UserSearch;
import org.jivesoftware.smackx.search.UserSearchManager;
import org.jivesoftware.smackx.xdata.Form;

import java.io.IOException;
import java.util.List;

/**
 * Created by gakwaya on 4/28/2016.
 */
public class RoosterConnection implements ConnectionListener,RoosterOperation {

    private static final String TAG = "RoosterConnection";

    private  final Context mApplicationContext;
    private  final String mUsername;
    private  final String mPassword;
    //private  final String mServiceName;
    private XMPPTCPConnection mConnection;
    private ConnectionCallback connectionCallback;


    public static enum ConnectionState
    {
        CONNECTED ,AUTHENTICATED, CONNECTING ,DISCONNECTING ,DISCONNECTED;
    }

    public static enum LoggedInState
    {
        LOGGED_IN , LOGGED_OUT;
    }


    public RoosterConnection( Context context , String userName,String password,ConnectionCallback connectionCallback)
    {

        this.mUsername = userName;
        this.mPassword = password;
        this.connectionCallback = connectionCallback;
        mApplicationContext = context.getApplicationContext();

    }


    public void connect() throws IOException,XMPPException,SmackException {

        String DOMAIN = "192.168.0.107";
        //String DOMAIN = "rooms.dismail.de";
        int PORT = 5222;
        //Log.d(TAG, "Connecting to server " + mServiceName);
        XMPPTCPConnectionConfiguration.Builder config = XMPPTCPConnectionConfiguration.builder();
        config.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);
        config.setUsernameAndPassword(mUsername, mPassword);
        config.setServiceName(DOMAIN);
        config.setHost(DOMAIN);
        config.setPort(PORT);
        config.setDebuggerEnabled(true);
        config.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);
        //config.setSocketFactory(SSLSocketFactory.getDefault());

        mConnection = new XMPPTCPConnection(config.build());
        try {
            mConnection.connect();
            mConnection.addConnectionListener(this);
        } catch (SmackException | IOException | XMPPException e) {
            e.printStackTrace();
        }


        mConnection.login();


        ChatManager.getInstanceFor(mConnection).addChatListener(new ChatManagerListener() {
            @Override
            public void chatCreated(Chat chat, boolean createdLocally) {

            }
        });


        ReconnectionManager reconnectionManager = ReconnectionManager.getInstanceFor(mConnection);
        reconnectionManager.setEnabledPerDefault(true);
        reconnectionManager.enableAutomaticReconnection();


        ChatManager.getInstanceFor(mConnection).addChatListener(new ChatManagerListener() {
            @Override
            public void chatCreated(Chat chat, boolean createdLocally) {
                chat.addMessageListener(new ChatMessageListener() {
                    @Override
                    public void processMessage(Chat chat, Message message) {
                        System.out.println("Received message: "
                                + (message != null ? message.getBody() : "NULL"));
                        Intent intent = new Intent("custom-event-name");
                        // You can also include some extra data.
                        intent.putExtra("message", message.getBody());
                        LocalBroadcastManager.getInstance(mApplicationContext).sendBroadcast(intent);
                    }
                });
            }
        });
    }


    public void disconnect()
    {
        //Log.d(TAG,"Disconnecting from server "+ mServiceName);
        if (mConnection != null)
        {
            mConnection.disconnect();
        }

        mConnection = null;


    }


    @Override
    public void connected(XMPPConnection connection) {
        RoosterConnectionService.sConnectionState=ConnectionState.CONNECTED;
        Log.d(TAG,"Connected Successfully");
        connectionCallback.connectedSuccessfully();
    }

    @Override
    public void authenticated(XMPPConnection connection, boolean resumed) {

        RoosterConnectionService.sConnectionState=ConnectionState.CONNECTED;
        Log.d(TAG,"Authenticated Successfully");


        Roster roseter = Roster.getInstanceFor(connection);

        Log.e("TAG",""+ roseter.getEntryCount());

        //addBuddy("vijay");


    }


    @Override
    public void connectionClosed() {
        RoosterConnectionService.sConnectionState=ConnectionState.DISCONNECTED;
        Log.d(TAG,"Connection closed()");
        connectionCallback.connectionError();
    }

    @Override
    public void connectionClosedOnError(Exception e) {
        RoosterConnectionService.sConnectionState=ConnectionState.DISCONNECTED;
        Log.d(TAG,"ConnectionClosedOnError, error "+ e.toString());
        connectionCallback.connectionError();
    }

    @Override
    public void reconnectingIn(int seconds) {
        RoosterConnectionService.sConnectionState = ConnectionState.CONNECTING;
        Log.d(TAG,"ReconnectingIn()");

    }

    @Override
    public void reconnectionSuccessful() {
        RoosterConnectionService.sConnectionState = ConnectionState.CONNECTED;
        Log.d(TAG,"ReconnectionSuccessful()");
        connectionCallback.connectedSuccessfully();
    }

    @Override
    public void reconnectionFailed(Exception e) {
        RoosterConnectionService.sConnectionState = ConnectionState.DISCONNECTED;
        Log.d(TAG,"ReconnectionFailed()");
        connectionCallback.connectionError();

    }





    public boolean sendmessage(String body, String destinationuser){

        String from = mConnection.getUser();

        ChatManager chatManager = ChatManager.getInstanceFor(mConnection);

        Message message = new Message(destinationuser, Message.Type.chat);

        message.setFrom(from);

        message.setBody(body);

        message.addExtension(new DefaultExtensionElement("from",from));

        message.addExtension(new DefaultExtensionElement("to", destinationuser));

        try {

            mConnection.sendStanza(message);

        } catch (SmackException.NotConnectedException e) {

            e.printStackTrace();

            return false;

        }

        return true;

    }



    public  void addBuddy(String username){
        Presence subscribe = new Presence(Presence.Type.subscribe);
        //I get deprecated inspection report on setTo(), but it still works
        subscribe.setTo(username+"@"+"192.168.0.110");
        try{
            mConnection.sendStanza(subscribe);
        }
        catch(Exception e){

        }

    }

    public static void getAllUsers()
    {
        try {
            UserSearchManager manager = new UserSearchManager(Utils.getConnection());
            String searchFormString = "search." + Utils.getConnection().getServiceName();
            Log.d("***", "SearchForm: " + searchFormString);
            Form searchForm = null;

            searchForm = manager.getSearchForm(searchFormString);

            Form answerForm = searchForm.createAnswerForm();

            UserSearch userSearch = new UserSearch();
            answerForm.setAnswer("Username", true);
            answerForm.setAnswer("search", "*");

            ReportedData results = userSearch.sendSearchForm(Utils.getConnection(), answerForm, searchFormString);
            if (results != null) {
                List<ReportedData.Row> rows = results.getRows();
                for (ReportedData.Row row : rows) {
                    Log.d("***", "row: " + row.getValues("Username").toString());
                }
            } else {
                Log.d("***", "No result found");
            }

        } catch (SmackException.NoResponseException e) {
            e.printStackTrace();
        } catch (XMPPException.XMPPErrorException e) {
            e.printStackTrace();
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
        }
    }
}