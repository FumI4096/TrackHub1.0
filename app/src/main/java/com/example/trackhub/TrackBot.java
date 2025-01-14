package com.example.trackhub;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class TrackBot extends AppCompatActivity {
    Database db;
    LinearLayout messageLyt;
    EditText inputMessageText;
    ImageView sendMessageBtn;
    String studentImage;
    private Button makeBombButton, whoDevelopedButton, capableLearningButton, introYourselfButton, backBtn, clearBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_track_bot);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        getWindow().setNavigationBarColor(getResources().getColor(android.R.color.black));
        getWindow().setStatusBarColor(getResources().getColor(android.R.color.black));

        db = new Database(this);

        messageLyt = findViewById(R.id.messageLayout);
        inputMessageText = findViewById(R.id.inputMessage);
        sendMessageBtn = findViewById(R.id.sendMessage);
        makeBombButton = findViewById(R.id.makeBomb);
        whoDevelopedButton = findViewById(R.id.whoDeveloped);
        capableLearningButton = findViewById(R.id.capableLearning);
        introYourselfButton = findViewById(R.id.introduceYourself);
        backBtn = findViewById(R.id.backButton);
        clearBtn = findViewById(R.id.clearButton);


        String studentId = getIntent().getStringExtra("studentId");

        studentImage = db.getStudentImage(studentId);

        messageLyt.removeAllViews();

        setMessage(studentId);

        makeBombButton.setOnClickListener(v -> {
            String buttonMessage = makeBombButton.getText().toString();
            String botMessage = "I am not programmed to perform this task";

            displayText(buttonMessage, botMessage);

            db.insertBotMessage(studentId, buttonMessage, botMessage);
        });

        whoDevelopedButton.setOnClickListener(v -> {
            String buttonMessage = whoDevelopedButton.getText().toString();
            String botMessage = "I was created by my master, Mimon Maiquez, December 12, 2024";

            displayText(buttonMessage, botMessage);
            db.insertBotMessage(studentId, buttonMessage, botMessage);
        });

        capableLearningButton.setOnClickListener(v -> {
            String buttonMessage = capableLearningButton.getText().toString();
            String botMessage = "I cannot, I am not capable of broadening my knowledge as I can only perform specific tasks. Do you have any tasks for me to do?";

            displayText(buttonMessage, botMessage);
            db.insertBotMessage(studentId, buttonMessage, botMessage);
        });

        introYourselfButton.setOnClickListener(v -> {
            String buttonMessage = introYourselfButton.getText().toString();
            String botMessage = "Hello, I am TrackBot, your guide and navigator through this platform! How I may at your service?";

            displayText(buttonMessage, botMessage);

            db.insertBotMessage(studentId, buttonMessage, botMessage);
        });

        sendMessageBtn.setOnClickListener(v -> {
            String message = inputMessageText.getText().toString();
            String botMessage = null;

            if (message.isEmpty() || message == null){
                Toast.makeText(this, "Message is empty", Toast.LENGTH_SHORT).show();
            }
            else{

                if((message.contains("How") || message.contains("Where") || message.contains("how") || message.contains("where")) && message.contains("post") && (message.contains("items") || message.contains("item")) && message.contains("lost")){
                    botMessage = "You can post item by clicking the \"Add Lost Item Post\" in the home section. Did my answer help?";
                }
                else if((message.contains("How") || message.contains("Where") || message.contains("how") || message.contains("where")) && message.contains("post") && (message.contains("items") || message.contains("item")) && message.contains("found")){
                    botMessage = "You can post item by clicking the \"Add Found Item Post\" at home. Did my answer help?";
                }
                else if((message.contains("How") || message.contains("Where") || message.contains("how") || message.contains("where")) && ((message.contains("post")) || (message.contains("posts")) || (message.contains("items") || message.contains("item")))){
                    botMessage = "You can post item by clicking the \"Add Lost Item Post\" or \"Add Found Item Post\". Did my answer help?";
                }
                else if((message.contains("How") || message.contains("how")) && message.contains("change") && (message.contains("profile") || message.contains("pfp") || message.contains("dp"))){
                    botMessage = "You can go to your profile by clicking the profile icon at the very top right corner. After that, click the profile image on the middle and choose the image you want, then click submit! Did this message help?";
                }
                else if((message.contains("How") || message.contains("Where") || message.contains("how") || message.contains("where")) && (message.contains("post") || message.contains("posted") || message.contains("posts")) || (message.contains("items") || message.contains("item")) && (message.contains("see") || message.contains("view")) && message.contains("lost")){
                    botMessage = "You can click the \"See Lost Item Posts\" and \"See Found Item Posts\" navigation to check your posts. Did my answer answered your question?";
                }
                else if((message.contains("How") || message.contains("Where") || message.contains("how") || message.contains("where")) && (message.contains("post") || message.contains("posted")) && (message.contains("items") || message.contains("item")) && (message.contains("see") || message.contains("view")) && message.contains("found")){
                    botMessage = "You can click the \"See Found Item Posts\" navigation to check your posts. Did my answer answered your question?";
                }
                else if((message.contains("How") || message.contains("Where") || message.contains("how") || message.contains("where")) && (message.contains("post") || message.contains("posted")) && message.contains("items") && message.contains("my")){
                    botMessage = "You can click the \"See Your Posts\" navigation to check your posts. Did my answer answered your question?";
                }
                else if((message.contains("How") || message.contains("Where") || message.contains("how") || message.contains("where")) && (message.contains("post") || message.contains("posted")) && (message.contains("items") || message.contains("item")) && (message.contains("see") || message.contains("view"))){
                    botMessage = "You can view posted items by clicking the \"See Lost Item Post\" or \"See Found Item Post\". Did my answer help?";
                }
                else if((message.contains("How") || message.contains("Where") || message.contains("how") || message.contains("where")) && (message.contains("notification") || message.contains("notifications") || message.contains("Notification") || message.contains("Notifications"))){
                    botMessage = "By clicking the notification bell on the top right corner, you can now view your notifications. Did I answer your question?";
                }
                else if(message.contains("No") || message.contains("no") || message.contains("Nope") || message.contains("nope") && !(message.contains("notification") || message.contains("notifications") || message.contains("Notification") || message.contains("Notifications"))){
                    botMessage = "I am sorry to hear that. I may have not understand your concern.";
                }
                else if(message.contains("Yes") || message.contains("yes") || message.contains("yess") || message.contains("Yess") || message.contains("Yep") || message.contains("yep") || message.contains("Yesss") || message.contains("yesss")){
                    botMessage = "Great! Let me know if you need anything!";
                }
                else if(message.contains("Hello") || message.contains("Hi") || message.contains("hello") || message.contains("hi")){
                    botMessage = "Hello there! How may I help you?";
                }
                else{
                    botMessage = "Sorry I do not understand your statement.";
                }

                displayText(message, botMessage);
                db.insertBotMessage(studentId, message, botMessage);
            }


        });

        backBtn.setOnClickListener(v -> {
            Intent backPage = new Intent(TrackBot.this, main_page.class);
            backPage.putExtra("StudentID", studentId);
            startActivity(backPage);
        });

        clearBtn.setOnClickListener(v -> {
            clearChat(studentId);

            CardView profileBotView = new CardView(this);
            CardView.LayoutParams profileBotLayoutParams = new CardView.LayoutParams(
                    160,
                    160
            );
            profileBotLayoutParams.setMargins(0, 0, 20, 0);
            profileBotView.setLayoutParams(profileBotLayoutParams);

            ImageView profileBot = new ImageView(this);
            profileBot.setImageResource(R.drawable.bot);
            profileBot.setPadding(50, 50, 50, 50);
            profileBot.setBackgroundColor(Color.parseColor("#76f0ff"));

            float radiusInDp = 80f;
            float radiusInPx = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    radiusInDp,
                    getResources().getDisplayMetrics()
            );

            profileBotView.setRadius(radiusInPx);
            profileBotView.setUseCompatPadding(false);
            profileBotView.setCardElevation(0);

            profileBotView.addView(profileBot);

            studentImage = db.getStudentImage(studentId);

            LinearLayout startBotMessageLayout = new LinearLayout(this);
            TextView startBotMessage = new TextView(this);

            String botStatement = "Hello " + db.getStudentName(studentId) + "! TrackBot in your utmost guidance service! How may I help you?";

            startBotMessage.setText(botStatement);
            startBotMessage.setTextColor(ContextCompat.getColor(this, R.color.black));
            startBotMessage.setPadding(50, 50, 50, 50);
            startBotMessage.setBackgroundColor(ContextCompat.getColor(this, R.color.linkColor));

            startBotMessageLayout.setPadding(20, 20, 20, 20);
            LinearLayout.LayoutParams startBotParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            startBotParams.gravity = Gravity.START;
            startBotMessageLayout.setLayoutParams(startBotParams);
            startBotMessageLayout.addView(profileBotView);
            startBotMessageLayout.addView(startBotMessage);

            messageLyt.addView(startBotMessageLayout);
        });

    }

    public void setMessage(String studentId){
        Cursor cursor = db.getBotMessage(studentId);

        if(cursor != null && cursor.moveToFirst()){
            //int studentIdIndex = cursor.getColumnIndex(Database.columnBotStudentID);
            int studentMessageIndex = cursor.getColumnIndex(Database.columnBotStudentMessage);
            int botMessageIndex = cursor.getColumnIndex(Database.columnBotMessage);

            do{
                if(studentMessageIndex != -1 && botMessageIndex != -1){
                    String studentMessage = cursor.getString(studentMessageIndex);
                    String botMessage = cursor.getString(botMessageIndex);

                    displayText(studentMessage, botMessage);
                }

            }
            while(cursor.moveToNext());
        }
        else{
            CardView profileBotView = new CardView(this);
            CardView.LayoutParams profileBotLayoutParams = new CardView.LayoutParams(
                    160,
                    160
            );
            profileBotLayoutParams.setMargins(0, 0, 20, 0);
            profileBotView.setLayoutParams(profileBotLayoutParams);

            ImageView profileBot = new ImageView(this);
            profileBot.setImageResource(R.drawable.bot);
            profileBot.setPadding(50, 50, 50, 50);
            profileBot.setBackgroundColor(Color.parseColor("#76f0ff"));

            float radiusInDp = 80f;
            float radiusInPx = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    radiusInDp,
                    getResources().getDisplayMetrics()
            );

            profileBotView.setRadius(radiusInPx);
            profileBotView.setUseCompatPadding(false);
            profileBotView.setCardElevation(0);

            profileBotView.addView(profileBot);


            studentImage = db.getStudentImage(studentId);

            LinearLayout startBotMessageLayout = new LinearLayout(this);
            TextView startBotMessage = new TextView(this);

            String botStatement = "Hello " + db.getStudentName(studentId) + "! TrackBot in your utmost guidance service! How may I help you?";

            startBotMessage.setText(botStatement);
            startBotMessage.setTextColor(ContextCompat.getColor(this, R.color.black));
            startBotMessage.setPadding(50, 50, 50, 50);
            startBotMessage.setBackgroundColor(ContextCompat.getColor(this, R.color.linkColor));

            startBotMessageLayout.setPadding(20, 20, 20, 20);
            LinearLayout.LayoutParams startBotParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            startBotParams.gravity = Gravity.START;
            startBotMessageLayout.setLayoutParams(startBotParams);
            startBotMessageLayout.addView(profileBotView);
            startBotMessageLayout.addView(startBotMessage);

            messageLyt.addView(startBotMessageLayout);

        }

    }

    public void displayText(String userInput, String botInput){
        LinearLayout userMessageLayout = new LinearLayout(this);
        LinearLayout botMessageLayout = new LinearLayout(this);
        ImageView profileCard = new ImageView(this);
        ImageView profileBot = new ImageView(this);

        float radiusInDp = 80f;
        float radiusInPx = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                radiusInDp,
                getResources().getDisplayMetrics()
        );

        if(studentImage != null){
            profileCard.setImageURI(Uri.parse(studentImage));
        }
        else{
            profileCard.setImageResource(R.drawable.profilehub);
        }

        profileBot.setImageResource(R.drawable.bot);
        profileBot.setPadding(50, 50, 50, 50);
        profileBot.setBackgroundColor(Color.parseColor("#76f0ff"));

        CardView profileBotView = new CardView(this);
        CardView.LayoutParams profileBotLayoutParams = new CardView.LayoutParams(
                160,
                160
        );
        profileBotLayoutParams.setMargins(0, 0, 20, 0);
        profileBotView.setLayoutParams(profileBotLayoutParams);

        CardView profileCardView = new CardView(this);
        CardView.LayoutParams profileCardLayoutParams = new CardView.LayoutParams(
                160,
                160
        );
        profileCardLayoutParams.setMargins(20, 0, 0, 0);
        profileCardView.setLayoutParams(profileCardLayoutParams);

        profileBotView.setRadius(radiusInPx);
        profileCardView.setRadius(radiusInPx);
        profileBotView.setUseCompatPadding(false);
        profileCardView.setUseCompatPadding(false);
        profileBotView.setCardElevation(0);
        profileCardView.setCardElevation(0);

        profileCard.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        ));

        profileBot.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        ));

        profileCardView.addView(profileCard);
        profileBotView.addView(profileBot);

        TextView userMessage = new TextView(this);
        TextView botMessage = new TextView(this);

        int maxCharactersPerLine = 42; // Characters limit per line
        String[] lines = userInput.split("(?<=\\G.{" + maxCharactersPerLine + "})");
        StringBuilder formattedText = new StringBuilder();
        for (int i = 0; i < lines.length; i++) {
            if (i > 0) {
                formattedText.append("-");
                formattedText.append("\n");
            }
            formattedText.append(lines[i]);
        }

        userMessage.setText(formattedText);
        userMessage.setPadding(50, 50, 50, 50);
        botMessage.setText(botInput);
        botMessage.setPadding(50, 50, 50, 50);
        userMessage.setTextColor(ContextCompat.getColor(this, R.color.black));
        userMessage.setBackgroundColor(ContextCompat.getColor(this, R.color.secondaryGreen));
        botMessage.setTextColor(ContextCompat.getColor(this, R.color.black));
        botMessage.setBackgroundColor(ContextCompat.getColor(this, R.color.linkColor));

        userMessageLayout.setPadding(20, 20, 20, 20);

        botMessageLayout.setPadding(20, 20, 20, 20);


        userMessageLayout.setOrientation(LinearLayout.HORIZONTAL);
        botMessageLayout.setOrientation(LinearLayout.HORIZONTAL);

        userMessageLayout.setGravity(Gravity.END);
        botMessageLayout.setGravity(Gravity.START);

        LinearLayout.LayoutParams messageParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );

        userMessageLayout.setLayoutParams(messageParams);
        botMessageLayout.setLayoutParams(messageParams);

        // Add views to user and bot layouts
        userMessageLayout.addView(userMessage);
        userMessageLayout.addView(profileCardView);

        botMessageLayout.addView(profileBotView);
        botMessageLayout.addView(botMessage);

        // Add layouts to the main message layout
        messageLyt.addView(userMessageLayout);
        messageLyt.addView(botMessageLayout);

        inputMessageText.setText("");

    }

    public void clearChat(String studentId){
        db.deleteBotMessages(studentId);
        messageLyt.removeAllViews();
        inputMessageText.setText("");
    }
}