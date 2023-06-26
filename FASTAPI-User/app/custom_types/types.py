from fastapi import FastAPI, Request, HTTPException, status
from fastapi.staticfiles import StaticFiles
from pymongo.database import Database
from starlette.types import Scope
from middleware.verify_token import verify_token


class CustomApi(FastAPI):
    database: Database


class CustomRequest(Request):
    app: CustomApi


class AuthenticatedStaticFiles(StaticFiles):
    async def get_response(self, path: str, scope: Scope):
        credentials_exception = HTTPException(status_code=status.HTTP_401_UNAUTHORIZED,
                                              detail="Could not validate credentials", headers={"WWW-Authenticate": "Bearer"})
        authorization_header = None
        for key, value in scope["headers"]:
            if key.lower() == b'authorization':
                authorization_header = value.decode("utf-8")
                break
        if authorization_header is not None:
            current_user = await verify_token(authorization_header.split(" ")[1])
            if current_user is not None:
                return await super().get_response(path, scope)
        raise credentials_exception
