package com.example.seektradeapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    // Database Info
    private static final String DATABASE_NAME = "Seek&TradeDatabase";
    private static final int DATABASE_VERSION = 1;

    // Table Names
    private static final String TABLE_POSTS = "posts";
    private static final String TABLE_USERS = "users";

    // Post Table Columns
    private static final String KEY_POST_ID = "postId";
    private static final String KEY_POST_USER_EMAIL_FK = "email";
    private static final String KEY_POST_CATEGORY = "category";
    private static final String KEY_POST_TITLE = "title";
    private static final String KEY_POST_DESCRIPTION = "description";
    private static final String KEY_POST_PRICE = "price";
    private static final String KEY_POST_POSTDATE = "postDate";
    private static final String KEY_POST_ADDRESS = "address";
    private static final String KEY_POST_ZIPCODE = "zipCode";
    private static final String KEY_POST_PHOTO = "photo";


    // User Table Columns
    private static final String KEY_USER_ID = "userId";
    private static final String KEY_USER_FULLNAME = "fullName";
    private static final String KEY_USER_REGISTRATIONDATE = "registrationDate";
    private static final String KEY_USER_EMAIL = "email";
    private static final String TAG = "Database_Helper";

//    private static final String KEY_PHOTO_ID = "photoId";
//    private static final String KEY_PHOTO_IMG = "photoImg";
//    private static final String KEY_PHOTO_POST_ID_FK = "postId";


    // Called when the database connection is being configured.
    // Configure database settings for things like foreign key support, write-ahead logging, etc.
    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    // Called when the database is created for the FIRST time.
    // If a database already exists on disk with the same DATABASE_NAME, this method will NOT be called.
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_POSTS_TABLE = "CREATE TABLE " + TABLE_POSTS +
                "(" +
                KEY_POST_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + // Define a primary key
                KEY_POST_USER_EMAIL_FK + " STRING REFERENCES " + TABLE_USERS + "(" + KEY_USER_EMAIL + ")," + // Define a foreign key
                KEY_POST_CATEGORY + " VARCHAR(30)," +
                KEY_POST_TITLE + " VARCHAR(30)," +
                KEY_POST_DESCRIPTION + " VARCHAR(100)," +
                KEY_POST_PRICE + " DOUBLE," +
                KEY_POST_POSTDATE + " DATE," +
                KEY_POST_ADDRESS + " VARCHAR(50)," +
                KEY_POST_ZIPCODE + " VARCHAR(20)," +
                KEY_POST_PHOTO + " VARCHAR(40)" +
                ")";


        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS +
                "(" +
                KEY_USER_ID + " VARCHAR(50)," +
                KEY_USER_FULLNAME + " VARCHAR(30)," +
                KEY_USER_REGISTRATIONDATE + " DATE," +
                KEY_USER_EMAIL + " VARCHAR(30) PRIMARY KEY" +
                ")";

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_POSTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);

        db.execSQL(CREATE_USERS_TABLE);
        db.execSQL(CREATE_POSTS_TABLE);
    }

    // Called when the database needs to be upgraded.
    // This method will only be called if a database already exists on disk with the same DATABASE_NAME,
    // but the DATABASE_VERSION is different than the version of the database that exists on disk.
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            // Simplest implementation is to drop all old tables and recreate them
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_POSTS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);

            onCreate(db);
        }
    }

    public void resetDB() {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_POSTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + "photos");

        // Simplest implementation is to drop all old tables and recreate them
        onCreate(db);
    }

//    // In any activity just pass the context and use the singleton method
//    DatabaseHelper helper = DatabaseHelper.getInstance(this);

    private static DatabaseHelper sInstance;

    // ...

    public static synchronized DatabaseHelper getInstance(Context context) {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null) {
            sInstance = new DatabaseHelper(context.getApplicationContext());
        }
        return sInstance;
    }


    /**
     * Constructor should be private to prevent direct instantiation.
     * Make a call to the static method "getInstance()" instead.
     */
    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Insert a post into the database
    public void addPost(Post post, String userEmail) {
        // Create and/or open the database for writing
        SQLiteDatabase db = getWritableDatabase();

        // It's a good idea to wrap our insert in a transaction. This helps with performance and ensures
        // consistency of the database.
        db.beginTransaction();
        try {
            // The user might already exist in the database (i.e. the same user created multiple posts).
//            long userId = addOrUpdateUser(post.user);

            ContentValues values = new ContentValues();
            values.put(KEY_POST_USER_EMAIL_FK, userEmail);
            values.put(KEY_POST_CATEGORY, post.getCategory());
            values.put(KEY_POST_TITLE, post.getTitle());
            values.put(KEY_POST_DESCRIPTION, post.getDescription());
            values.put(KEY_POST_PRICE, post.getPrice());
            values.put(KEY_POST_POSTDATE, post.getPostDate());
            values.put(KEY_POST_ADDRESS, post.getAddress());
            values.put(KEY_POST_ZIPCODE, post.getZipCode());
            values.put(KEY_POST_PHOTO, post.getPhoto());

            // Notice how we haven't specified the primary key. SQLite auto increments the primary key column.
            db.insertOrThrow(TABLE_POSTS, null, values);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to add post to database");
        } finally {
            db.endTransaction();
        }
    }

    // Insert or update a user in the database
    // Since SQLite doesn't support "upsert" we need to fall back on an attempt to UPDATE (in case the
    // user already exists) optionally followed by an INSERT (in case the user does not already exist).
    // Unfortunately, there is a bug with the insertOnConflict method
    // (https://code.google.com/p/android/issues/detail?id=13045) so we need to fall back to the more
    // verbose option of querying for the user's primary key if we did an update.

    public long updatePost(Post post) {
        // The database connection is cached so it's not expensive to call getWriteableDatabase() multiple times.
        SQLiteDatabase db = getWritableDatabase();

        ContentValues newValues = new ContentValues();
        newValues.put(KEY_POST_USER_EMAIL_FK, post.getUserEmail());
        newValues.put(KEY_POST_CATEGORY, post.getCategory());
        newValues.put(KEY_POST_TITLE, post.getTitle());
        newValues.put(KEY_POST_DESCRIPTION, post.getDescription());
        newValues.put(KEY_POST_PRICE, post.getPrice());
        newValues.put(KEY_POST_POSTDATE, post.getPostDate());
        newValues.put(KEY_POST_ADDRESS, post.getAddress());
        newValues.put(KEY_POST_ZIPCODE, post.getZipCode());
        newValues.put(KEY_POST_PHOTO, post.getPhoto());

        db.update(TABLE_POSTS, newValues, " postId = ?", new String[]{post.getPostId() + ""});

        return post.getPostId();
}


    public long addOrUpdateUser(User user) {
        // The database connection is cached so it's not expensive to call getWriteableDatabase() multiple times.
        SQLiteDatabase db = getWritableDatabase();
        long userId = -1;

        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_USER_ID, user.getUserId());
            values.put(KEY_USER_FULLNAME, user.getFullName());
            values.put(KEY_USER_REGISTRATIONDATE, user.getRegistrationDate());
            values.put(KEY_USER_EMAIL, user.getEmail());

            // First try to update the user in case the user already exists in the database
            // This assumes userEmail are unique
            int rows = db.update(TABLE_USERS, values, KEY_USER_EMAIL + "= ?", new String[]{user.getEmail()});

            // Check if update succeeded
            if (rows == 1) {
                // Get the primary key of the user we just updated
                String usersSelectQuery = String.format("SELECT %s FROM %s WHERE %s = ?",
                        KEY_USER_ID, TABLE_USERS, KEY_USER_EMAIL);
                Cursor cursor = db.rawQuery(usersSelectQuery, new String[]{String.valueOf(user.getEmail())});
                try {
                    if (cursor.moveToFirst()) {
                        userId = cursor.getInt(0);
                        db.setTransactionSuccessful();
                    }
                } finally {
                    if (cursor != null && !cursor.isClosed()) {
                        cursor.close();
                    }
                }
            } else {
                // user with this userName did not already exist, so insert new user
                userId = db.insertOrThrow(TABLE_USERS, null, values);
                db.setTransactionSuccessful();
            }
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to add or update user");
        } finally {
            db.endTransaction();
        }
        return userId;
    }

    public List<Post> getAllPosts() {
        List<Post> posts = new ArrayList<>();

        // SELECT * FROM POSTS
        // LEFT OUTER JOIN USERS
        // ON POSTS.KEY_POST_USER_ID_FK = USERS.KEY_USER_ID
        String POSTS_SELECT_QUERY =
                String.format("SELECT * FROM %s;",
                        TABLE_POSTS);

        // "getReadableDatabase()" and "getWriteableDatabase()" return the same object (except under low
        // disk space scenarios)
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(POSTS_SELECT_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    Post newPost = new Post();
                    newPost.setPostId(cursor.getInt(cursor.getColumnIndex(KEY_POST_ID)));
                    newPost.setCategory(cursor.getString(cursor.getColumnIndex(KEY_POST_CATEGORY)));
                    newPost.setTitle(cursor.getString(cursor.getColumnIndex(KEY_POST_TITLE)));
                    newPost.setDescription(cursor.getString(cursor.getColumnIndex(KEY_POST_DESCRIPTION)));
                    newPost.setPrice(cursor.getDouble(cursor.getColumnIndex(KEY_POST_PRICE)));
                    newPost.setUserEmail(cursor.getString(cursor.getColumnIndex(KEY_POST_USER_EMAIL_FK)));
                    newPost.setPostDate(cursor.getString(cursor.getColumnIndex(KEY_POST_POSTDATE)));
                    newPost.setAddress(cursor.getString(cursor.getColumnIndex(KEY_POST_ADDRESS)));
                    newPost.setZipCode(cursor.getString(cursor.getColumnIndex(KEY_POST_ZIPCODE)));
                    newPost.setPhoto(cursor.getString(cursor.getColumnIndex(KEY_POST_PHOTO)));

                    posts.add(newPost);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to get posts from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return posts;
    }

    public Post getPostById(int postId) {
        Post post = new Post();
        // SELECT * FROM POSTS
        // LEFT OUTER JOIN USERS
        // ON POSTS.KEY_POST_USER_ID_FK = USERS.KEY_USER_ID
        String POSTS_SELECT_QUERY =
                String.format("SELECT * FROM %s WHERE %s = %s;",
                        TABLE_POSTS,
                        KEY_POST_ID, postId);

        // "getReadableDatabase()" and "getWriteableDatabase()" return the same object (except under low
        // disk space scenarios)
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(POSTS_SELECT_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                post.setPostId(cursor.getInt(cursor.getColumnIndex(KEY_POST_ID)));
                post.setCategory(cursor.getString(cursor.getColumnIndex(KEY_POST_CATEGORY)));
                post.setTitle(cursor.getString(cursor.getColumnIndex(KEY_POST_TITLE)));
                post.setDescription(cursor.getString(cursor.getColumnIndex(KEY_POST_DESCRIPTION)));
                post.setPrice(cursor.getDouble(cursor.getColumnIndex(KEY_POST_PRICE)));
                post.setUserEmail(cursor.getString(cursor.getColumnIndex(KEY_POST_USER_EMAIL_FK)));
                post.setPostDate(cursor.getString(cursor.getColumnIndex(KEY_POST_POSTDATE)));
                post.setAddress(cursor.getString(cursor.getColumnIndex(KEY_POST_ADDRESS)));
                post.setZipCode(cursor.getString(cursor.getColumnIndex(KEY_POST_ZIPCODE)));
                post.setPhoto(cursor.getString(cursor.getColumnIndex(KEY_POST_PHOTO)));
            }
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to get posts from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return post;
    }


    public List<Post> getPostsByUserEmail(String userEmail) {
        List<Post> posts = new ArrayList<>();

        String POSTS_SELECT_QUERY =
                String.format("SELECT * FROM %s WHERE %s = '%s';",
                        TABLE_POSTS,
                        KEY_POST_USER_EMAIL_FK, userEmail);

        // "getReadableDatabase()" and "getWriteableDatabase()" return the same object (except under low
        // disk space scenarios)
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(POSTS_SELECT_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    Post newPost = new Post();
                    newPost.setPostId(cursor.getInt(cursor.getColumnIndex(KEY_POST_ID)));
                    newPost.setCategory(cursor.getString(cursor.getColumnIndex(KEY_POST_CATEGORY)));
                    newPost.setTitle(cursor.getString(cursor.getColumnIndex(KEY_POST_TITLE)));
                    newPost.setDescription(cursor.getString(cursor.getColumnIndex(KEY_POST_DESCRIPTION)));
                    newPost.setPrice(cursor.getDouble(cursor.getColumnIndex(KEY_POST_PRICE)));
                    newPost.setUserEmail(cursor.getString(cursor.getColumnIndex(KEY_POST_USER_EMAIL_FK)));
                    newPost.setPostDate(cursor.getString(cursor.getColumnIndex(KEY_POST_POSTDATE)));
                    newPost.setAddress(cursor.getString(cursor.getColumnIndex(KEY_POST_ADDRESS)));
                    newPost.setZipCode(cursor.getString(cursor.getColumnIndex(KEY_POST_ZIPCODE)));
                    newPost.setPhoto(cursor.getString(cursor.getColumnIndex(KEY_POST_PHOTO)));

                    posts.add(newPost);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to get posts from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return posts;
    }

    public User getUserByEmail(String userEmail) {
        User user = new User();

        // SELECT * FROM POSTS
        // LEFT OUTER JOIN USERS
        // ON POSTS.KEY_POST_USER_ID_FK = USERS.KEY_USER_ID
        String POSTS_SELECT_QUERY =
                String.format("SELECT * FROM %s WHERE %s = '%s'",
                        TABLE_USERS,
                        KEY_USER_EMAIL, userEmail);

        // "getReadableDatabase()" and "getWriteableDatabase()" return the same object (except under low
        // disk space scenarios)
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(POSTS_SELECT_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                user.setUserId(cursor.getString(cursor.getColumnIndex(KEY_USER_ID)));
                user.setFullName(cursor.getString(cursor.getColumnIndex(KEY_USER_FULLNAME)));
                user.setRegistrationDate(cursor.getString(cursor.getColumnIndex(KEY_USER_REGISTRATIONDATE)));
                user.setEmail(cursor.getString(cursor.getColumnIndex(KEY_USER_EMAIL)));
            }
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to get users from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return user;
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();

        // SELECT * FROM POSTS
        // LEFT OUTER JOIN USERS
        // ON POSTS.KEY_POST_USER_ID_FK = USERS.KEY_USER_ID
        String POSTS_SELECT_QUERY =
                String.format("SELECT * FROM %s;",
                        TABLE_USERS);

        // "getReadableDatabase()" and "getWriteableDatabase()" return the same object (except under low
        // disk space scenarios)
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(POSTS_SELECT_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    User newUser = new User();
                    newUser.setUserId(cursor.getString(cursor.getColumnIndex(KEY_USER_ID)));
                    newUser.setFullName(cursor.getString(cursor.getColumnIndex(KEY_USER_FULLNAME)));
                    newUser.setRegistrationDate(cursor.getString(cursor.getColumnIndex(KEY_USER_REGISTRATIONDATE)));
                    newUser.setEmail(cursor.getString(cursor.getColumnIndex(KEY_USER_EMAIL)));

                    users.add(newUser);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to get users from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return users;
    }

    public void deleteAllPostsAndUsers() {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            // Order of deletions is important when foreign key relationships exist.
            db.delete(TABLE_POSTS, null, null);
            db.delete(TABLE_USERS, null, null);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to delete all posts and users");
        } finally {
            db.endTransaction();
        }
    }

}
