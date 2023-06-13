from fastapi import APIRouter,Depends,Request
from middleware.verify_token import verify_token
from views.user import get_user

router = APIRouter()

@router.get('/{id}')
def get_user_route(request:Request,id:str,current_user:str=Depends(verify_token)):
    return get_user(request,id)
