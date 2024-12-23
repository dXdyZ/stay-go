db = db.getSiblingDB('message_bd'); // Переключаемся на базу данных message_bd

db.createUser({
    user: "app_user",
    pwd: "app_password",
    roles: [
        {
            role: "readWrite",
            db: "message_bd"
        }
    ]
});

print("User and database created successfully!");
