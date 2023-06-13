from fastapi import APIRouter,Depends
from fastapi.security.oauth2 import OAuth2PasswordRequestForm
from views.auth import register,login
from models.User import RegisterUser
from typing import Annotated
from custom_types.types import CustomRequest


router = APIRouter()

@router.post("/register")
async def create_new_user_route(request:CustomRequest,user:RegisterUser=Depends()):        
    return await register(request,user)

@router.post("/login")
def get_token_route(request:CustomRequest,user:Annotated[OAuth2PasswordRequestForm,Depends()]):  
    return login(request,user)
