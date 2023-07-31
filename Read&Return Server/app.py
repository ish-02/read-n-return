import hashlib
import threading

from flask import Flask, request, jsonify, abort, url_for
from flask_login import LoginManager, login_user, login_required, current_user, logout_user
from flask_mail import Mail, Message

from database import *

app = Flask(__name__)
app.config["SQLALCHEMY_DATABASE_URI"] = "sqlite:///D:/ubuntu/rrserver/instance/rr.sqlite"
app.secret_key = "abc"

app.config['MAIL_SERVER'] = 'smtp.gmail.com'
app.config['MAIL_PORT'] = 465
app.config['MAIL_USERNAME'] = 'ishwarrules@gmail.com'
app.config['MAIL_PASSWORD'] = 'irgcycpfqmmvfyge'
app.config['MAIL_USE_TLS'] = False
app.config['MAIL_USE_SSL'] = True

mail = Mail(app)
login_manager = LoginManager(app)


@login_manager.user_loader
def load_user(email):
    return Users.get_user(email)


with app.app_context():
    db.init_app(app)
    db.create_all()


@app.route('/')
def hello_world():  # put application's code here
    args: dict = request.args
    if args:
        transaction: str = args['t']
        auth: str = args['auth']
        action: str = args['act']

        transaction: Transactions = Transactions.query.filter_by(id=transaction).first()
        print(hashlib.sha256(bytes(str(transaction.timestamp), encoding='utf-8')).hexdigest())
        print(auth)
        if transaction and hashlib.sha256(bytes(str(transaction.timestamp), encoding='utf-8')).hexdigest() == auth:

            transaction.status = "accepted" if action == '1' else 'rejected'

            if action == '1':
                book: Books = Books.query.filter_by(id=transaction.book_id).first()
                if book:
                    book.gone = True

            db.session.commit()
            return jsonify({"message": "Transaction completed"}), 200

    return 'Hello World!'


@app.route("/register", methods=["POST"])
def register_user():
    if request.method == "POST":
        payload = request.get_json(silent=True)
        if payload and "email" in payload and "name" in payload and "password" in payload:
            if Users.query.filter(Users.email == payload["email"]).first():
                return abort(409)

            user = Users(email=payload["email"], name=payload["name"], password=payload["password"])
            db.session.add(user)
            db.session.commit()

            login_user(user, remember=True)
            return jsonify({"email": user.email, "name": user.name}), 200

    return abort(400)


@app.route("/login", methods=["POST"])
def login():
    if request.method == "POST":
        payload = request.get_json(silent=True)
        print(payload)
        if payload and "email" in payload and "password" in payload:
            user: Users = Users.query.filter(Users.email == payload["email"]).first()
            if user:
                if user.password == payload["password"]:
                    login_user(user, remember=True)
                    return jsonify({"email": user.email, "name": user.name}), 200
                return abort(405)
            return abort(404)

    return abort(400)


@login_required
@app.route("/secure", methods=["GET"])
def secure():
    return jsonify({"email": current_user.email, "name": current_user.name}), 200


@login_required
@app.route("/add-book", methods=["POST"])
def add_book():
    if request.method == "POST":
        payload = request.get_json(silent=True)
        if payload and "name" in payload and "author" in payload and "description" in payload and "genre" in payload:
            book = Books(name=payload["name"], author=payload["author"], description=payload["description"],
                         genre=payload["genre"], owner_email=current_user.email)
            db.session.add(book)
            db.session.commit()
            return jsonify({}), 200
    return abort(400)


@login_required
@app.route("/delete-book/<book_id>")
def delete_book(book_id):
    book = Books.query.filter_by(id=book_id).first()
    if book:
        db.session.delete(book)
        db.session.commit()

        return jsonify({}), 200

    return abort(404)


@login_required
@app.route("/books", methods=["GET", "POST"])
def all_books():
    if request.method == "GET":
        print(Books.query.filter_by(gone=False).all())
        books = list(map(Books.get_dict, Books.query.filter_by(gone=False).all()))
        return jsonify({"books": books}), 200

    if request.method == "POST":
        payload = request.get_json(silent=True)
        if payload and "email" in payload:
            books = Books.query.filter_by(gone=False, owner_email=payload["email"]).all()
            books = list(map(Books.get_dict, books))
            return jsonify({"books": books}), 200

    return abort(400)


@login_required
@app.route("/books/<bookid>", methods=["GET", "POST"])
def one_books(bookid):
    if request.method == "GET":
        book = Books.query.filter_by(id=bookid, gone=False).first()
        print(bookid, book)
        if book:
            have_requested = list(filter(lambda x: x.user_email == current_user.email, book.transactions))
            book_dict = book.get_dict()
            book_dict["requested"] = bool(have_requested)
            return jsonify({"book": book_dict}), 200

    return abort(400)


@login_required
@app.route("/all_orders")
def get_orders():
    transactions = Transactions.query.filter_by(user_email=current_user.email).all()
    pay = {"orders": list(map(Transactions.get_dict, transactions))}
    print(pay)
    return jsonify(pay), 200


def send_mail(book: Books, transaction, recipient):
    message = Message("Hey! I would like to read your book", sender=app.config["MAIL_USERNAME"], recipients=[
        book.owner.email
    ])
    message.body = f"Hey {book.owner.name},\nThis is an automated email from Read&Return. We have received a " \
                   f"request for your book {book.name} by {book.author} from our user identified by email address" \
                   f" as {recipient[1]}. Please have a conversation with the requester via email for the " \
                   f"same.\n\n If you want to reject this request follow this link - " \
                   f"{url_for('hello_world', _external=True)}?t={transaction.id}" \
                   f"&auth={hashlib.sha256(bytes(str(transaction.timestamp), encoding='utf-8')).hexdigest()}" \
                   f"&act=0\n\nRead&Return"

    mail.send(message)

    message = Message("Hey! Got what you are looking for?", sender=app.config["MAIL_USERNAME"], recipients=[
        recipient[1]
    ])
    message.body = f"Hey {recipient[0]},\nThis is an automated email from Read&Return. You requested a " \
                   f"the book {book.name} by {book.author} on our platform. A request has been sent to the owner" \
                   f" and you shall be contacted by them soon on your email. When you receive the book please" \
                   f" click following link to let us know - {url_for('hello_world', _external=True)}" \
                   f"?t={transaction.id}" \
                   f"&auth={hashlib.sha256(bytes(str(transaction.timestamp), encoding='utf-8')).hexdigest()}&act=1" \
                   f"\n\nRead&Return"

    mail.send(message)


@login_required
@app.route("/order", methods=["POST"])
def order_book():
    payload = request.get_json()
    book: Books = Books.query.filter_by(id=payload['bookId'], gone=False).first()

    if book:
        transaction = Transactions(book_id=book.id, user_email=current_user.email)
        recep = (current_user.name, current_user.email)
        db.session.add(transaction)
        db.session.commit()

        send_mail(book, transaction, recep)

        db.session.commit()

        return jsonify({"status": "request sent!"}), 200
    return jsonify({}), 404


@login_required
@app.route("/genres", methods=["GET", "POST"])
def get_genres():
    if request.method == "GET":
        print(Books.query.all())
        genres = set(map(lambda x: x.genre, Books.query.filter_by(gone=False).all()))
        print(genres)
        return jsonify({"genres": list(genres)}), 200

    if request.method == "POST":
        payload = request.get_json(silent=True)
        print(payload)
        if payload and "genre" in payload:
            books = Books.query.filter_by(genre=payload["genre"], gone=False).all()
            books = list(map(Books.get_dict, books))
            print(books)
            return jsonify({"books": books}), 200

    return abort(400)


@login_required
@app.route("/logout")
def logout():
    logout_user()
    return jsonify({}), 200


if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000, debug=True)
