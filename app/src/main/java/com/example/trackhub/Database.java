    package com.example.trackhub;
    
    import android.content.ContentValues;
    import android.content.Context;
    import android.database.Cursor;
    import android.database.sqlite.SQLiteDatabase;
    import android.database.sqlite.SQLiteOpenHelper;
    import android.util.Log;
    
    import java.text.SimpleDateFormat;
    import java.util.Date;
    import java.util.Locale;
    
    public class Database extends SQLiteOpenHelper {
        private static final String databaseName = "TBDB.db";
        private static final int dbVersion = 15;
    
        public static final String tbStudents = "students";
        public static final String columnStudentID = "id";
        public static final String columnFirstName = "firstName";
        public static final String columnLastName = "lastName";
        public static final String columnEmail = "email";
        public static final String columnPassword = "password";
        public static final String columnContacts = "contact";
        public static final String columnImage = "image";
    
        public static final String tbLostItems = "lost_items";
        public static final String columnLiID = "LostID";
        public static final String columnLiSdID = "Student_LostID";
        public static final String columnLiDate = "LostDate";
        public static final String columnLiDesc = "Description";
        public static final String columnLiImage = "Image";
    
        public static final String tbFoundItems = "found_items";
        public static final String columnFiID = "FoundID";
        public static final String columnFiSdID = "Student_FoundID";
        public static final String columnFiDate = "LostDate";
        public static final String columnFiDesc = "Description";
        public static final String columnFiImage = "Image";
    
        public static final String tbComments = "comments";
        public static final String columnPostCommentID = "Post_Comment_ID";
        public static final String columnCommentID = "Comment_ID";
        public static final String columnCommentStudentID = "Student_ID";
        public static final String columnCommentDate = "date";
        public static final String columnComment = "message";
        public static final String columnCommentSymbol = "symbol";
    
        public static final String tbBot = "bot";
        public static final String columnBotID = "bot_id";
        public static final String columnBotStudentID = "Student_ID";
        public static final String columnBotStudentMessage = "Student_Message";
        public static final String columnBotMessage = "Bot_Message";

        public static final String tbNotif = "notification";
        public static final String columnNotifyStudentID = "student_notify_id";
        public static final String columnNotifyID = "notify_id";
        public static final String columnNotifyCommentID = "notification_comment_id";
        public static final String columnNotifyPostID = "notification_post_id";
        public static final String columnNotifySymbol = "notification_symbol";
    
        public Database(Context c){
            super(c, databaseName, null, dbVersion);
        }
    
        @Override
        public void onCreate(SQLiteDatabase db) {
            String CREATE_STUDENTS_TABLE = "CREATE TABLE " + tbStudents + "("
                    + columnStudentID + " INTEGER PRIMARY KEY,"
                    + columnFirstName + " TEXT,"
                    + columnLastName + " TEXT,"
                    + columnEmail + " TEXT,"
                    + columnPassword + " TEXT,"
                    + columnContacts + " TEXT,"
                    + columnImage + " TEXT" +
                    ")";
    
            String CREATE_LOST_ITEMS_TABLE = "CREATE TABLE " + tbLostItems + "("
                    + columnLiID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + columnLiSdID + " INTEGER, "
                    + columnLiDesc + " TEXT,"
                    + columnLiDate + " DATETIME, "
                    + columnLiImage + " TEXT,"
                    + "FOREIGN KEY (" + columnLiSdID + ") REFERENCES " + tbStudents + "(" + columnStudentID + ")"
                    + ")";
    
            String CREATE_FOUND_ITEMS_TABLE = "CREATE TABLE " + tbFoundItems + "("
                    + columnFiID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + columnFiSdID + " INTEGER, "
                    + columnFiDesc + " TEXT,"
                    + columnFiDate + " DATETIME, "
                    + columnFiImage + " TEXT,"
                    + "FOREIGN KEY (" + columnFiSdID + ") REFERENCES " + tbStudents + "(" + columnStudentID + ")"
                    + ")";
    
            String CREATE_BOT_TABLE = "CREATE TABLE " + tbBot + "("
                    + columnBotID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + columnBotStudentID + " INTEGER, "
                    + columnBotStudentMessage + " TEXT,"
                    + columnBotMessage + " TEXT,"
                    + "FOREIGN KEY (" + columnBotStudentID + ") REFERENCES " + tbStudents + "(" + columnStudentID + ")"
                    + ")";
    
            String CREATE_FOUND_COMMENTS_TABLE = "CREATE TABLE " + tbComments + "("
                    + columnPostCommentID + " INTEGER," //based on the post's id
                    + columnCommentID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + columnCommentStudentID + " INTEGER," //who commented?
                    + columnCommentDate + " DATETIME, "
                    + columnComment + " TEXT, "
                    + columnCommentSymbol + " TEXT, "
                    + "FOREIGN KEY (" + columnPostCommentID + ") REFERENCES " + tbFoundItems + "(" + columnFiID + "),"
                    + "FOREIGN KEY (" + columnPostCommentID + ") REFERENCES " + tbLostItems + "(" + columnLiID + "),"
                    + "FOREIGN KEY (" + columnCommentStudentID + ") REFERENCES " + tbStudents + "(" + columnStudentID + ")"
                    + ")";

            String CREATE_NOTIFICATION_TABLE = "CREATE TABLE " + tbNotif + "("
                    + columnNotifyID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + columnNotifyPostID + " INTEGER, "
                    + columnNotifyStudentID + " INTEGER, "
                    + columnNotifyCommentID + " INTEGER, "
                    + columnNotifySymbol + " TEXT, "
                    + "FOREIGN KEY (" + columnNotifyCommentID + ") REFERENCES " + tbComments + "(" + columnCommentID + "),"
                    + "FOREIGN KEY (" + columnNotifyStudentID + ") REFERENCES " + tbStudents + "(" + columnStudentID + ")"
                    + ")";
    
            db.execSQL(CREATE_STUDENTS_TABLE);
            db.execSQL(CREATE_LOST_ITEMS_TABLE);
            db.execSQL(CREATE_FOUND_ITEMS_TABLE);
            db.execSQL(CREATE_BOT_TABLE);
            db.execSQL(CREATE_FOUND_COMMENTS_TABLE);
            db.execSQL(CREATE_NOTIFICATION_TABLE);
    
        }
    
        public boolean login(String id, String password){
            SQLiteDatabase db = this.getReadableDatabase();
            String query = "SELECT * FROM " + tbStudents + " WHERE " + columnStudentID + " = ? AND " + columnPassword + " = ?";
            Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(id), password});
    
            boolean isLoggedIn = false;
            if (cursor != null) {
                isLoggedIn = cursor.getCount() > 0;
                cursor.close();
            }
            return isLoggedIn;
        }
    
        public boolean checkIdRegistered(int id){
            SQLiteDatabase db = this.getReadableDatabase();
            String query = "SELECT * FROM " + tbStudents + " WHERE " + columnStudentID + " = ?";
            Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(id)});
    
            boolean isIdRegistered = false;
            if (cursor != null) {
                isIdRegistered = cursor.getCount() > 0;
                cursor.close();
            }
            return isIdRegistered;
        }
    
        public boolean checkEmailRegistered(String email){
            SQLiteDatabase db = this.getReadableDatabase();
            String query = "SELECT * FROM " + tbStudents + " WHERE " + columnEmail + " = ? ";
            Cursor cursor = db.rawQuery(query, new String[]{email});
    
            boolean isEmailRegistered = false;
            if (cursor != null) {
                isEmailRegistered = cursor.getCount() > 0;
                cursor.close();
            }
            return isEmailRegistered;
        }
    
        public long insertStudentInfo(int id, String firstName, String lastName, String email, String password, String contact){
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(columnStudentID, id);
            values.put(columnFirstName, firstName);
            values.put(columnLastName, lastName);
            values.put(columnEmail, email);
            values.put(columnPassword, password);
            values.put(columnContacts, contact);
    
            Log.d("DBHelper", "Inserting student info: ID=" + id + ", FirstName=" + firstName +
                    ", LastName=" + lastName + ", Email=" + email + ", Contact=" + contact);
    
            long result = db.insert(tbStudents, null, values);
            return result;
        }
    
        public long insertImage(int id, String image){
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(columnImage, image);
    
            Log.d("DBHelper", "Updating image for student ID=" + id + ", Image=" + image);
    
            int rowsUpdated = db.update(tbStudents, values, columnStudentID + " = ?", new String[]{String.valueOf(id)});
    
            if (rowsUpdated > 0) {
                Log.d("DBHelper", "Image updated successfully for ID=" + id);
            } else {
                Log.d("DBHelper", "No matching student found for ID=" + id);
            }
    
            return rowsUpdated;
        }
    
        public Cursor getStudentInfo(String id){
            SQLiteDatabase db = this.getReadableDatabase();
            String query = "SELECT ( " + columnFirstName + " || ' ' || " + columnLastName +  " ) AS fullname, "
                    + columnStudentID + ", "
                    + columnEmail + ", "
                    + columnContacts + ", "
                    + columnImage +
                    " FROM " + tbStudents + " WHERE " + columnStudentID + " = ?";
    
            return db.rawQuery(query, new String[]{id});
        }
    
        public long insertLostItemDetail(String id,String description, String image){
            SQLiteDatabase db = this.getWritableDatabase();
    
            SimpleDateFormat getDateTime = new SimpleDateFormat("MMMM dd, yyyy hh:mm a", Locale.getDefault());
            ContentValues values = new ContentValues();
            values.put(columnLiSdID, id);
            values.put(columnLiDesc, description);
            values.put(columnLiImage, image);
            values.put(columnLiDate, getDateTime.format(new Date()));
    
            Log.d("DBHelper", "Inserting lost items info: description=" + description + ", image=" + image +
                    ", date=" + getDateTime.format(new Date()) + ", id" + id);
    
            long result = db.insert(tbLostItems, null, values);
            return result;
        }
    
        public Cursor getAllLostItemDetails() {
            SQLiteDatabase db = this.getReadableDatabase();
            String query = "SELECT li.*, (s." + columnFirstName + " || ' ' || s." + columnLastName + ") AS fullName, s." + columnImage +
                    " FROM " + tbLostItems + " AS li " +
                    "LEFT JOIN " + tbStudents + " AS s " +
                    "ON li." + columnLiSdID + " = s." + columnStudentID
                    + " ORDER BY " + columnLiID + " DESC";
    
            return db.rawQuery(query, null);
        }
    
        public Cursor getAllLostItemDetails(String id) {
            SQLiteDatabase db = this.getReadableDatabase();
    
            String query = "SELECT li.*, (s." + columnFirstName + " || ' ' || s." + columnLastName + ") AS fullName, s." + columnImage +
                    " FROM " + tbLostItems + " AS li " +
                    "LEFT JOIN " + tbStudents + " AS s " +
                    "ON li." + columnLiSdID + " = s." + columnStudentID
                    + " WHERE " + columnStudentID + " = ?"
                    + " ORDER BY " + columnLiID + " DESC";
    
            return db.rawQuery(query, new String[]{id});
        }

        public Cursor getAllLostItemDetails(String id, String itemId) {
            SQLiteDatabase db = this.getReadableDatabase();

            String query = "SELECT li.*, (s." + columnFirstName + " || ' ' || s." + columnLastName + ") AS fullName, s." + columnImage +
                    " FROM " + tbLostItems + " AS li " +
                    "LEFT JOIN " + tbStudents + " AS s " +
                    "ON li." + columnLiSdID + " = s." + columnStudentID
                    + " WHERE " + columnStudentID + " = ? " + "AND " + columnLiID + " = ?"
                    + " ORDER BY " + columnLiID + " DESC";

            return db.rawQuery(query, new String[]{id, itemId});
        }
    
        public long insertFoundItemDetail(String id, String description, String image){
            SQLiteDatabase db = this.getWritableDatabase();
    
            SimpleDateFormat getDateTime = new SimpleDateFormat("MMMM dd, yyyy hh:mm a", Locale.getDefault());
            ContentValues values = new ContentValues();
            values.put(columnFiSdID, id);
            values.put(columnFiDesc, description);
            values.put(columnFiImage, image);
            values.put(columnFiDate, getDateTime.format(new Date()));
    
            long result = db.insert(tbFoundItems, null, values);
            return result;
        }
    
        public Cursor getAllFoundItemDetails() {
            SQLiteDatabase db = this.getReadableDatabase();
    
            String query = "SELECT fi.*, (s." + columnFirstName + " || ' ' || s." + columnLastName + ") AS fullName, s." + columnImage +
                    " FROM " + tbFoundItems + " AS fi " +
                    "LEFT JOIN " + tbStudents + " AS s " +
                    "ON fi." + columnFiSdID + " = s." + columnStudentID
                    + " ORDER BY " + columnFiID + " DESC";
    
            return db.rawQuery(query, null);
        }
    
        public Cursor getAllFoundItemDetails(String id) {
            SQLiteDatabase db = this.getReadableDatabase();
    
            String query = "SELECT fi.*, (s." + columnFirstName + " || ' ' || s." + columnLastName + ") AS fullName, s." + columnImage +
                    " FROM " + tbFoundItems + " AS fi " +
                    "LEFT JOIN " + tbStudents + " AS s " +
                    "ON fi." + columnFiSdID + " = s." + columnStudentID
                    + " WHERE " + columnStudentID + " = ?"
                    + " ORDER BY " + columnFiID + " DESC";
    
            return db.rawQuery(query, new String[]{id});
        }

        public Cursor getAllFoundItemDetails(String id, String itemId) {
            SQLiteDatabase db = this.getReadableDatabase();

            String query = "SELECT fi.*, (s." + columnFirstName + " || ' ' || s." + columnLastName + ") AS fullName, s." + columnImage +
                    " FROM " + tbFoundItems + " AS fi " +
                    "LEFT JOIN " + tbStudents + " AS s " +
                    "ON fi." + columnFiSdID + " = s." + columnStudentID
                    + " WHERE " + columnStudentID + " = ? " + "AND " + columnFiID + " = ?"
                    + " ORDER BY " + columnFiID + " DESC";

            return db.rawQuery(query, new String[]{id, itemId});
        }
    
        public long insertBotMessage(String studentId, String studentMessage, String botMessage){
            SQLiteDatabase db = this.getWritableDatabase();
    
            ContentValues values = new ContentValues();
            values.put(columnBotStudentID, studentId);
            values.put(columnBotStudentMessage, studentMessage);
            values.put(columnBotMessage, botMessage);
    
    
            //Log.d("TestMessage", "Student ID: " + studentId + " Student Message: " + studentMessage + " Bot Message: " + botMessage);
    
            long result = db.insert(tbBot, null, values);
            return result;
        }
    
        public long insertComment(String studentId, String itemId, String message, String symbol){
            SQLiteDatabase db = this.getWritableDatabase();
    
            SimpleDateFormat getDateTime = new SimpleDateFormat("MMMM dd, yyyy hh:mm a", Locale.getDefault());
            ContentValues values = new ContentValues();
            values.put(columnPostCommentID, itemId);
            values.put(columnCommentStudentID, studentId);
            values.put(columnComment, message);
            values.put(columnCommentSymbol, symbol);
            values.put(columnCommentDate, getDateTime.format(new Date()));
    
            //Log.d("TestComment", "Student ID: " + studentId + " Item ID: " + itemId + " Comment: " + message + " Date: " + getDateTime.format(new Date()) + " Symbol: " + symbol);
    
            long result = db.insert(tbComments, null, values);
            return result;
        }

        public long insertNotificationComment(String studentId, String commentId, String mainStudentId, String symbol){
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(columnNotifyStudentID, studentId);
            values.put(columnNotifyCommentID, commentId);
            values.put(columnNotifyPostID, mainStudentId);
            values.put(columnNotifySymbol, symbol);

            Log.d("TestComment", "Student ID: " + studentId + " comment ID: " + commentId + " main: " + mainStudentId + " symbol: " + symbol);


            long result = db.insert(tbNotif, null, values);
            return result;
        }
    
        public Cursor getBotMessage(String studentId){
            SQLiteDatabase db = this.getReadableDatabase();
    
            String query = "SELECT b.*, (s." + columnFirstName + " || ' ' || s." + columnLastName + ") AS fullname, s." + columnImage +
                    " FROM " + tbBot + " AS b " +
                    "LEFT JOIN " + tbStudents + " AS s " +
                    "ON b." + columnBotStudentID + " = s." + columnStudentID +
                    " WHERE b." + columnBotStudentID + " = ?"
                    + " ORDER BY " + columnBotID + " ASC";
    
            return db.rawQuery(query, new String[]{studentId});
        }

        public Cursor getNotificationComment(String studentId){
            SQLiteDatabase db = this.getReadableDatabase();

            String query = "SELECT n.*, c.* , (s." + columnFirstName + " || ' ' || s." + columnLastName + ") AS fullname, s." + columnImage +
                    " FROM " + tbNotif + " AS n" +
                    " LEFT JOIN " + tbComments + " AS c " + "ON n." + columnNotifyCommentID + "= c." + columnCommentID +
                    " LEFT JOIN " + tbStudents + " AS s " + "ON c." + columnCommentStudentID + " = s." + columnStudentID +
                    " WHERE n." + columnNotifyPostID + " = ? " +
                    " ORDER BY " + columnNotifyID + " DESC";

            return db.rawQuery(query, new String[]{studentId});
        }

        public Cursor getNotificationComment(){
            SQLiteDatabase db = this.getReadableDatabase();

            String query = "SELECT *" +
                    " FROM " + tbNotif;

            return db.rawQuery(query, null);
        }
    
        public Cursor getComments(String itemId, String symbol) {
            SQLiteDatabase db = this.getReadableDatabase();
    
            String query = "SELECT c.*, (s." + columnFirstName + " || ' ' || s." + columnLastName + ") AS fullname, s." + columnImage +
                    " FROM " + tbComments + " AS c " +
                    "LEFT JOIN " + tbStudents + " AS s " +
                    "ON c." + columnCommentStudentID + " = s." + columnStudentID +
                    " WHERE " + columnPostCommentID + " = ?" + " AND " + columnCommentSymbol + " = ?"
                    + " ORDER BY " + columnCommentID + " DESC";
    
            return db.rawQuery(query, new String[]{itemId, symbol});
        }

        public int getCommentID(String itemId, String symbol) {
            SQLiteDatabase db = this.getReadableDatabase();
            int commentID = -1;

            String query = "SELECT " + columnCommentID +
                    " FROM " + tbComments  +
                    " WHERE " + columnPostCommentID + " = ?" + " AND " + columnCommentSymbol + " = ?"
                    + " ORDER BY " + columnCommentID + " DESC";

            try (Cursor cursor = db.rawQuery(query, new String[]{itemId, symbol})) {
                if (cursor != null && cursor.moveToFirst()) {
                    commentID = cursor.getInt(cursor.getColumnIndexOrThrow(columnCommentID));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return commentID;
        }
    
        public String getStudentName(String studentId) {
            SQLiteDatabase db = this.getReadableDatabase();
            String query = " SELECT " + columnFirstName + " || ' ' || " + columnLastName + " AS fullName " +
                    " FROM " + tbStudents +
                    " WHERE " + columnStudentID + " = ?";
            Cursor cursor = db.rawQuery(query, new String[]{studentId});
            String name = "";
    
            if (cursor.moveToFirst()) {
                name = cursor.getString(0);
            }
            cursor.close();
            db.close();
            return name;
        }
    
        public String getStudentImage(String studentId) {
            SQLiteDatabase db = this.getReadableDatabase();
            String query = " SELECT " + columnImage +
                    " FROM " + tbStudents +
                    " WHERE " + columnStudentID + " = ?";
            Cursor cursor = db.rawQuery(query, new String[]{studentId});
            String image = "";
    
            if (cursor.moveToFirst()) {
                image = cursor.getString(0);
            }
            cursor.close();
            db.close();
            return image;
        }
    
        public void deleteLostItems(String lostItemId){
            SQLiteDatabase db = this.getWritableDatabase();
            String query = "DELETE FROM " + tbLostItems +
                    " WHERE " + columnLiID + " = ?";
            db.execSQL(query, new String[]{lostItemId});
            db.close();
        }
    
        public void deleteFoundItems(String foundItemId){
            SQLiteDatabase db = this.getWritableDatabase();
            String query = "DELETE FROM " + tbFoundItems +
                    " WHERE " + columnFiID + " = ?";
            db.execSQL(query, new String[]{foundItemId});
            db.close();
        }
    
        public void deleteComments(String id, String symbol){
            SQLiteDatabase db = this.getWritableDatabase();
            String query = "DELETE FROM " + tbComments +
                    " WHERE " + columnPostCommentID + " = ?" + " AND " + columnCommentSymbol + " = ?";
            db.execSQL(query, new String[]{id, symbol});
            db.close();
        }

        public void deleteNotificationComments(String id, String symbol){
            SQLiteDatabase db = this.getWritableDatabase();
            String query = "DELETE FROM " + tbNotif +
                    " WHERE " + columnNotifyPostID + " = ?" + " AND " + columnNotifySymbol + " = ?";
            db.execSQL(query, new String[]{id, symbol});
            db.close();
        }
    
        public void deleteBotMessages(String studentId){
            SQLiteDatabase db = this.getWritableDatabase();
            String query = "DELETE FROM " + tbBot +
                    " WHERE " + columnBotStudentID + " = ?";
            db.execSQL(query, new String[]{studentId});
            db.close();
        }
    
        public void deleteLostItems(){
            SQLiteDatabase db = this.getWritableDatabase();
            db.execSQL("DELETE FROM " + tbLostItems);
        }
    
        public void deleteFoundItems(){
            SQLiteDatabase db = this.getWritableDatabase();
            db.execSQL("DELETE FROM " + tbFoundItems);
        }
    
        public void deleteStudentInfo(){
            SQLiteDatabase db = this.getWritableDatabase();
            db.execSQL("DELETE FROM " + tbStudents);
        }
    
        public void deleteComments(){
            SQLiteDatabase db = this.getWritableDatabase();
            db.execSQL("DELETE FROM " + tbComments);
        }

        public void deleteNotificationComments(){
            SQLiteDatabase db = this.getWritableDatabase();
            db.execSQL("DELETE FROM " + tbNotif);
        }

    
        @Override
        public void onUpgrade(SQLiteDatabase db, int i, int i1) {
            // Drop older tables if they exist
            db.execSQL("DROP TABLE IF EXISTS " + tbStudents);
            db.execSQL("DROP TABLE IF EXISTS " + tbLostItems);
            db.execSQL("DROP TABLE IF EXISTS " + tbBot);
            db.execSQL("DROP TABLE IF EXISTS " + tbNotif);
            db.execSQL("DROP TABLE IF EXISTS " + tbFoundItems);
            db.execSQL("DROP TABLE IF EXISTS " + tbComments);
            // Create tables again
            onCreate(db);
        }
    
    
    
    }
