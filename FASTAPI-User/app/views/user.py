from custom_types.types import CustomRequest
from bson.objectid import ObjectId
from bson.errors import InvalidId


def get_user(request:CustomRequest,id:str):
    try:        
        user = request.app.database["users"].find_one({"_id":ObjectId(id)})
        if user:
            user["_id"] = str(user["_id"])
            return user
        else:
            return {"message": "User not found"}
    except InvalidId:
        return {"message": "Invalid user ID format"}
   
