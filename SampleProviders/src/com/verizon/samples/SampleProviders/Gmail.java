package com.verizon.samples.SampleProviders;

import android.app.ListActivity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.widget.SimpleCursorAdapter;

public class Gmail extends ListActivity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        String columns[] = new String[] { ConversationColumns.SUBJECT, ConversationColumns.FROM, ConversationColumns.ID };
        int tocols[] = new int[] {android.R.id.text1, android.R.id.text2};
        Uri uri =  Uri.parse("content://gmail-ls/unread/^1");
        Cursor c = managedQuery(uri, columns, null, null, null);
        SimpleCursorAdapter sca = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_2, c, columns, tocols);
        setListAdapter(sca);
     }
    
    public static final class ConversationColumns {
        public static final String ID = "_id";
        public static final String SUBJECT = "subject";
        public static final String SNIPPET = "snippet";
        public static final String FROM = "fromAddress";
        public static final String DATE = "date";
        public static final String PERSONAL_LEVEL = "personalLevel";
        /** A list of label names with a space after each one (including the last one). This makes
         * it easier remove individual labels from this list using SQL. */
        public static final String LABEL_IDS = "labelIds";
        public static final String NUM_MESSAGES = "numMessages";
        public static final String MAX_MESSAGE_ID = "maxMessageId";
        public static final String HAS_ATTACHMENTS = "hasAttachments";
        public static final String HAS_MESSAGES_WITH_ERRORS = "hasMessagesWithErrors";
        public static final String FORCE_ALL_UNREAD = "forceAllUnread";

        private ConversationColumns() {}
    }
    
    public static final class MessageColumns {
    	  
    	          public static final String ID = "_id";
    	          public static final String MESSAGE_ID = "messageId";
    	          public static final String CONVERSATION_ID = "conversation";
    	          public static final String SUBJECT = "subject";
    	          public static final String SNIPPET = "snippet";
    	          public static final String FROM = "fromAddress";
    	          public static final String TO = "toAddresses";
    	          public static final String CC = "ccAddresses";
    	          public static final String BCC = "bccAddresses";
    	          public static final String REPLY_TO = "replyToAddresses";
    	          public static final String DATE_SENT_MS = "dateSentMs";
    	          public static final String DATE_RECEIVED_MS = "dateReceivedMs";
    	          public static final String LIST_INFO = "listInfo";
    	          public static final String PERSONAL_LEVEL = "personalLevel";
    	          public static final String BODY = "body";
    	          public static final String EMBEDS_EXTERNAL_RESOURCES = "bodyEmbedsExternalResources";
    	          public static final String LABEL_IDS = "labelIds";
    	          public static final String JOINED_ATTACHMENT_INFOS = "joinedAttachmentInfos";
    	          public static final String ERROR = "error";
    	          // TODO: add a method for accessing this
    	          public static final String REF_MESSAGE_ID = "refMessageId";
    	  
    	          // Fake columns used only for saving or sending messages.
    	          public static final String FAKE_SAVE = "save";
    	          public static final String FAKE_REF_MESSAGE_ID = "refMessageId";
    	  
    	          private MessageColumns() {}
    	      }
}