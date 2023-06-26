from celery import Celery
from PIL import Image

celery = Celery(__name__)
celery.config_from_object('celeryconfig')


@celery.task(name="picture_resize")
def picture_resize(file_name):
    file_path = f"static/{file_name}"
    with Image.open(file_path) as image:
        resized_image = image.resize((400, 300))
        resized_image.save(file_path)
    return {"result": "picture is uploaded"}
