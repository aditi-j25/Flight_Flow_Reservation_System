--USER TABLE 
CREATE TABLE IF NOT EXISTS users (
    user_id INTEGER PRIMARY KEY AUTOINCREMENT, 
    email TEXT NOT NULL UNIQUE,
    user_password TEXT NOT NULL,
    first_name TEXT NOT NULL,
    last_name TEXT NOT NULL,
    address TEXT,
    phone TEXT,
    role TEXT NOT NULL CHECK(role IN ('ADMIN', 'CUSTOMER', 'AGENT')),
    promotions_opt_in BOOLEAN NOT NULL DEFAULT 0,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);


-- FLIGHTS TABLE
CREATE TABLE IF NOT EXISTS flights (
    flight_id INTEGER PRIMARY KEY AUTOINCREMENT,
    flight_number TEXT NOT NULL UNIQUE,
    departure_city TEXT NOT NULL,
    arrival_city TEXT NOT NULL,
    departure_time DATETIME NOT NULL,
    arrival_time DATETIME NOT NULL,
    total_seats INTEGER NOT NULL,
    available_seats INTEGER NOT NULL,
    price REAL NOT NULL
);