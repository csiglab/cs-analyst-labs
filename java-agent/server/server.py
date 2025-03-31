from http.server import BaseHTTPRequestHandler, HTTPServer
import json
import sqlite3
from datetime import datetime

# Create SQLite database and table with the new schema
def init_db():
    conn = sqlite3.connect('data.db')
    c = conn.cursor()
    c.execute('''
        CREATE TABLE IF NOT EXISTS logs (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            timestamp INTEGER,
            message TEXT,
            exception TEXT,
            data TEXT
        )
    ''')
    conn.commit()
    conn.close()

class SimpleAPIHandler(BaseHTTPRequestHandler):
    def _send_response(self, status_code, message):
        self.send_response(status_code)
        self.send_header("Content-type", "application/json")
        self.end_headers()
        self.wfile.write(json.dumps(message).encode())

    def do_GET(self):
        response = {"message": "Welcome to the API!"}
        self._send_response(200, response)

    def do_POST(self):
        # Handle POST request
        if self.path == '/api/logs':
            content_length = int(self.headers['Content-Length'])
            post_data = self.rfile.read(content_length)
            data = json.loads(post_data.decode())

            print(data)

            # Extract the fields from the JSON data
            message = data.get('message', None)
            exception = data.get('exception', None)
            timestamp =  data.get("timestamp", None)

            self.store_in_db(timestamp, message, exception, json.dumps(data) )
            response = {"message": "Data received and stored in DB"}
            self._send_response(200, response)

        else:
            self._send_response(404, {"error": "Not Found"})

    def store_in_db(self, timestamp, message, exception, json_data):
        # Insert data into the SQLite database
        conn = sqlite3.connect('data.db')
        c = conn.cursor()
        c.execute('''
            INSERT INTO logs (timestamp, message, exception, data) 
            VALUES (?, ?, ?, ?)
        ''', (timestamp, message, exception, json_data))
        conn.commit()
        conn.close()

def run(server_class=HTTPServer, handler_class=SimpleAPIHandler, port=8080):
    init_db()  # Initialize the database and create the table if needed
    server_address = ('', port)
    httpd = server_class(server_address, handler_class)
    print(f"Starting server on port {port}...")
    httpd.serve_forever()

if __name__ == "__main__":
    run()
