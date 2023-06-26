from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware
from routes import user, auth
from dotenv import load_dotenv
import os
from pymongo.mongo_client import MongoClient
from custom_types.types import AuthenticatedStaticFiles
from celery.result import AsyncResult
from fastapi.responses import JSONResponse

load_dotenv()

app = FastAPI()
print(__name__)

app.add_middleware(CORSMiddleware, allow_origins="*",
                   allow_credentials=True, allow_methods=["*"], allow_headers=["*"])
app.mount("/static", AuthenticatedStaticFiles(directory="static"), name="static")


@app.on_event("startup")
def startup_db_client():
    app.mongodb_client = MongoClient(os.getenv("MONGO_URL"))
    app.database = app.mongodb_client["user-service"]


@app.on_event("shutdown")
def shutdown_db_client():
    app.mongodb_client.close()


app.include_router(auth.router, prefix='/auth')
app.include_router(user.router, prefix='/user')


@app.get("/tasks/{task_id}")
def get_task(task_id: str):
    task_result = AsyncResult(task_id)
    result = {
        "task_id": task_id,
        "task_status": task_result.status,
        "task_result": task_result.result
    }

    return JSONResponse(result)
