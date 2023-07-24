import base64
import os

from fastapi import FastAPI, status, Header
from fastapi.responses import JSONResponse

import csv_service
import html_service
from responses.ResponseType import ResponseType
import uvicorn

from responses.utils.Utils import Utils
from responses.vector_list.VectorList import VectorList
from responses.vectors_inform.VectorData import VectorData

app = FastAPI()


@app.get("/find/{tg_id}")
def find(vtr: str, type_of_list: str, tg_id: int, snils: str = "", uuid: str = Header(...)):
    result = ResponseType().set_vector_name(vtr).set_uuid(uuid).set_tg_id(tg_id)
    result.set_vector_data(VectorData.parse_json(vtr, type_of_list)). \
        set_applicant_data(Utils.parse_json(vtr, snils, type_of_list))
    return JSONResponse(
        status_code=status.HTTP_200_OK if result.get_applicant_date() is not None or result.get_vector_data() is not None else status.HTTP_400_BAD_REQUEST,
        content=result.to_dict()
    )


@app.get("/find_vector_inform/{tg_id}")
def find1(vtr: str, tg_id: int, uuid: str = Header(...)):
    result = ResponseType().set_vector_name(vtr).set_uuid(uuid).set_tg_id(tg_id)
    result.set_vector_data(VectorData.parse_json(vtr))
    return JSONResponse(
        status_code=status.HTTP_200_OK if result.get_applicant_date() is not None or result.get_vector_data() is not None else status.HTTP_400_BAD_REQUEST,
        content=result.to_dict()
    )


@app.get("/all_vectors")
def get_all(uuid: str = Header(...)):
    result = ResponseType().set_uuid(uuid).set_vector_list(VectorList.get_vectors())
    return JSONResponse(
        status_code=status.HTTP_200_OK if result.has_vectors_list() is not None else status.HTTP_400_BAD_REQUEST,
        content=result.to_dict()
    )


@app.get("/all_vectors_lists")
def get_all_ability(vtr: str, uuid: str = Header(...)):
    result = ResponseType().set_uuid(uuid).set_vector_list(VectorList.get_ability_lists(vtr))
    return JSONResponse(
        status_code=status.HTTP_200_OK if result.has_vectors_list() is not None else status.HTTP_400_BAD_REQUEST,
        content=result.to_dict()
    )


@app.get("/csv/{tg_id}")
def download_csv(vtr: str, type_of_list: str, tg_id: int, uuid: str = Header(...)):
    filename = vtr + "-" + str(tg_id) + ".csv"
    csv_service.convert_json_to_csv(r"../Python/vectors/" + vtr + "&" + type_of_list + ".json",
                                    r"../Python/csv_files/" + filename)
    response = ResponseType().set_uuid(uuid).set_tg_id(tg_id).set_vector_name(vtr)
    with open(r"../Python/csv_files/" + filename, "rb") as file:
        file_bytes = file.read()

    file_base64 = base64.b64encode(file_bytes).decode('utf-8')
    response.set_file(file_base64)
    os.remove(r"../Python/csv_files/" + filename)
    return JSONResponse(
        status_code=status.HTTP_200_OK if response.has_file() is not None else status.HTTP_400_BAD_REQUEST,
        content=response.to_dict()
    )


@app.get("/html/{tg_id}")
def download_html(vtr: str, type_of_list: str, tg_id: int, uuid: str = Header(...)):
    filename = vtr + "-" + str(tg_id) + ".html"
    html_service.convert_json_to_html(r"../Python/vectors/" + vtr + "&" + type_of_list + ".json",
                                      r"../Python/html_files/" + filename)
    response = ResponseType().set_uuid(uuid).set_tg_id(tg_id).set_vector_name(vtr)
    with open(r"../Python/html_files/" + filename, "rb") as file:
        file_bytes = file.read()

    file_base64 = base64.b64encode(file_bytes).decode('utf-8')
    response.set_file(file_base64)
    os.remove(r"../Python/html_files/" + filename)
    return JSONResponse(
        status_code=status.HTTP_200_OK if response.has_file() is not None else status.HTTP_400_BAD_REQUEST,
        content=response.to_dict()
    )


if __name__ == "__main__":
    uvicorn.run(app, host="localhost", port=8000)
