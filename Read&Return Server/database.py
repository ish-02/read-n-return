import datetime

from flask_login import UserMixin
from flask_sqlalchemy import SQLAlchemy

db = SQLAlchemy()


class Users(UserMixin, db.Model):
    __tablename__ = "users"
    name = db.Column(db.String(500), nullable=False)
    email = db.Column(db.String(500), primary_key=True)
    password = db.Column(db.String(500), nullable=False)

    books = db.relationship("Books", backref='owner', lazy=True)
    transactions = db.relationship("Transactions", backref='user', lazy=True)

    is_authenticated = db.Column(db.Boolean, default=True)
    is_active = db.Column(db.Boolean, default=True)

    def get_id(self):
        return self.email

    @staticmethod
    def get_user(email):
        return Users.query.filter_by(email=email).first()


class Books(db.Model):
    __tablename__ = "books"
    id = db.Column(db.Integer, primary_key=True)
    name = db.Column(db.String(500), nullable=False)
    author = db.Column(db.String(500))
    genre = db.Column(db.String(500))
    description = db.Column(db.String(1000))
    owner_email = db.Column(db.String(500), db.ForeignKey("users.email"))
    date_added = db.Column(db.DateTime, default=datetime.datetime.now)
    gone = db.Column(db.Boolean, default=False, nullable=False)

    transactions = db.relationship("Transactions", backref='book', lazy=True)

    def get_dict(self):
        print(self.owner)
        if type(self) != Books:
            return None
        js = {
            "id": self.id,
            "name": self.name,
            "description": self.description,
            "genre": self.genre,
            "author": self.author,
            "owner": {
                "name": self.owner.name,
                "email": self.owner.email
            }
        }
        print(js)
        return js


class Transactions(db.Model):
    __tablename__ = "transactions"
    id = db.Column(db.Integer, primary_key=True)
    book_id = db.Column(db.Integer, db.ForeignKey("books.id"))
    user_email = db.Column(db.String(500), db.ForeignKey("users.email"))
    status = db.Column(db.String(100), default="pending", nullable=False)
    timestamp = db.Column(db.DateTime, default=datetime.datetime.now, nullable=False)

    def get_dict(self):
        return {
            "id": self.id,
            "book": {
                "id": self.book.id,
                "name": self.book.name,
                "author": self.book.author,
            },
            "user": {
                "email": self.user.email
            },
            "status": self.status
        }
