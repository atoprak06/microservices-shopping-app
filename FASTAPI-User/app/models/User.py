from pydantic import BaseModel,Field,EmailStr
from datetime import datetime
from fastapi import UploadFile,File,Form

class RegisterUser(BaseModel):
    first_name : str = Form(...,max_length=50,min_length=2)
    last_name : str = Form(...,max_length=50,min_length=2)
    email : EmailStr = Form(...)
    password : str = Form(...,min_length=8)
    location : str = Form(None)
    picture:UploadFile=File(default=None)


class User(BaseModel):
    first_name : str = Field(...,max_length=50,min_length=2)
    last_name : str = Field(...,max_length=50,min_length=2)
    email : EmailStr = Field(...)
    password : str = Field(...,min_length=5)
    location : str = None  
    picture_path : str = None
    friends : list = None   
    description:str = Field(default=None,max_length=150)
    created_at : datetime = Field(default_factory=datetime.utcnow)
    update_at : datetime = Field(default_factory=datetime.utcnow)


