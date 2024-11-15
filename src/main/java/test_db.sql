-- เข้าสู่ psql shell หรือใช้ pgAdmin
CREATE DATABASE test_db;

-- สร้างผู้ใช้สองคน: user1 และ user2
CREATE USER boat_tester WITH PASSWORD 'boatandgift';
CREATE USER gift_tester WITH PASSWORD 'giftandboat';

-- ให้สิทธิ์แก่ผู้ใช้ในฐานข้อมูล
GRANT ALL PRIVILEGES ON DATABASE test_db TO boat_tester;
GRANT ALL PRIVILEGES ON DATABASE test_db TO gift_tester;