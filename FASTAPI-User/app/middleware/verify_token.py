from fastapi import Depends,HTTPException,status
from fastapi.security.oauth2 import OAuth2PasswordBearer
from typing import Annotated
from jose import jwt,JWTError
from dotenv import load_dotenv
import os

load_dotenv()

oauth2_scheme = OAuth2PasswordBearer(tokenUrl="auth/login")

async def verify_token(token:Annotated[str|None,Depends(oauth2_scheme)]):
    credentials_exception = HTTPException(status_code=status.HTTP_401_UNAUTHORIZED,detail="Could not validate credentials",headers={"WWW-Authenticate":"Bearer"})
    print(token)
    try:
        decoded_data = jwt.decode(token,key=os.getenv("JWT_SECRET"),algorithms="HS256")        
        return decoded_data["id"]
    except JWTError:
        raise credentials_exception