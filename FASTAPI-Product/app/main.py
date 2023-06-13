from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware
from dotenv import load_dotenv
import os
from custom_types.types import AuthenticatedStaticFiles
from sqlalchemy import create_engine
from sqlalchemy.orm import sessionmaker

load_dotenv()

app = FastAPI()

app.add_middleware(CORSMiddleware,allow_origins="*",allow_credentials=True,allow_methods=["*"],allow_headers=["*"])
app.mount("/static",AuthenticatedStaticFiles(directory="static"),name="static")


app.include_router(auth.router,prefix='/auth')
app.include_router(user.router,prefix='/user')
