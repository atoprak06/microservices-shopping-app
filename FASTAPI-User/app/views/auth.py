from models.User import User, RegisterUser
from fastapi import HTTPException, status, Depends
from fastapi.security.oauth2 import OAuth2PasswordRequestForm
from passlib.context import CryptContext
from jose import jwt
from dotenv import load_dotenv
import os
from datetime import datetime, timedelta
from typing import Annotated
from custom_types.types import CustomRequest
from datetime import datetime
import random
from starlette.responses import JSONResponse
from worker import picture_resize
from middleware.verify_token import verify_token
load_dotenv()

pwd_context = CryptContext(schemes=["bcrypt"], deprecated="auto")


async def register(request: CustomRequest, user: RegisterUser):
    collection = request.app.database["users"]
    existing_user = collection.find_one({"email": user.email})
    if existing_user:
        raise HTTPException(
            status_code=status.HTTP_409_CONFLICT, detail="User already exists")
    file_name = None
    if user.picture is not None:
        content_type = user.picture.content_type
        if content_type not in ["image/jpeg", "image/jpg", "image/png"]:
            return JSONResponse(status_code=status.HTTP_415_UNSUPPORTED_MEDIA_TYPE, content="Only JPEG,JPG,PNG files are allowed.")
        file_name = f"{random.randint(0000000000,9999999999)}_{user.picture.filename}"
        file_path = f"static/{file_name}"
        with open(file_path, "wb") as file:
            contents = await user.picture.read()
            file.write(contents)
        task = picture_resize.delay(file_name)
        print(task.id)
    user.password = pwd_context.hash(user.password)
    new_user = User(first_name=user.first_name, last_name=user.last_name,
                    email=user.email, location=user.location, password=user.password)
    new_user.picture_path = file_name
    collection.insert_one(new_user.dict())
    return {"message": "User created succesfully"}


def login(request: CustomRequest, user: Annotated[OAuth2PasswordRequestForm, Depends()]):
    login_user = request.app.database['users'].find_one(
        {"email": user.username})
    if not login_user:
        raise HTTPException(
            status_code=status.HTTP_401_UNAUTHORIZED, detail="This user does not exists")
    is_password_valid = pwd_context.verify(
        user.password, login_user["password"])
    if not is_password_valid:
        raise HTTPException(
            status_code=status.HTTP_401_UNAUTHORIZED, detail="Password is not correct")
    payload = {
        "id": str(login_user["_id"]),
        "exp": datetime.utcnow()+timedelta(days=7)
    }
    token = jwt.encode(payload, os.getenv("JWT_SECRET"), algorithm="HS256")
    return {"access_token": token, "token_type": "bearer"}


async def verifyToken(request: CustomRequest):
    credentials_exception = HTTPException(status_code=status.HTTP_401_UNAUTHORIZED,
                                          detail="Could not validate credentials", headers={"WWW-Authenticate": "Bearer"})
    authorization_header = None
    for key, value in request["headers"]:
        if key.lower() == b'authorization':
            authorization_header = value.decode("utf-8")
            break
    if authorization_header is not None:
        current_user = await verify_token(authorization_header.split(" ")[1])
        return current_user
    raise credentials_exception
